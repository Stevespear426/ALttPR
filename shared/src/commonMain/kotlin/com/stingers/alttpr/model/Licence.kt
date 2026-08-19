package com.stingers.alttpr.model

data class Licence(
    val name: String,
    val type: LicenceType,
    val url: String,
    val version: String? = null
)