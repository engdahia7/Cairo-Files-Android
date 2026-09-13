package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WiretapAudioTape
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CrimsonGlow
import com.example.ui.theme.CrimsonThread
import com.example.ui.theme.CyanTerminal
import com.example.ui.theme.EmpathyColor
import com.example.ui.theme.NoirDarkCard
import com.example.ui.theme.NoirObsidian
import com.example.ui.theme.NoirSurface
import com.example.ui.theme.NoirSurfaceHighlight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun WiretapSectionCard(
    wiretaps: List<WiretapAudioTape>,
    selectedWiretap: WiretapAudioTape?,
    activeFrequency: Float,
    isDialerOpen: Boolean,
    dialedNumber: String,
    callOutput: String?,
    onSelectTape: (WiretapAudioTape?) -> Unit,
    onTuneFrequency: (Float) -> Unit,
    onToggleDialer: (Boolean) -> Unit,
    onDialDigit: (String) -> Unit,
    onClearDigit: () -> Unit,
    onMakeCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("wiretap_section_card"),
        colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(AmberGold.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Radio,
                            contentDescription = "التنصت الصوتي",
                            tint = AmberGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "جهاز التنصت والتسجيلات المسربة",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "محلل الترددات وهوائي كبائن شوارع القاهرة",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                // Phone Dialer Toggle Button
                Button(
                    onClick = { onToggleDialer(!isDialerOpen) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDialerOpen) CrimsonThread else NoirSurfaceHighlight
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("open_phone_dialer_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "هاتف المحقق",
                        tint = if (isDialerOpen) Color.White else AmberGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isDialerOpen) "إغلاق" else "الهاتف",
                        color = if (isDialerOpen) Color.White else AmberGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Retro Phone Dialer Popup / Card
            AnimatedVisibility(visible = isDialerOpen) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp)
                        .testTag("phone_dialer_container"),
                    colors = CardDefaults.cardColors(containerColor = NoirObsidian),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, CyanTerminal.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📞 هاتف مكتب محمد عبده (قرص الاتصال)",
                                color = CyanTerminal,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "أرقام هامة: 122 (النجدة) / 0213 (يوسف) / 1992 (الخزنة)",
                                color = TextMuted,
                                fontSize = 9.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Dialed Screen
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .background(Color(0xFF0F121C), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFF262D42), RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = if (dialedNumber.isEmpty()) "اطلب الرقم..." else dialedNumber,
                                color = if (dialedNumber.isEmpty()) TextMuted else AmberGold,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Keypad 3x4
                        val digits = listOf(
                            listOf("1", "2", "3"),
                            listOf("4", "5", "6"),
                            listOf("7", "8", "9"),
                            listOf("C", "0", "📞")
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            digits.forEach { row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    row.forEach { btn ->
                                        Surface(
                                            color = when (btn) {
                                                "📞" -> EmpathyColor
                                                "C" -> CrimsonThread
                                                else -> NoirSurfaceHighlight
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(40.dp)
                                                .clickable {
                                                    when (btn) {
                                                        "C" -> onClearDigit()
                                                        "📞" -> onMakeCall()
                                                        else -> onDialDigit(btn)
                                                    }
                                                }
                                                .testTag("dialer_btn_$btn")
                                        ) {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = btn,
                                                    color = when (btn) {
                                                        "📞" -> Color.Black
                                                        "C" -> Color.White
                                                        else -> TextPrimary
                                                    },
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Call Output message
                        if (callOutput != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                color = Color(0xFF1E1428),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = callOutput,
                                    color = AmberGold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(10.dp),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // Wiretaps List
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                wiretaps.forEach { tape ->
                    val isSelected = selectedWiretap?.id == tape.id
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) NoirSurfaceHighlight else NoirSurface
                        ),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) AmberGold else if (tape.isDecrypted) EmpathyColor.copy(alpha = 0.4f) else Color(0xFF2C3246)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectTape(if (isSelected) null else tape) }
                            .testTag("wiretap_item_${tape.id}")
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (tape.isDecrypted) Icons.Default.Headphones else Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = if (tape.isDecrypted) EmpathyColor else CrimsonGlow,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = tape.title,
                                            color = TextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${tape.dateLabel} • ${tape.durationLabel}",
                                            color = TextMuted,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Surface(
                                    color = if (tape.isDecrypted) EmpathyColor.copy(alpha = 0.2f) else CrimsonGlow.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (tape.isDecrypted) "فك التشفير ✓" else "مشفر (يحتاج ضبط)",
                                        color = if (tape.isDecrypted) EmpathyColor else CrimsonGlow,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "المصدر: ${tape.sourceLocation}",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "المتصل: ${tape.caller} ➔ ${tape.receiver}",
                                color = AmberGold,
                                fontSize = 11.sp
                            )

                            // Active Frequency Tuner & Transcript Panel
                            if (isSelected) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    color = NoirObsidian,
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color(0xFF33384A)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Tune,
                                                    contentDescription = null,
                                                    tint = CyanTerminal,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "معايرة التردد: ${String.format(java.util.Locale.US, "%.1f", activeFrequency)} kHz",
                                                    color = CyanTerminal,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Text(
                                                text = "الهدف التقريبي: ${String.format(java.util.Locale.US, "%.1f", tape.frequencyKhz)} kHz",
                                                color = TextMuted,
                                                fontSize = 11.sp
                                            )
                                        }

                                        Slider(
                                            value = activeFrequency,
                                            onValueChange = onTuneFrequency,
                                            valueRange = 85.0f..115.0f,
                                            colors = SliderDefaults.colors(
                                                thumbColor = AmberGold,
                                                activeTrackColor = CyanTerminal,
                                                inactiveTrackColor = Color(0xFF2A3144)
                                            ),
                                            modifier = Modifier.testTag("frequency_slider_${tape.id}")
                                        )

                                        if (tape.isDecrypted) {
                                            Text(
                                                text = "التفريغ الصوتي الكامل:",
                                                color = EmpathyColor,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = tape.fullTranscript,
                                                color = TextPrimary,
                                                fontSize = 12.sp,
                                                lineHeight = 17.sp
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Surface(
                                                color = AmberGold.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = tape.unlockedClue,
                                                    color = AmberGold,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    modifier = Modifier.padding(8.dp)
                                                )
                                            }
                                        } else {
                                            Text(
                                                text = "⚠️ إشارة مشوشة جداً: حرك المؤشر نحو التردد (${String.format(java.util.Locale.US, "%.1f", tape.frequencyKhz)} kHz) لتصفية الصوت واستخراج الاعتراف.",
                                                color = CrimsonGlow,
                                                fontSize = 11.sp
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
}
