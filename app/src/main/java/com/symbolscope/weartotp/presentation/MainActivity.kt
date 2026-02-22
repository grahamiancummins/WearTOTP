package com.symbolscope.weartotp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.symbolscope.weartotp.presentation.navigation.AppNavigation
import com.symbolscope.weartotp.presentation.theme.WearTOTPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)
        setContent {
            WearTOTPTheme {
                AppNavigation()
            }
        }
    }
}
