package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.ActiveSubscription
import com.example.model.InternetPackage
import com.example.model.PredefinedPackages
import com.example.model.SmsLog
import com.example.model.SmsType
import com.example.model.UserProfile
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class GlobalNetRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("globalnet_secure_storage", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_FULL_NAME = "key_full_name"
        private const val KEY_PHONE = "key_phone"
        private const val KEY_COUNTRY = "key_country"
        private const val KEY_CITY = "key_city"
        private const val KEY_LOCATION = "key_location"
        private const val KEY_LAT = "key_lat"
        private const val KEY_LNG = "key_lng"
        private const val KEY_REGISTERED = "key_registered"

        private const val KEY_ACTIVE_CODE = "key_active_code"
        private const val KEY_ACTIVE_NAME = "key_active_name"
        private const val KEY_INIT_GEN_GB = "key_init_gen_gb"
        private const val KEY_REM_GEN_GB = "key_rem_gen_gb"
        private const val KEY_INIT_SOC_GB = "key_init_soc_gb"
        private const val KEY_REM_SOC_GB = "key_rem_soc_gb"
        private const val KEY_ACTIVATED_AT = "key_activated_at"
        private const val KEY_EXPIRES_AT = "key_expires_at"
        private const val KEY_VALIDITY_DAYS = "key_validity_days"
        private const val KEY_IS_CONNECTED = "key_is_connected"

        private const val KEY_SMS_LOGS = "key_sms_logs"
    }

    fun getUserProfile(): UserProfile {
        val name = prefs.getString(KEY_FULL_NAME, "") ?: ""
        val phone = prefs.getString(KEY_PHONE, "") ?: ""
        val country = prefs.getString(KEY_COUNTRY, "") ?: ""
        val city = prefs.getString(KEY_CITY, "") ?: ""
        val loc = prefs.getString(KEY_LOCATION, "") ?: ""
        val lat = if (prefs.contains(KEY_LAT)) prefs.getFloat(KEY_LAT, 0f).toDouble() else null
        val lng = if (prefs.contains(KEY_LNG)) prefs.getFloat(KEY_LNG, 0f).toDouble() else null
        val isReg = prefs.getBoolean(KEY_REGISTERED, false)

        return UserProfile(
            fullName = name,
            phoneNumber = phone,
            country = country,
            city = city,
            preciseLocation = loc,
            latitude = lat,
            longitude = lng,
            isRegistered = isReg
        )
    }

    fun saveUserProfile(profile: UserProfile) {
        prefs.edit().apply {
            putString(KEY_FULL_NAME, profile.fullName)
            putString(KEY_PHONE, profile.phoneNumber)
            putString(KEY_COUNTRY, profile.country)
            putString(KEY_CITY, profile.city)
            putString(KEY_LOCATION, profile.preciseLocation)
            if (profile.latitude != null) putFloat(KEY_LAT, profile.latitude.toFloat()) else remove(KEY_LAT)
            if (profile.longitude != null) putFloat(KEY_LNG, profile.longitude.toFloat()) else remove(KEY_LNG)
            putBoolean(KEY_REGISTERED, profile.isRegistered)
            apply()
        }
    }

    fun getActiveSubscription(): ActiveSubscription? {
        val code = prefs.getString(KEY_ACTIVE_CODE, null) ?: return null
        val name = prefs.getString(KEY_ACTIVE_NAME, "Free Data Package") ?: "Free Data Package"
        val initGen = prefs.getFloat(KEY_INIT_GEN_GB, 0f).toDouble()
        val remGen = prefs.getFloat(KEY_REM_GEN_GB, 0f).toDouble()
        val initSoc = prefs.getFloat(KEY_INIT_SOC_GB, 0f).toDouble()
        val remSoc = prefs.getFloat(KEY_REM_SOC_GB, 0f).toDouble()
        val actAt = prefs.getLong(KEY_ACTIVATED_AT, 0L)
        val expAt = prefs.getLong(KEY_EXPIRES_AT, 0L)
        val days = prefs.getInt(KEY_VALIDITY_DAYS, 15)
        val isConn = prefs.getBoolean(KEY_IS_CONNECTED, true)

        return ActiveSubscription(
            packageCode = code,
            packageName = name,
            initialGeneralGb = initGen,
            remainingGeneralGb = remGen,
            initialSocialGb = initSoc,
            remainingSocialGb = remSoc,
            activatedAtMillis = actAt,
            expiresAtMillis = expAt,
            validityDays = days,
            isConnected = isConn
        )
    }

    fun activatePackage(pkg: InternetPackage): ActiveSubscription {
        val now = System.currentTimeMillis()
        val expiresAt = now + (pkg.validityDays.toLong() * 24 * 60 * 60 * 1000)
        val sub = ActiveSubscription(
            packageCode = pkg.code,
            packageName = pkg.title,
            initialGeneralGb = pkg.generalDataGb.toDouble(),
            remainingGeneralGb = pkg.generalDataGb.toDouble(),
            initialSocialGb = pkg.socialDataGb.toDouble(),
            remainingSocialGb = pkg.socialDataGb.toDouble(),
            activatedAtMillis = now,
            expiresAtMillis = expiresAt,
            validityDays = pkg.validityDays,
            isConnected = true
        )

        prefs.edit().apply {
            putString(KEY_ACTIVE_CODE, sub.packageCode)
            putString(KEY_ACTIVE_NAME, sub.packageName)
            putFloat(KEY_INIT_GEN_GB, sub.initialGeneralGb.toFloat())
            putFloat(KEY_REM_GEN_GB, sub.remainingGeneralGb.toFloat())
            putFloat(KEY_INIT_SOC_GB, sub.initialSocialGb.toFloat())
            putFloat(KEY_REM_SOC_GB, sub.remainingSocialGb.toFloat())
            putLong(KEY_ACTIVATED_AT, sub.activatedAtMillis)
            putLong(KEY_EXPIRES_AT, sub.expiresAtMillis)
            putInt(KEY_VALIDITY_DAYS, sub.validityDays)
            putBoolean(KEY_IS_CONNECTED, sub.isConnected)
            apply()
        }

        return sub
    }

    fun setConnectionState(isConnected: Boolean) {
        prefs.edit().putBoolean(KEY_IS_CONNECTED, isConnected).apply()
    }

    fun clearActiveSubscription() {
        prefs.edit().apply {
            remove(KEY_ACTIVE_CODE)
            remove(KEY_ACTIVE_NAME)
            remove(KEY_INIT_GEN_GB)
            remove(KEY_REM_GEN_GB)
            remove(KEY_INIT_SOC_GB)
            remove(KEY_REM_SOC_GB)
            remove(KEY_ACTIVATED_AT)
            remove(KEY_EXPIRES_AT)
            remove(KEY_VALIDITY_DAYS)
            remove(KEY_IS_CONNECTED)
            apply()
        }
    }

    fun getSmsLogs(): List<SmsLog> {
        val raw = prefs.getString(KEY_SMS_LOGS, null) ?: return emptyList()
        val list = mutableListOf<SmsLog>()
        try {
            val jsonArray = JSONArray(raw)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    SmsLog(
                        id = obj.getString("id"),
                        type = SmsType.valueOf(obj.getString("type")),
                        packageCode = obj.getString("packageCode"),
                        timestamp = obj.getLong("timestamp"),
                        messageBody = obj.getString("messageBody"),
                        recipientOrSender = obj.getString("recipientOrSender"),
                        status = obj.getString("status")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list.sortedByDescending { it.timestamp }
    }

    fun addSmsLog(log: SmsLog) {
        val current = getSmsLogs().toMutableList()
        current.add(0, log)
        val jsonArray = JSONArray()
        current.take(30).forEach { item ->
            val obj = JSONObject().apply {
                put("id", item.id)
                put("type", item.type.name)
                put("packageCode", item.packageCode)
                put("timestamp", item.timestamp)
                put("messageBody", item.messageBody)
                put("recipientOrSender", item.recipientOrSender)
                put("status", item.status)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_SMS_LOGS, jsonArray.toString()).apply()
    }
}
