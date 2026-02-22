package com.symbolscope.weartotp

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class YamlImportActivity : ComponentActivity() {

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            importFromUri(uri)
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filePickerLauncher.launch(arrayOf("*/*"))
    }

    private fun importFromUri(uri: Uri) {
        try {
            val content = contentResolver.openInputStream(uri)?.bufferedReader()?.readText()
                ?: run {
                    Toast.makeText(this, "Could not read file", Toast.LENGTH_LONG).show()
                    return
                }
            val sites = parseYaml(content)
            if (sites.isEmpty()) {
                Toast.makeText(this, "No sites found in YAML", Toast.LENGTH_LONG).show()
                return
            }
            sites.forEach { (name, secret) ->
                WearDataLayerManager.sendSite(this, name, secret)
            }
            Toast.makeText(this, "Sent ${sites.size} site(s) to watch", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun parseYaml(content: String): List<Pair<String, String>> {
        val sites = mutableListOf<Pair<String, String>>()
        var currentName: String? = null

        for (rawLine in content.lines()) {
            val line = rawLine.trimStart()
            when {
                line.startsWith("- name:") -> {
                    currentName = line.removePrefix("- name:").trim()
                }
                line.startsWith("name:") && currentName == null -> {
                    currentName = line.removePrefix("name:").trim()
                }
                (line.startsWith("key:") || line.startsWith("secret:")) && currentName != null -> {
                    val secret = line
                        .removePrefix("secret:")
                        .removePrefix("key:")
                        .trim()
                    if (secret.isNotBlank()) {
                        sites.add(Pair(currentName!!, secret))
                    }
                    currentName = null
                }
            }
        }
        return sites
    }
}
