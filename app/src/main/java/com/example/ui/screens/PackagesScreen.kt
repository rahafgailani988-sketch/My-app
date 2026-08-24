package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InternetPackage
import com.example.ui.theme.AccentOrange
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PackagesScreen(
    packages: List<InternetPackage>,
    enteredCode: String,
    errorMessage: String?,
    onCodeEnteredChange: (String) -> Unit,
    onActivateCode: (String) -> Unit,
    onRequestViaSms: (InternetPackage) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Quick Code Activation Card (Geometric Balance 28dp section)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quick_code_activator_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = GeoSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GeoBorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(GeoPrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = GeoPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "INSTANT CODE ACTIVATOR",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.1.sp,
                                color = GeoTextSecondary
                            )
                            Text(
                                text = "Enter 4-digit code (e.g. 2222) to unlock",
                                style = MaterialTheme.typography.bodySmall,
                                color = GeoTextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = enteredCode,
                            onValueChange = onCodeEnteredChange,
                            placeholder = { Text("Code (e.g. 2222)", color = GeoTextMuted, fontSize = 14.sp) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if (enteredCode.isNotBlank()) {
                                        onActivateCode(enteredCode)
                                    }
                                }
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("package_code_input"),
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

                        Button(
                            onClick = { onActivateCode(enteredCode) },
                            enabled = enteredCode.length == 4,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GeoPrimary,
                                disabledContainerColor = GeoPrimaryContainer.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier
                                .height(52.dp)
                                .testTag("activate_code_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                tint = if (enteredCode.length == 4) Color.White else GeoTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Activate",
                                fontWeight = FontWeight.Bold,
                                color = if (enteredCode.length == 4) Color.White else GeoTextMuted
                            )
                        }
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = errorMessage,
                            color = GeoError,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick Select Code Chips
                    Text(
                        text = "QUICK CODES",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = GeoTextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        packages.forEach { pkg ->
                            val isSelected = enteredCode == pkg.code
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        onCodeEnteredChange(pkg.code)
                                    }
                                    .testTag("quick_chip_${pkg.code}"),
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
                                    Text(
                                        text = pkg.code,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isSelected) GeoOnPrimaryContainer else GeoTextPrimary,
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "(${pkg.totalDataGb}GB)",
                                        color = if (isSelected) GeoPrimary else GeoTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SELECT NETWORK PACKAGE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.1.sp,
                    color = GeoTextSecondary
                )
                Text(
                    text = "Select a code to activate",
                    fontSize = 11.sp,
                    color = GeoPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Packages List styled in Geometric Balance 20dp cards
        items(packages, key = { it.code }) { pkg ->
            val isSelected = enteredCode == pkg.code
            PackageCard(
                pkg = pkg,
                isSelected = isSelected,
                onActivate = { onActivateCode(pkg.code) },
                onSendSms = { onRequestViaSms(pkg) },
                onCopyCode = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("GlobalLink Package Code", pkg.code)
                    clipboard.setPrimaryClip(clip)
                    onCodeEnteredChange(pkg.code)
                    Toast.makeText(context, "Code ${pkg.code} selected and copied!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PackageCard(
    pkg: InternetPackage,
    isSelected: Boolean = false,
    onActivate: () -> Unit,
    onSendSms: () -> Unit,
    onCopyCode: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("package_card_${pkg.code}")
            .clickable { onCopyCode() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) GeoPrimaryContainer else GeoSurface
        ),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            if (isSelected) GeoPrimary else GeoBorder
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 0.5.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header: Code + Description + Selection indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Big Monospace Code
                    Text(
                        text = pkg.code,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = GeoPrimary,
                        modifier = Modifier.width(52.dp)
                    )

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${pkg.generalDataGb}GB + ${pkg.socialDataGb}GB Social",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = GeoTextPrimary
                            )
                            if (pkg.isPopular) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(AccentOrange.copy(alpha = 0.15f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "POPULAR",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AccentOrange
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Duration: ${pkg.validityDays} Days • Zero Cost",
                            fontSize = 11.sp,
                            color = GeoTextSecondary
                        )
                    }
                }

                // Geometric Check Indicator
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(GeoPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .border(2.dp, GeoBorder, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onActivate,
                    modifier = Modifier
                        .weight(1.3f)
                        .height(44.dp)
                        .testTag("activate_btn_${pkg.code}"),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GeoPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Activate ${pkg.code}",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }

                OutlinedButton(
                    onClick = onSendSms,
                    modifier = Modifier
                        .weight(1.1f)
                        .height(44.dp)
                        .testTag("sms_btn_${pkg.code}"),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GeoPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Sms,
                        contentDescription = "Send via SMS App",
                        tint = GeoPrimary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "SMS App",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = GeoPrimary
                    )
                }

                IconButton(
                    onClick = onCopyCode,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(GeoSurfaceVariant)
                        .testTag("copy_btn_${pkg.code}")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy code",
                        tint = GeoTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

