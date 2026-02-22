package com.symbolscope.weartotp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(
                        onScanQr = {
                            startActivity(Intent(this, QrScanActivity::class.java))
                        },
                        onImportYaml = {
                            startActivity(Intent(this, YamlImportActivity::class.java))
                        },
                        onEnterKey = {
                            startActivity(Intent(this, EnterKeyActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MainScreen(
    onScanQr: () -> Unit,
    onImportYaml: () -> Unit,
    onEnterKey: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "WearTOTP",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Send a site to your watch",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onScanQr,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Scan QR Code")
        }
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = onImportYaml,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Import from YAML")
        }
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = onEnterKey,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enter Key")
        }
    }
}
