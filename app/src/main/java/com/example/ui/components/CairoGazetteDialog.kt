package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CairoGazetteEdition
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CrimsonThread
import com.example.ui.theme.NoirDarkCard
import com.example.ui.theme.NoirSurfaceHighlight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CairoGazetteDialog(
    editions: List<CairoGazetteEdition>,
    selectedEdition: CairoGazetteEdition?,
    onSelectEdition: (CairoGazetteEdition) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentEdition = selectedEdition ?: editions.firstOrNull()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp)
                .testTag("cairo_gazette_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF3EDE2), // ورق صحف عتيق مصفر 1948
            border = BorderStroke(2.dp, Color(0xFF332B25))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Newspaper Header (Masthead)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ثمن النسخة: قرشان صاغ",
                        color = Color(0xFF4A3E37),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "جريدة وقائع القاهرة الكبرى",
                            color = Color(0xFF1E1714),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "صحيفة سياسية أسبوعية جامعة - تصدر عن دار أخبار الوادي",
                            color = Color(0xFF5A4C44),
                            fontSize = 10.sp
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_gazette_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "طي الصحيفة",
                            tint = Color(0xFF332B25)
                        )
                    }
                }

                // Date and Edition Bar
                Divider(color = Color(0xFF332B25), thickness = 1.5.dp, modifier = Modifier.padding(vertical = 4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (currentEdition != null) "العدد ${currentEdition.editionNumber}" else "العدد 1248",
                        color = Color(0xFF332B25),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = currentEdition?.dateString ?: "القاهرة - ربيع ١٩٤٨",
                        color = Color(0xFF332B25),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "طبعة الصباح الأولى",
                        color = Color(0xFF332B25),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Divider(color = Color(0xFF332B25), thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

                // Edition Selector Carousel
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(editions, key = { it.id }) { edition ->
                        val isSelected = edition.id == currentEdition?.id
                        Surface(
                            color = if (isSelected) Color(0xFF2B221C) else Color(0xFFE4D9C8),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, Color(0xFF332B25)),
                            modifier = Modifier
                                .clickable { onSelectEdition(edition) }
                                .testTag("gazette_edition_${edition.id}")
                        ) {
                            Text(
                                text = "${edition.editionNumber}: ${edition.dateString}",
                                color = if (isSelected) Color(0xFFF3EDE2) else Color(0xFF2B221C),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Newspaper Body (Scrollable Front Page)
                if (currentEdition != null) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        item {
                            // Screaming Headline
                            Text(
                                text = currentEdition.mainHeadline,
                                color = Color(0xFF900C12), // أحمر صحفي كلاسيكي
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 24.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            )

                            // Sub-headline
                            Text(
                                text = currentEdition.subHeadline,
                                color = Color(0xFF221A15),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 18.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFEAE1D2), RoundedCornerShape(4.dp))
                                    .padding(8.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Lead Story Columns
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Main Column
                                Column(modifier = Modifier.weight(0.65f)) {
                                    Text(
                                        text = currentEdition.leadStory,
                                        color = Color(0xFF1E1714),
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Detective Quote Box
                                    Surface(
                                        color = Color(0xFF2C231E),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text(
                                                text = "تصريح المحقق محمد عبده للصحافة:",
                                                color = AmberGold,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "«${currentEdition.detectiveQuote}»",
                                                color = Color(0xFFF3EDE2),
                                                fontSize = 11.sp,
                                                lineHeight = 16.sp
                                            )
                                        }
                                    }
                                }

                                // Side Column (Public Opinion & Vintage Ad)
                                Column(modifier = Modifier.weight(0.35f)) {
                                    Surface(
                                        color = Color(0xFFE5DBCB),
                                        shape = RoundedCornerShape(4.dp),
                                        border = BorderStroke(1.dp, Color(0xFF9E8E81)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text(
                                                text = "نبض الشارع القاهري",
                                                color = Color(0xFF2B221C),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = currentEdition.publicReaction,
                                                color = Color(0xFF4A3E37),
                                                fontSize = 10.sp,
                                                lineHeight = 14.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Vintage Ad snippet
                                    Surface(
                                        color = Color(0xFFDFD4C2),
                                        shape = RoundedCornerShape(4.dp),
                                        border = BorderStroke(1.dp, Color(0xFF8A7A6E)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(6.dp)) {
                                            Text(
                                                text = "إعلان تجاري",
                                                color = Color(0xFF5A4C44),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = currentEdition.vintageAdSnippet,
                                                color = Color(0xFF332B25),
                                                fontSize = 9.sp,
                                                lineHeight = 13.sp
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
