package com.example.model

data class SmsLog(
    val id: String,
    val type: SmsType,
    val packageCode: String,
    val timestamp: Long,
    val messageBody: String,
    val recipientOrSender: String,
    val status: String
)

enum class SmsType {
    OUTGOING_PACKAGE_REQUEST,
    INCOMING_ACTIVATION_CONFIRMATION
}
