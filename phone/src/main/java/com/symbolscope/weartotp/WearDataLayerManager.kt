package com.symbolscope.weartotp

import android.content.Context
import com.google.android.gms.wearable.Wearable

object WearDataLayerManager {

    fun sendSite(context: Context, name: String, secret: String) {
        val payload = "$name|$secret".toByteArray(Charsets.UTF_8)
        Wearable.getNodeClient(context).connectedNodes
            .addOnSuccessListener { nodes ->
                nodes.forEach { node ->
                    Wearable.getMessageClient(context)
                        .sendMessage(node.id, "/add_site", payload)
                        .addOnFailureListener { e ->
                            e.printStackTrace()
                        }
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
