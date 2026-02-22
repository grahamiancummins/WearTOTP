package com.symbolscope.weartotp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.symbolscope.weartotp.data.Site
import com.symbolscope.weartotp.presentation.viewmodel.MainViewModel

@Composable
fun SiteListScreen(
    viewModel: MainViewModel,
    onSiteClick: (String) -> Unit,
    onAddClick: () -> Unit
) {
    val sites by viewModel.sites.collectAsState()
    var siteToDelete by remember { mutableStateOf<Site?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (sites.isEmpty()) {
                item {
                    Text(
                        text = "No sites yet.\nTap + to add one.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
            items(sites) { site ->
                Chip(
                    onClick = {},
                    label = { Text(site.name) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(site.name) {
                            detectTapGestures(
                                onTap = { onSiteClick(site.name) },
                                onLongPress = { siteToDelete = site }
                            )
                        },
                    colors = ChipDefaults.primaryChipColors()
                )
            }
            item {
                Button(
                    onClick = onAddClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("+  Add Site")
                }
            }
        }

        if (siteToDelete != null) {
            DeleteConfirmationOverlay(
                siteName = siteToDelete!!.name,
                onConfirm = {
                    viewModel.deleteSite(siteToDelete!!)
                    siteToDelete = null
                },
                onDismiss = { siteToDelete = null }
            )
        }
    }
}

@Composable
private fun DeleteConfirmationOverlay(
    siteName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background.copy(alpha = 0.95f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Delete '$siteName'?",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onDismiss) {
                    Text("No")
                }
                Button(
                    onClick = onConfirm,
                    colors = androidx.wear.compose.material.ButtonDefaults.secondaryButtonColors()
                ) {
                    Text("Yes")
                }
            }
        }
    }
}
