package com.symbolscope.weartotp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.symbolscope.weartotp.presentation.viewmodel.MainViewModel
import com.symbolscope.weartotp.totp.TotpGenerator
import kotlinx.coroutines.delay

@Composable
fun TotpCodeScreen(
    siteName: String,
    viewModel: MainViewModel
) {
    val sites by viewModel.sites.collectAsState()
    val site = sites.find { it.name == siteName }

    var timeMs by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1_000L)
            timeMs = System.currentTimeMillis()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (site != null) {
            val code = remember(site.secret, timeMs / 30_000) {
                TotpGenerator.generateCode(site.secret, timeMs)
            }
            val secondsLeft = TotpGenerator.secondsUntilNext(timeMs)
            val progress = secondsLeft / 30f

            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 4.dp
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = site.name,
                    style = MaterialTheme.typography.caption1,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${code.substring(0, 3)} ${code.substring(3)}",
                    style = MaterialTheme.typography.display3,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${secondsLeft}s",
                    style = MaterialTheme.typography.caption2,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Text("Site not found")
        }
    }
}
