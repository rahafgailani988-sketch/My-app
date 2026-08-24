package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.theme.GeoBorder
import com.example.ui.theme.GeoBorderSubtle
import com.example.ui.theme.GeoError
import com.example.ui.theme.GeoOnPrimaryContainer
import com.example.ui.theme.GeoPrimary
import com.example.ui.theme.GeoPrimaryContainer
import com.example.ui.theme.GeoSuccess
import com.example.ui.theme.GeoSurface
import com.example.ui.theme.GeoSurfaceVariant
import com.example.ui.theme.GeoTextMuted
import com.example.ui.theme.GeoTextPrimary
import com.example.ui.theme.GeoTextSecondary
import com.example.ui.viewmodel.CountryOption

@Composable
fun ProfileLocationScreen(
    profile: UserProfile,
    isFetchingLocation: Boolean,
    locationError: String?,
    countryOptions: List<CountryOption>,
    onFullNameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onCountryChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onPreciseLocationChange: (String) -> Unit,
    onFetchLocation: () -> Unit,
    onSaveProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Registration Summary Card (Geometric Balance 28dp card)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_status_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = GeoSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GeoBorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (profile.isComplete) GeoPrimaryContainer else GeoSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (profile.isComplete) Icons.Default.CheckCircle else Icons.Default.Person,
                            contentDescription = null,
                            tint = if (profile.isComplete) GeoPrimary else GeoTextSecondary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (profile.fullName.isNotBlank()) profile.fullName else "Subscriber Profile",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextPrimary
                        )
                        Text(
                            text = if (profile.isComplete) "✓ Registered for Global Free Access" else "Complete profile details for free global activation",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (profile.isComplete) GeoSuccess else GeoTextSecondary
                        )
                    }
                }
            }
        }

        // Form Fields Card (Geometric Balance rounded-28px form)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_form_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = GeoSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GeoBorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = GeoPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "PERSONAL INFORMATION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.1.sp,
                            color = GeoTextSecondary
                        )
                    }

                    // 1. Full Name
                    OutlinedTextField(
                        value = profile.fullName,
                        onValueChange = onFullNameChange,
                        placeholder = { Text("Full Name (e.g. Marco Rossi)", color = GeoTextMuted, fontSize = 14.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = GeoPrimary, modifier = Modifier.size(18.dp))
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("full_name_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GeoPrimary,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = GeoTextPrimary,
                            unfocusedTextColor = GeoTextPrimary,
                            focusedContainerColor = GeoSurfaceVariant,
                            unfocusedContainerColor = GeoSurfaceVariant
                        )
                    )

                    // 2. Phone Number
                    OutlinedTextField(
                        value = profile.phoneNumber,
                        onValueChange = onPhoneNumberChange,
                        placeholder = { Text("Phone Number (e.g. +1 555-019-2834)", color = GeoTextMuted, fontSize = 14.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = GeoPrimary, modifier = Modifier.size(18.dp))
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("phone_number_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GeoPrimary,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = GeoTextPrimary,
                            unfocusedTextColor = GeoTextPrimary,
                            focusedContainerColor = GeoSurfaceVariant,
                            unfocusedContainerColor = GeoSurfaceVariant
                        )
                    )

                    // Country Quick Selection Chips
                    Column {
                        Text(
                            text = "COUNTRY OF ORIGIN",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = GeoTextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(countryOptions) { country ->
                                val isSelected = profile.country.equals(country.name, ignoreCase = true)
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { onCountryChange(country.name) }
                                        .testTag("country_chip_${country.code}"),
                                    color = if (isSelected) GeoPrimaryContainer else GeoSurfaceVariant,
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        if (isSelected) 1.5.dp else 1.dp,
                                        if (isSelected) GeoPrimary else GeoBorderSubtle
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = country.flagEmoji, fontSize = 13.sp)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = country.name,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) GeoOnPrimaryContainer else GeoTextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 3. Country Input Field & 4. City Input Field in Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = profile.country,
                            onValueChange = onCountryChange,
                            placeholder = { Text("Country", color = GeoTextMuted, fontSize = 13.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.Public, contentDescription = null, tint = GeoPrimary, modifier = Modifier.size(16.dp))
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("country_input"),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeoPrimary,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = GeoTextPrimary,
                                unfocusedTextColor = GeoTextPrimary,
                                focusedContainerColor = GeoSurfaceVariant,
                                unfocusedContainerColor = GeoSurfaceVariant
                            )
                        )

                        OutlinedTextField(
                            value = profile.city,
                            onValueChange = onCityChange,
                            placeholder = { Text("City", color = GeoTextMuted, fontSize = 13.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.LocationCity, contentDescription = null, tint = GeoPrimary, modifier = Modifier.size(16.dp))
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("city_input"),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GeoPrimary,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = GeoTextPrimary,
                                unfocusedTextColor = GeoTextPrimary,
                                focusedContainerColor = GeoSurfaceVariant,
                                unfocusedContainerColor = GeoSurfaceVariant
                            )
                        )
                    }

                    // 5. Precise Location Field + GPS auto-fetch
                    OutlinedTextField(
                        value = profile.preciseLocation,
                        onValueChange = onPreciseLocationChange,
                        placeholder = { Text("Precise Location / Coordinates / Street", color = GeoTextMuted, fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = GeoPrimary, modifier = Modifier.size(18.dp))
                        },
                        trailingIcon = {
                            if (isFetchingLocation) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = GeoPrimary,
                                    strokeWidth = 2.dp
                                )
                            }
                        },
                        singleLine = false,
                        maxLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("location_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GeoPrimary,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = GeoTextPrimary,
                            unfocusedTextColor = GeoTextPrimary,
                            focusedContainerColor = GeoSurfaceVariant,
                            unfocusedContainerColor = GeoSurfaceVariant
                        )
                    )

                    if (locationError != null) {
                        Text(
                            text = locationError,
                            color = GeoError,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // GPS Detector Button
                    OutlinedButton(
                        onClick = onFetchLocation,
                        enabled = !isFetchingLocation,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("detect_location_button"),
                        shape = RoundedCornerShape(24.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GeoPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = null,
                            tint = GeoPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isFetchingLocation) "Acquiring GPS Fix..." else "Auto-Detect GPS Location",
                            color = GeoPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Save Profile Button
                    Button(
                        onClick = onSaveProfile,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("save_profile_button"),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GeoPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Save Profile Information",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

