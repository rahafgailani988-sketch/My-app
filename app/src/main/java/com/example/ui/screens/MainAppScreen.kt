package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ActivationReceiptDialog
import com.example.ui.components.HeroHeader
import com.example.ui.theme.GeoBackground
import com.example.ui.theme.GeoOnPrimaryContainer
import com.example.ui.theme.GeoPrimary
import com.example.ui.theme.GeoPrimaryContainer
import com.example.ui.theme.GeoSurface
import com.example.ui.theme.GeoTextPrimary
import com.example.ui.theme.GeoTextSecondary
import com.example.ui.viewmodel.GlobalNetViewModel

@Composable
fun MainAppScreen(
    viewModel: GlobalNetViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("main_app_scaffold"),
        containerColor = GeoBackground,
        bottomBar = {
            NavigationBar(
                containerColor = GeoSurface,
                tonalElevation = 4.dp,
                modifier = Modifier.testTag("bottom_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = uiState.selectedTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = "Packages"
                        )
                    },
                    label = { Text("Packages", fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GeoOnPrimaryContainer,
                        selectedTextColor = GeoPrimary,
                        indicatorColor = GeoPrimaryContainer,
                        unselectedIconColor = GeoTextSecondary,
                        unselectedTextColor = GeoTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_packages")
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Live Status"
                        )
                    },
                    label = { Text("Live Status", fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GeoOnPrimaryContainer,
                        selectedTextColor = GeoPrimary,
                        indicatorColor = GeoPrimaryContainer,
                        unselectedIconColor = GeoTextSecondary,
                        unselectedTextColor = GeoTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_status")
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == 2,
                    onClick = { viewModel.selectTab(2) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Subscriber"
                        )
                    },
                    label = { Text("Subscriber", fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GeoOnPrimaryContainer,
                        selectedTextColor = GeoPrimary,
                        indicatorColor = GeoPrimaryContainer,
                        unselectedIconColor = GeoTextSecondary,
                        unselectedTextColor = GeoTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_profile")
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == 3,
                    onClick = { viewModel.selectTab(3) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.MarkEmailRead,
                            contentDescription = "SMS Receipts"
                        )
                    },
                    label = { Text("SMS Receipts", fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GeoOnPrimaryContainer,
                        selectedTextColor = GeoPrimary,
                        indicatorColor = GeoPrimaryContainer,
                        unselectedIconColor = GeoTextSecondary,
                        unselectedTextColor = GeoTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_sms")
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Hero Header
            HeroHeader(
                activeSubscription = uiState.activeSubscription,
                subscriberName = uiState.userProfile.fullName
            )

            // Screen Content according to Selected Tab
            Box(modifier = Modifier.weight(1f)) {
                when (uiState.selectedTab) {
                    0 -> {
                        PackagesScreen(
                            packages = uiState.packages,
                            enteredCode = uiState.enteredCode,
                            errorMessage = uiState.errorMessage,
                            onCodeEnteredChange = viewModel::onCodeEnteredChange,
                            onActivateCode = { code ->
                                viewModel.activatePackageByCode(code)
                            },
                            onRequestViaSms = { pkg ->
                                val intent = viewModel.createSmsIntentForPackage(pkg)
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "No SMS app found. Activated code ${pkg.code} directly!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    viewModel.activatePackageByCode(pkg.code)
                                }
                            }
                        )
                    }

                    1 -> {
                        ConnectionDashboardScreen(
                            activeSubscription = uiState.activeSubscription,
                            onToggleConnection = viewModel::toggleConnection,
                            onSwitchPackage = { viewModel.selectTab(0) },
                            onDisconnect = viewModel::disconnectAndReset
                        )
                    }

                    2 -> {
                        ProfileLocationScreen(
                            profile = uiState.userProfile,
                            isFetchingLocation = uiState.isFetchingLocation,
                            locationError = uiState.locationError,
                            countryOptions = viewModel.popularCountries,
                            onFullNameChange = viewModel::onFullNameChange,
                            onPhoneNumberChange = viewModel::onPhoneNumberChange,
                            onCountryChange = viewModel::onCountryChange,
                            onCityChange = viewModel::onCityChange,
                            onPreciseLocationChange = viewModel::onPreciseLocationChange,
                            onFetchLocation = viewModel::fetchCurrentLocation,
                            onSaveProfile = {
                                viewModel.saveCurrentProfile()
                                Toast.makeText(context, "Profile Saved Successfully!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    3 -> {
                        SmsHistoryScreen(smsLogs = uiState.smsLogs)
                    }
                }
            }
        }

        // Popup Confirmation Dialog on Activation
        uiState.activationSuccessEvent?.let { receipt ->
            ActivationReceiptDialog(
                receipt = receipt,
                onDismiss = viewModel::dismissReceipt
            )
        }
    }
}

