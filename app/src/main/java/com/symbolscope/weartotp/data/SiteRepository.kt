package com.symbolscope.weartotp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sites")
private val SITES_KEY = stringPreferencesKey("sites_json")

class SiteRepository(private val context: Context) {

    val sites: Flow<List<Site>> = context.dataStore.data.map { prefs ->
        val json = prefs[SITES_KEY] ?: "[]"
        try {
            Json.decodeFromString<List<Site>>(json)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addSite(site: Site) {
        context.dataStore.edit { prefs ->
            val current = try {
                Json.decodeFromString<List<Site>>(prefs[SITES_KEY] ?: "[]")
            } catch (e: Exception) {
                emptyList()
            }
            prefs[SITES_KEY] = Json.encodeToString(current + site)
        }
    }

    suspend fun deleteSite(site: Site) {
        context.dataStore.edit { prefs ->
            val current = try {
                Json.decodeFromString<List<Site>>(prefs[SITES_KEY] ?: "[]")
            } catch (e: Exception) {
                emptyList()
            }
            prefs[SITES_KEY] = Json.encodeToString(current.filter { it.name != site.name })
        }
    }
}
