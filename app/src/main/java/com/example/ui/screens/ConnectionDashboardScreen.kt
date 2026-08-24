package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActiveSubscription
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.GeoBorder
import com.example.ui.theme.GeoBorderSubtle
import com.example.ui.theme.GeoError
import com.example.ui.theme.GeoOnPrimaryContainer
import com.example.ui.theme.GeoPrimary
import com.example.ui.theme.GeoPrimaryContainer
import com.example.ui.theme.GeoSecondary
import com.example.ui.theme.GeoSuccess
import com.example.ui.theme.GeoSurface
import com.example.ui.theme.GeoSurfaceVariant
import com.example.ui.theme.GeoTextMuted
import com.example.ui.theme.GeoTextPrimary
import com.example.ui.theme.GeoTextSecondary

@Composable
fun ConnectionDashboardScreen(
    activeSubscription: ActiveSubscription?,
    onToggleConnection: () -> Unit,
    onSwitchPackage: () -> Unit,
    onDisconnect: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (activeSubscription == null) {
        // Empty State: No active package
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GeoPrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WifiOff,
                    contentDescription = null,
                    tint = GeoPrimary,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "No Internet Package Active",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GeoTextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter one of the 5 free package codes (2211, 2222, 2233, 2244, 2255) to activate unlimited free worldwide data immediately.",
                style = MaterialTheme.typography.bodyMedium,
                color = GeoTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSwitchPackage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("browse_packages_empty_btn"),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GeoPrimary)
            ) {
                Text(
                    text = "View Free Packages & Activate",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    } else {
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val pulseScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse"
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Main Connection Status Hero Card (Geometric Balance 28dp card)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("live_connection_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = GeoSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GeoBorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "UNIVERSAL GATEWAY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextSecondary,
                            letterSpacing = 1.2.sp
                        )

                        // Status Chip
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (activeSubscription.isConnected) GeoPrimaryContainer else GeoSurfaceVariant)
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (activeSubscription.isConnected) GeoSuccess else GeoError)
                                )
                                Text(
                                    text = if (activeSubscription.isConnected) "ACTIVE & CONNECTED" else "PAUSED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeSubscription.isConnected) GeoOnPrimaryContainer else GeoTextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Pulse Connection Ring
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .scale(if (activeSubscription.isConnected) pulseScale else 1f)
                            .clip(CircleShape)
                            .background(if (activeSubscription.isConnected) GeoPrimaryContainer else GeoSurfaceVariant)
                            .border(
                                2.dp,
                                if (activeSubscription.isConnected) GeoPrimary else GeoBorder,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = if (activeSubscription.isConnected) Icons.Default.Wifi else Icons.Default.WifiOff,
                                contentDescription = null,
                                tint = if (activeSubscription.isConnected) GeoPrimary else GeoTextSecondary,
                                modifier = Modifier.size(34.dp)
                            )
                            Text(
                                text = "Code ${activeSubscription.packageCode}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = if (activeSubscription.isConnected) GeoOnPrimaryContainer else GeoTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = activeSubscription.packageName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = GeoTextPrimary
                    )

                    Text(
                        text = "Valid for ${activeSubscription.remainingDays} Days ${activeSubscription.remainingHours} Hours remaining",
                        style = MaterialTheme.typography.bodySmall,
                        color = GeoTextSecondary
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Toggle Connection Button
                    Button(
                        onClick = onToggleConnection,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("toggle_connection_button"),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activeSubscription.isConnected) GeoSurfaceVariant else GeoPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = null,
                            tint = if (activeSubscription.isConnected) GeoTextPrimary else Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (activeSubscription.isConnected) "Pause Tunnel Connection" else "Resume Connection",
                            color = if (activeSubscription.isConnected) GeoTextPrimary else Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Quotas Breakdown
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("data_breakdown_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = GeoSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GeoBorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "DATA QUOTA ALLOCATION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.1.sp,
                        color = GeoTextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // General Data Bar
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "General Web Data",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = GeoTextPrimary
                            )
                            Text(
                                text = "${activeSubscription.remainingGeneralGb.toInt()} GB / ${activeSubscription.initialGeneralGb.toInt()} GB",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = GeoPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { activeSubscription.generalProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = GeoPrimary,
                            trackColor = GeoPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Social Media Data Bar
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Social Media Data (WhatsApp/IG/TikTok)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = GeoTextPrimary
                            )
                            Text(
                                text = "${activeSubscription.remainingSocialGb.toInt()} GB / ${activeSubscription.initialSocialGb.toInt()} GB",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = GeoSecondary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { activeSubscription.socialProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = GeoSecondary,
                            trackColor = GeoSurfaceVariant
                        )
                    }
                }
            }

            // Live Telemetry Grid
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("telemetry_grid_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = GeoSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GeoBorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "NETWORK TELEMETRY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.1.sp,
                        color = GeoTextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = GeoSurfaceVariant,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDownward,
                                        contentDescription = null,
                                        tint = GeoPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Download", fontSize = 11.sp, color = GeoTextSecondary)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${activeSubscription.downloadSpeedMbps} Mbps",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = GeoTextPrimary
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            color = GeoSurfaceVariant,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowUpward,
                                        contentDescription = null,
                                        tint = GeoSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Upload", fontSize = 11.sp, color = GeoTextSecondary)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${activeSubscription.uploadSpeedMbps} Mbps",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = GeoTextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = GeoSurfaceVariant,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Speed,
                                        contentDescription = null,
                                        tint = AccentOrange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Latency", fontSize = 11.sp, color = GeoTextSecondary)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${activeSubscription.pingMs} ms",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = GeoTextPrimary
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            color = GeoSurfaceVariant,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = GeoPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Tunnel IP", fontSize = 11.sp, color = GeoTextSecondary)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = activeSubscription.assignedIpAddress,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = GeoTextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Options: Switch Package or Disconnect
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onSwitchPackage,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("switch_package_button"),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GeoPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = GeoPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Change Package", color = GeoPrimary, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = onDisconnect,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("disconnect_package_button"),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GeoError)
                ) {
                    Text(text = "Disconnect", color = GeoError, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

