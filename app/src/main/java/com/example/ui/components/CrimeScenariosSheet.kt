package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.BoardLinkEntity
import com.example.data.model.CrimeScenario
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CrimsonGlow
import com.example.ui.theme.CrimsonThread
import com.example.ui.theme.CyanTerminal
import com.example.ui.theme.EmpathyColor
import com.example.ui.theme.NoirDarkCard
import com.example.ui.theme.NoirSurface
import com.example.ui.theme.NoirSurfaceHighlight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CrimeScenariosSheet(
    scenarios: List<CrimeScenario>,
    boardLinks: List<BoardLinkEntity>,
    selectedScenarioId: String?,
    onSelectScenarioForHighlight: (String) -> Unit,
    onOpenScenarioModal: (CrimeScenario) -> Unit,
    onDismiss: () -> Unit
) {
    val validPairs = boardLinks.filter { it.isValidConnection }.flatMap {
        listOf(it.sourceId to it.targetId, it.targetId to it.sourceId)
    }.toSet()

    val formedCount = scenarios.count { sc ->
        sc.requiredConnections.all { (a, b) -> (a to b) in validPairs || (b to a) in validPairs }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = NoirSurface),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.5.dp, AmberGold),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .widthIn(max = 640.dp)
                .testTag("crime_scenarios_sheet_dialog")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(AmberGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Insights,
                                contentDescription = null,
                                tint = AmberGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "سيناريوهات الجريمة والاستنتاج المنطقي",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "اربط خيوط الأدلة لتأكيد سيناريوهات واقعة قصر النيل",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Banner
                Surface(
                    color = NoirDarkCard,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFF383C50)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "معدل تشكيل السيناريوهات الجنائية:",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "$formedCount من أصل ${scenarios.size} مؤكدة",
                                    color = if (formedCount == scenarios.size) EmpathyColor else AmberGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { if (scenarios.isNotEmpty()) formedCount.toFloat() / scenarios.size else 0f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = if (formedCount == scenarios.size) EmpathyColor else AmberGold,
                                trackColor = NoirSurfaceHighlight
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scenarios List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .height(380.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(scenarios, key = { it.id }) { sc ->
                        val satisfiedLinksCount = sc.requiredConnections.count { (a, b) ->
                            (a to b) in validPairs || (b to a) in validPairs
                        }
                        val isFullyFormed = satisfiedLinksCount == sc.requiredConnections.size
                        val isHighlighted = selectedScenarioId == sc.id

                        Surface(
                            color = if (isHighlighted) Color(0xFF28241D) else NoirDarkCard,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                if (isHighlighted) 2.dp else 1.dp,
                                if (isHighlighted) AmberGold else if (isFullyFormed) EmpathyColor.copy(alpha = 0.5f) else Color(0xFF33384B)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(text = sc.category.iconEmoji, fontSize = 18.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = sc.title,
                                                color = if (isHighlighted) AmberGold else TextPrimary,
                                                fontSize = 12.5.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = sc.category.label,
                                                color = TextMuted,
                                                fontSize = 9.5.sp
                                            )
                                        }
                                    }

                                    Surface(
                                        color = if (isFullyFormed) EmpathyColor.copy(alpha = 0.2f) else CrimsonThread.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(6.dp),
                                        border = BorderStroke(
                                            1.dp,
                                            if (isFullyFormed) EmpathyColor else CrimsonThread
                                        )
                                    ) {
                                        Text(
                                            text = if (isFullyFormed) "✓ مكتمل ومؤكد" else "🧵 $satisfiedLinksCount / ${sc.requiredConnections.size} خيوط",
                                            color = if (isFullyFormed) EmpathyColor else CrimsonGlow,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = sc.thesis,
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 16.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { onSelectScenarioForHighlight(sc.id) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isHighlighted) AmberGold else NoirSurfaceHighlight
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.5f)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = if (isHighlighted) Color.Black else AmberGold,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isHighlighted) "إلغاء الإبراز" else "إبراز الخيوط 🧵",
                                            color = if (isHighlighted) Color.Black else AmberGold,
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            onOpenScenarioModal(sc.copy(isFormed = isFullyFormed))
                                            onDismiss()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonThread),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1.2f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Timeline,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "إعادة بناء الجريمة 🔍",
                                            color = Color.White,
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
