package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActiveSubscription
import com.example.ui.theme.GeoBorderSubtle
import com.example.ui.theme.GeoOnPrimaryContainer
import com.example.ui.theme.GeoPrimary
import com.example.ui.theme.GeoPrimaryContainer
import com.example.ui.theme.GeoSuccess
import com.example.ui.theme.GeoSurface
import com.example.ui.theme.GeoTextMuted
import com.example.ui.theme.GeoTextPrimary
import com.example.ui.theme.GeoTextSecondary

@Composable
fun HeroHeader(
    activeSubscription: ActiveSubscription?,
    subscriberName: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("hero_header_card")
    ) {
        // Geometric Balance Top Brand Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Geometric rounded-2xl brand icon box
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(GeoPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = "GlobalLink Icon",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Column {
                    Text(
                        text = "GlobalLink",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = GeoTextPrimary,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "FREE CONNECTIVITY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.6.sp,
                        color = GeoPrimary.copy(alpha = 0.85f)
                    )
                }
            }

            // Top Right Pill: FREE PLAN / ACTIVE Status
            val isConnected = activeSubscription?.isConnected == true
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(GeoPrimaryContainer)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .scale(if (isConnected) pulseScale else 1f)
                            .clip(CircleShape)
                            .background(if (isConnected) GeoSuccess else GeoOnPrimaryContainer)
                    )
                    Text(
                        text = if (isConnected) "CONNECTED" else "FREE PLAN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GeoOnPrimaryContainer,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // Active Banner Summary Card if connected or registered
        if (activeSubscription != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GeoPrimaryContainer),
                border = androidx.compose.foundation.BorderStroke(1.dp, GeoPrimary.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = null,
                            tint = GeoOnPrimaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Code ${activeSubscription.packageCode} • ${activeSubscription.totalRemainingGb.toInt()}GB Active",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = GeoOnPrimaryContainer
                        )
                    }
                    Text(
                        text = "${activeSubscription.remainingDays}d left",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GeoOnPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        } else if (subscriberName.isNotBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Welcome back, $subscriberName",
                style = MaterialTheme.typography.bodySmall,
                color = GeoTextSecondary,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

