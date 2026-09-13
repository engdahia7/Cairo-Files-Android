package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.audio.DetectiveSoundEngine
import com.example.data.model.CaseEntity
import com.example.data.model.CaseStatus
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CrimsonGlow
import com.example.ui.theme.CrimsonThread
import com.example.ui.theme.CyanTerminal
import com.example.ui.theme.EmpathyColor
import com.example.ui.theme.NoirDarkCard
import com.example.ui.theme.NoirObsidian
import com.example.ui.theme.NoirSurfaceHighlight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

enum class StampSize {
    COMPACT,
    REGULAR,
    LARGE
}

data class ForensicStampDetails(
    val title: String,
    val subtitle: String,
    val jurisdiction: String,
    val inkColor: Color,
    val sealCode: String,
    val proceduralDirective: String,
    val icon: @Composable () -> Unit
)

object StampRegistry {
    fun getStampDetails(status: CaseStatus, caseId: Int? = null): ForensicStampDetails {
        val idStr = if (caseId != null) String.format("%02d", caseId) else "٤٨"
        return when (status) {
            CaseStatus.RESOLVED -> ForensicStampDetails(
                title = "مغلقة ومحسومة",
                subtitle = "أُحيلت للنيابة العامة • إدانة قاطعة",
                jurisdiction = "محكمة جنايات القاهرة المختلطة",
                inkColor = Color(0xFF22C55E), // Emerald Green Vintage Ink
                sealCode = "ج-١٩٤٨/$idStr-مُغلق",
                proceduralDirective = "تم استيفاء كافة أركان الإدانة الجنائية وربط الأدلة في لوحة التحريات وثبوت التهمة دون أدنى شك معقول.",
                icon = { Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF22C55E), modifier = Modifier.size(16.dp)) }
            )
            CaseStatus.IN_PROGRESS -> ForensicStampDetails(
                title = "قيد التحقيق الميداني",
                subtitle = "تحريات سرية جارية • عاجل وهام",
                jurisdiction = "قسم مباحث الجنايات • باب الخلق",
                inkColor = Color(0xFFF59E0B), // Burnt Amber Gold Ink
                sealCode = "ت-١٩٤٨/$idStr-جارٍ",
                proceduralDirective = "القضية مفتوحة للتحري والاستجواب الميداني. يُلزم المحقق باكتشاف القرائن وربط الفرضيات بالخيوط الحمراء لإحالتها للمحاكمة.",
                icon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp)) }
            )
            CaseStatus.UNLOCKED -> ForensicStampDetails(
                title = "مفتوحة للتكليف",
                subtitle = "أمر استجواب وتفتيش صادر",
                jurisdiction = "دفتر الأحوال الجنائية الرسمي",
                inkColor = Color(0xFF38BDF8), // Prussian Blue / Cyan Ink
                sealCode = "ب-١٩٤٨/$idStr-تكليف",
                proceduralDirective = "صدر إذن النيابة بالتحري ولم يبدأ المحقق بجمع الاستدلالات بعد. اضغط على القضية لمباشرة النزول لموقع الحادث.",
                icon = { Icon(Icons.Default.Policy, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(16.dp)) }
            )
            CaseStatus.LOCKED -> ForensicStampDetails(
                title = "سري للغاية ومحظور",
                subtitle = "محفوظ بأمر الدائرة السياسية",
                jurisdiction = "الأرشيف المغلق • ديوان عابدين",
                inkColor = Color(0xFFEF4444), // Crimson Red Wax Ink
                sealCode = "س-١٩٤٨/$idStr-محظور",
                proceduralDirective = "الملف مقيد بختم الحظر الملكي. يتطلب فك شفرات القضايا السابقة والتقدم في فصول الرواية لرفع الختم وفتح الملف.",
                icon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp)) }
            )
            CaseStatus.UNRESOLVED -> ForensicStampDetails(
                title = "قضية معلّقة وموقوفة",
                subtitle = "نقص قرائن • استئناف جنائي مطلوب",
                jurisdiction = "قلم الحفظ والعرائض الجنائية",
                inkColor = Color(0xFFF43F5E), // Rose Warning Ink
                sealCode = "ع-١٩٤٨/$idStr-معلق",
                proceduralDirective = "توقف السير في الدعوى لنقص الأدلة الدامغة أو تقديم استنتاج غير مؤكد. يتوجب إعادة معاينة مسرح الجريمة.",
                icon = { Icon(Icons.Default.ReportProblem, contentDescription = null, tint = Color(0xFFF43F5E), modifier = Modifier.size(16.dp)) }
            )
        }
    }
}

@Composable
fun CaseStatusStamp(
    status: CaseStatus,
    modifier: Modifier = Modifier,
    caseId: Int? = null,
    size: StampSize = StampSize.REGULAR,
    rotation: Float = -6.5f,
    showInteractiveModalOnTap: Boolean = true
) {
    var showDocketModal by remember { mutableStateOf(false) }
    val details = remember(status, caseId) { StampRegistry.getStampDetails(status, caseId) }

    val outerBorderWidth = when (size) {
        StampSize.COMPACT -> 1.2.dp
        StampSize.REGULAR -> 1.8.dp
        StampSize.LARGE -> 2.5.dp
    }
    val innerBorderWidth = when (size) {
        StampSize.COMPACT -> 0.6.dp
        StampSize.REGULAR -> 0.8.dp
        StampSize.LARGE -> 1.2.dp
    }
    val titleFontSize = when (size) {
        StampSize.COMPACT -> 9.sp
        StampSize.REGULAR -> 12.sp
        StampSize.LARGE -> 16.sp
    }
    val subFontSize = when (size) {
        StampSize.COMPACT -> 7.sp
        StampSize.REGULAR -> 9.sp
        StampSize.LARGE -> 11.sp
    }

    Box(
        modifier = modifier
            .rotate(rotation)
            .clickable(enabled = showInteractiveModalOnTap) {
                DetectiveSoundEngine.playStampImpact()
                showDocketModal = true
            }
            .testTag("case_status_stamp_${status.name.lowercase()}")
    ) {
        // Outer Inked Frame
        Surface(
            color = details.inkColor.copy(alpha = 0.08f),
            shape = RoundedCornerShape(if (size == StampSize.COMPACT) 4.dp else 6.dp),
            border = BorderStroke(outerBorderWidth, details.inkColor.copy(alpha = 0.85f)),
            modifier = Modifier.padding(1.dp)
        ) {
            // Inner Inked Line (Classic Double-Bordered Rubber Stamp)
            Box(
                modifier = Modifier
                    .padding(if (size == StampSize.COMPACT) 2.dp else 3.dp)
                    .border(
                        BorderStroke(innerBorderWidth, details.inkColor.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(if (size == StampSize.COMPACT) 2.dp else 4.dp)
                    )
                    .padding(
                        horizontal = when (size) {
                            StampSize.COMPACT -> 6.dp
                            StampSize.REGULAR -> 10.dp
                            StampSize.LARGE -> 16.dp
                        },
                        vertical = when (size) {
                            StampSize.COMPACT -> 2.dp
                            StampSize.REGULAR -> 4.dp
                            StampSize.LARGE -> 8.dp
                        }
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Header line (small)
                    if (size != StampSize.COMPACT) {
                        Text(
                            text = "★ ${details.jurisdiction} ★",
                            color = details.inkColor.copy(alpha = 0.75f),
                            fontSize = if (size == StampSize.LARGE) 10.sp else 8.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                    }

                    // Main Stamped Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = details.title,
                            color = details.inkColor,
                            fontSize = titleFontSize,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.8.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Subtitle / Code
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = if (size == StampSize.COMPACT) details.sealCode else "${details.sealCode} • ${details.subtitle}",
                        color = details.inkColor.copy(alpha = 0.8f),
                        fontSize = subFontSize,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    // Interactive Docket Certificate Modal
    if (showDocketModal) {
        ForensicDocketStampDialog(
            status = status,
            caseId = caseId,
            details = details,
            onDismiss = { showDocketModal = false }
        )
    }
}

@Composable
fun ForensicDocketStampDialog(
    status: CaseStatus,
    caseId: Int?,
    details: ForensicStampDetails,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("forensic_docket_stamp_dialog"),
            colors = CardDefaults.cardColors(containerColor = NoirSurfaceHighlight),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.5.dp, details.inkColor.copy(alpha = 0.7f))
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                // Header with Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = details.inkColor.copy(alpha = 0.15f),
                            shape = CircleShape,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                details.icon()
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "شهادة قيد وتصديق الختم الجنائي",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "سجلات نيابة قضايا أمن العاصمة • ١٩٤٨",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Official Large Stamp Replica in Center
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CaseStatusStamp(
                        status = status,
                        caseId = caseId,
                        size = StampSize.LARGE,
                        rotation = -5f,
                        showInteractiveModalOnTap = false
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color(0xFF333A4D), thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // Dossier Metadata Table
                DocketMetadataRow("رقم القيد الرسمي:", details.sealCode)
                DocketMetadataRow("الجهة المعتمدة:", details.jurisdiction)
                DocketMetadataRow("تاريخ الرصد والختم:", "١٩٤٨ م • قلم الجنايات الكبرى")
                DocketMetadataRow("درجة السرية:", if (status == CaseStatus.LOCKED) "سرّي للغاية ومحظور الإفشاء" else "متاح لمأمور الضبط القضائي")

                Spacer(modifier = Modifier.height(12.dp))

                // Procedural Directive Box
                Surface(
                    color = NoirDarkCard,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFF2E3447)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Gavel,
                                contentDescription = null,
                                tint = AmberGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "الحيثيات والإجراءات القضائية الواجبة:",
                                color = AmberGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = details.proceduralDirective,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Footer Notice
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ممهور ببصمة وكيل نيابة أمن الدولة • القاهرة ١٩٤٨",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DocketMetadataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = TextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ColdCaseStatusStamp(
    isCompleted: Boolean,
    isLocked: Boolean,
    modifier: Modifier = Modifier,
    size: StampSize = StampSize.COMPACT,
    rotation: Float = -5f
) {
    val inkColor = when {
        isCompleted -> Color(0xFF22C55E)
        isLocked -> Color(0xFFEF4444)
        else -> Color(0xFFF59E0B)
    }

    val primaryText = when {
        isCompleted -> "بلاغ محسوم ✓"
        isLocked -> "موقوف • تضليل"
        else -> "بلاغ جارٍ"
    }

    val subText = when {
        isCompleted -> "أُغلق الدفتر"
        isLocked -> "أمر نيابة"
        else -> "تحريات ١٩٤٨"
    }

    Box(
        modifier = modifier
            .rotate(rotation)
            .testTag("cold_case_stamp")
    ) {
        Surface(
            color = inkColor.copy(alpha = 0.08f),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.2.dp, inkColor.copy(alpha = 0.85f)),
            modifier = Modifier.padding(1.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .border(
                        BorderStroke(0.6.dp, inkColor.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(2.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = primaryText,
                        color = inkColor,
                        fontSize = if (size == StampSize.COMPACT) 9.sp else 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = subText,
                        color = inkColor.copy(alpha = 0.8f),
                        fontSize = if (size == StampSize.COMPACT) 7.sp else 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
