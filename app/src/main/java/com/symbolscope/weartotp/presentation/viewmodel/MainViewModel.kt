package com.symbolscope.weartotp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.symbolscope.weartotp.data.Site
import com.symbolscope.weartotp.data.SiteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SiteRepository(application)

    val sites: StateFlow<List<Site>> = repository.sites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addSite(name: String, secret: String) {
        viewModelScope.launch {
            repository.addSite(Site(name = name.trim(), secret = secret.trim()))
        }
    }

    fun deleteSite(site: Site) {
        viewModelScope.launch {
            repository.deleteSite(site)
        }
    }
}
