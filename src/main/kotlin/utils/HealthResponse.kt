package com.streaming.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse (val status: String)