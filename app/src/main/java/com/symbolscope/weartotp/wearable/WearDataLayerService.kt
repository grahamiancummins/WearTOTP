package com.symbolscope.weartotp.wearable

import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import com.symbolscope.weartotp.data.Site
import com.symbolscope.weartotp.data.SiteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class WearDataLayerService : WearableListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/add_site") {
            val payload = String(messageEvent.data, Charsets.UTF_8)
            val parts = payload.split("|", limit = 2)
            if (parts.size == 2) {
                val site = Site(name = parts[0], secret = parts[1])
                scope.launch {
                    SiteRepository(applicationContext).addSite(site)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
