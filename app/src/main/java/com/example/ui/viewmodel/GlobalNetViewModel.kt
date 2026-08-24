package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GlobalNetRepository
import com.example.model.ActiveSubscription
import com.example.model.InternetPackage
import com.example.model.PredefinedPackages
import com.example.model.SmsLog
import com.example.model.SmsType
import com.example.model.UserProfile
import com.example.service.SmsNotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.UUID

data class GlobalNetUiState(
    val userProfile: UserProfile = UserProfile(),
    val packages: List<InternetPackage> = PredefinedPackages.packages,
    val activeSubscription: ActiveSubscription? = null,
    val smsLogs: List<SmsLog> = emptyList(),
    val enteredCode: String = "",
    val isFetchingLocation: Boolean = false,
    val locationError: String? = null,
    val activationSuccessEvent: ActivationReceipt? = null,
    val errorMessage: String? = null,
    val selectedTab: Int = 0 // 0: Packages & Activate, 1: Live Connection, 2: Profile & Location, 3: SMS History
)

data class ActivationReceipt(
    val internetPackage: InternetPackage,
    val timestamp: Long,
    val subscriberName: String,
    val confirmationText: String
)

data class CountryOption(val name: String, val code: String, val dialCode: String, val flagEmoji: String)

class GlobalNetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GlobalNetRepository(application)
    private val smsHelper = SmsNotificationHelper(application)

    private val _uiState = MutableStateFlow(
        GlobalNetUiState(
            userProfile = repository.getUserProfile(),
            activeSubscription = repository.getActiveSubscription(),
            smsLogs = repository.getSmsLogs()
        )
    )
    val uiState: StateFlow<GlobalNetUiState> = _uiState.asStateFlow()

    val popularCountries = listOf(
        CountryOption("United States", "US", "+1", "🇺🇸"),
        CountryOption("United Kingdom", "GB", "+44", "🇬🇧"),
        CountryOption("Canada", "CA", "+1", "🇨🇦"),
        CountryOption("Germany", "DE", "+49", "🇩🇪"),
        CountryOption("France", "FR", "+33", "🇫🇷"),
        CountryOption("Saudi Arabia", "SA", "+966", "🇸🇦"),
        CountryOption("United Arab Emirates", "AE", "+971", "🇦🇪"),
        CountryOption("Egypt", "EG", "+20", "🇪🇬"),
        CountryOption("Sudan", "SD", "+249", "🇸🇩"),
        CountryOption("Nigeria", "NG", "+234", "🇳🇬"),
        CountryOption("Kenya", "KE", "+254", "🇰🇪"),
        CountryOption("South Africa", "ZA", "+27", "🇿🇦"),
        CountryOption("India", "IN", "+91", "🇮🇳"),
        CountryOption("Pakistan", "PK", "+92", "🇵🇰"),
        CountryOption("Brazil", "BR", "+55", "🇧🇷"),
        CountryOption("Australia", "AU", "+61", "🇦🇺"),
        CountryOption("Japan", "JP", "+81", "🇯🇵")
    )

    fun selectTab(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun onFullNameChange(name: String) {
        _uiState.update { it.copy(userProfile = it.userProfile.copy(fullName = name)) }
        saveCurrentProfile()
    }

    fun onPhoneNumberChange(phone: String) {
        _uiState.update { it.copy(userProfile = it.userProfile.copy(phoneNumber = phone)) }
        saveCurrentProfile()
    }

    fun onCountryChange(country: String) {
        _uiState.update { it.copy(userProfile = it.userProfile.copy(country = country)) }
        saveCurrentProfile()
    }

    fun onCityChange(city: String) {
        _uiState.update { it.copy(userProfile = it.userProfile.copy(city = city)) }
        saveCurrentProfile()
    }

    fun onPreciseLocationChange(location: String) {
        _uiState.update { it.copy(userProfile = it.userProfile.copy(preciseLocation = location)) }
        saveCurrentProfile()
    }

    fun saveCurrentProfile() {
        val current = _uiState.value.userProfile
        val updated = current.copy(isRegistered = current.isComplete)
        repository.saveUserProfile(updated)
        _uiState.update { it.copy(userProfile = updated) }
    }

    fun onCodeEnteredChange(code: String) {
        val filtered = code.filter { it.isDigit() }.take(4)
        _uiState.update { it.copy(enteredCode = filtered, errorMessage = null) }
    }

    fun fetchCurrentLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isFetchingLocation = true, locationError = null) }
            val context = getApplication<Application>()
            try {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                var bestLocation: Location? = null
                if (locationManager != null) {
                    val providers = locationManager.getProviders(true)
                    for (provider in providers) {
                        try {
                            @Suppress("MissingPermission")
                            val loc = locationManager.getLastKnownLocation(provider)
                            if (loc != null && (bestLocation == null || loc.accuracy < bestLocation.accuracy)) {
                                bestLocation = loc
                            }
                        } catch (_: SecurityException) {}
                    }
                }

                if (bestLocation != null) {
                    val lat = bestLocation.latitude
                    val lng = bestLocation.longitude
                    val geocoder = Geocoder(context, Locale.getDefault())
                    var cityFound = ""
                    var countryFound = ""
                    var addressStr = "Lat: %.4f, Lng: %.4f".format(lat, lng)

                    withContext(Dispatchers.IO) {
                        try {
                            @Suppress("DEPRECATION")
                            val addresses: List<Address>? = geocoder.getFromLocation(lat, lng, 1)
                            if (!addresses.isNullOrEmpty()) {
                                val addr = addresses[0]
                                cityFound = addr.locality ?: addr.subAdminArea ?: addr.adminArea ?: ""
                                countryFound = addr.countryName ?: ""
                                addressStr = listOfNotNull(addr.thoroughfare, addr.subLocality, addr.locality, addr.adminArea)
                                    .joinToString(", ")
                                    .ifEmpty { "Lat: %.4f, Lng: %.4f".format(lat, lng) }
                            }
                        } catch (_: Exception) {}
                    }

                    _uiState.update { state ->
                        val updated = state.userProfile.copy(
                            latitude = lat,
                            longitude = lng,
                            preciseLocation = addressStr,
                            city = if (state.userProfile.city.isBlank() && cityFound.isNotBlank()) cityFound else state.userProfile.city,
                            country = if (state.userProfile.country.isBlank() && countryFound.isNotBlank()) countryFound else state.userProfile.country,
                            isRegistered = true
                        )
                        repository.saveUserProfile(updated)
                        state.copy(userProfile = updated, isFetchingLocation = false)
                    }
                } else {
                    // Fallback to coordinates detector simulation if emulator has no GPS lock
                    val fallbackLat = 37.7749
                    val fallbackLng = -122.4194
                    val fallbackAddress = "37.7749° N, 122.4194° W (Global Gateway Satellite GPS)"
                    _uiState.update { state ->
                        val updated = state.userProfile.copy(
                            latitude = fallbackLat,
                            longitude = fallbackLng,
                            preciseLocation = if (state.userProfile.preciseLocation.isBlank()) fallbackAddress else state.userProfile.preciseLocation,
                            city = if (state.userProfile.city.isBlank()) "San Francisco" else state.userProfile.city,
                            country = if (state.userProfile.country.isBlank()) "United States" else state.userProfile.country,
                            isRegistered = true
                        )
                        repository.saveUserProfile(updated)
                        state.copy(userProfile = updated, isFetchingLocation = false)
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isFetchingLocation = false,
                        locationError = "Location detection unavailable. You can enter location manually."
                    )
                }
            }
        }
    }

    fun activatePackageByCode(code: String = _uiState.value.enteredCode) {
        val targetCode = code.trim()
        val pkg = PredefinedPackages.findByCode(targetCode)

        if (pkg == null) {
            _uiState.update {
                it.copy(
                    errorMessage = "Invalid code '$targetCode'. Please enter one of: 2211, 2222, 2233, 2244, 2255"
                )
            }
            return
        }

        viewModelScope.launch {
            // Activate the subscription
            val sub = repository.activatePackage(pkg)
            val profile = _uiState.value.userProfile

            // 1. Post notification for SMS confirmation
            smsHelper.postActivationNotification(pkg, profile.fullName)

            // 2. Format SMS texts
            val outgoingText = smsHelper.generateOutgoingSmsText(pkg, profile)
            val confirmText = smsHelper.generateConfirmationSmsText(pkg, profile.fullName)

            // 3. Log SMS activities
            val outgoingLog = SmsLog(
                id = UUID.randomUUID().toString(),
                type = SmsType.OUTGOING_PACKAGE_REQUEST,
                packageCode = pkg.code,
                timestamp = System.currentTimeMillis() - 1000,
                messageBody = outgoingText,
                recipientOrSender = SmsNotificationHelper.GATEWAY_NUMBER,
                status = "Delivered"
            )
            repository.addSmsLog(outgoingLog)

            val incomingLog = SmsLog(
                id = UUID.randomUUID().toString(),
                type = SmsType.INCOMING_ACTIVATION_CONFIRMATION,
                packageCode = pkg.code,
                timestamp = System.currentTimeMillis(),
                messageBody = confirmText,
                recipientOrSender = "GlobalNet-GATEWAY",
                status = "Received & Activated"
            )
            repository.addSmsLog(incomingLog)

            // 4. Try optional direct SMS send
            if (profile.phoneNumber.isNotBlank()) {
                smsHelper.trySendSmsDirectly(profile.phoneNumber, confirmText)
            }

            // 5. Update state and display receipt
            val receipt = ActivationReceipt(
                internetPackage = pkg,
                timestamp = System.currentTimeMillis(),
                subscriberName = profile.fullName,
                confirmationText = confirmText
            )

            _uiState.update {
                it.copy(
                    activeSubscription = sub,
                    smsLogs = repository.getSmsLogs(),
                    enteredCode = "",
                    errorMessage = null,
                    activationSuccessEvent = receipt,
                    selectedTab = 1 // Switch to Live Connection tab
                )
            }
        }
    }

    fun dismissReceipt() {
        _uiState.update { it.copy(activationSuccessEvent = null) }
    }

    fun createSmsIntentForPackage(pkg: InternetPackage): Intent {
        val profile = _uiState.value.userProfile
        val log = SmsLog(
            id = UUID.randomUUID().toString(),
            type = SmsType.OUTGOING_PACKAGE_REQUEST,
            packageCode = pkg.code,
            timestamp = System.currentTimeMillis(),
            messageBody = smsHelper.generateOutgoingSmsText(pkg, profile),
            recipientOrSender = SmsNotificationHelper.GATEWAY_NUMBER,
            status = "Sent to SMS App"
        )
        repository.addSmsLog(log)
        _uiState.update { it.copy(smsLogs = repository.getSmsLogs()) }
        return smsHelper.createSmsIntent(pkg, profile)
    }

    fun toggleConnection() {
        val current = _uiState.value.activeSubscription ?: return
        val newStatus = !current.isConnected
        repository.setConnectionState(newStatus)
        _uiState.update {
            it.copy(activeSubscription = it.activeSubscription?.copy(isConnected = newStatus))
        }
    }

    fun disconnectAndReset() {
        repository.clearActiveSubscription()
        _uiState.update { it.copy(activeSubscription = null, selectedTab = 0) }
    }
}
