package com.symbolscope.weartotp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import java.net.URLDecoder

class QrScanActivity : ComponentActivity() {

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
        val contents = result.contents
        if (contents != null) {
            val parsed = parseOtpAuthUri(contents)
            if (parsed != null) {
                val (name, secret) = parsed
                WearDataLayerManager.sendSite(this, name, secret)
                Toast.makeText(this, "Sent '$name' to watch", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Not a valid OTP QR code", Toast.LENGTH_LONG).show()
            }
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val options = ScanOptions().apply {
            setPrompt("Scan a TOTP QR code")
            setBeepEnabled(false)
            setOrientationLocked(false)
        }
        barcodeLauncher.launch(options)
    }

    private fun parseOtpAuthUri(uri: String): Pair<String, String>? {
        if (!uri.startsWith("otpauth://totp/")) return null
        val withoutScheme = uri.removePrefix("otpauth://totp/")
        val labelEncoded = withoutScheme.substringBefore("?")
        val label = try {
            URLDecoder.decode(labelEncoded, "UTF-8")
        } catch (e: Exception) {
            labelEncoded
        }
        val name = if (":" in label) label.substringAfter(":").trim() else label.trim()
        val queryString = withoutScheme.substringAfter("?", "")
        val params = queryString.split("&").associate { param ->
            val parts = param.split("=", limit = 2)
            if (parts.size == 2) parts[0] to parts[1] else parts[0] to ""
        }
        val secret = params["secret"]?.takeIf { it.isNotBlank() } ?: return null
        return Pair(name, secret)
    }
}
