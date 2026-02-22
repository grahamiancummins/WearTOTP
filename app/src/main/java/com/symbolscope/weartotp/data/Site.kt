package com.symbolscope.weartotp.data

import kotlinx.serialization.Serializable

@Serializable
data class Site(val name: String, val secret: String)
