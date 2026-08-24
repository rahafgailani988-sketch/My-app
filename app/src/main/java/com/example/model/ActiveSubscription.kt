package com.example.model

data class ActiveSubscription(
    val packageCode: String,
    val packageName: String,
    val initialGeneralGb: Double,
    val remainingGeneralGb: Double,
    val initialSocialGb: Double,
    val remainingSocialGb: Double,
    val activatedAtMillis: Long,
    val expiresAtMillis: Long,
    val validityDays: Int,
    val isConnected: Boolean = true,
    val assignedIpAddress: String = "192.0.2.78",
    val pingMs: Int = 18,
    val downloadSpeedMbps: Double = 420.5,
    val uploadSpeedMbps: Double = 98.4
) {
    val totalRemainingGb: Double get() = remainingGeneralGb + remainingSocialGb
    val totalInitialGb: Double get() = initialGeneralGb + initialSocialGb
    val generalProgress: Float get() = if (initialGeneralGb > 0) (remainingGeneralGb / initialGeneralGb).toFloat().coerceIn(0f, 1f) else 0f
    val socialProgress: Float get() = if (initialSocialGb > 0) (remainingSocialGb / initialSocialGb).toFloat().coerceIn(0f, 1f) else 0f
    val isExpired: Boolean get() = System.currentTimeMillis() >= expiresAtMillis
    
    val remainingDays: Long get() {
        val diff = expiresAtMillis - System.currentTimeMillis()
        return if (diff > 0) diff / (1000 * 60 * 60 * 24) else 0
    }

    val remainingHours: Long get() {
        val diff = expiresAtMillis - System.currentTimeMillis()
        return if (diff > 0) (diff / (1000 * 60 * 60)) % 24 else 0
    }
}
