package com.example.ui.screens

import com.example.R

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.DetectiveSoundEngine
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CrimsonGlow
import com.example.ui.theme.CrimsonThread
import com.example.ui.theme.CyanTerminal
import com.example.ui.theme.NoirDarkCard
import com.example.ui.theme.NoirObsidian
import com.example.ui.theme.NoirSurface
import com.example.ui.theme.NoirSurfaceHighlight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onEnterGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    var typewriterProgress by remember { mutableStateOf("") }
    val fullQuote = "في أزقة القاهرة شتاء ١٩٤٨.. الحقيقة لا تظهر في النور، بل تنتظر في عتمة الأزقة القديمة."
    var progressVal by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        isVisible = true
        DetectiveSoundEngine.playTypewriterClack()

        // محاكاة كتابة النص بالآلة الكاتبة مع دقات خفيفة
        for (i in 1..fullQuote.length) {
            typewriterProgress = fullQuote.substring(0, i)
            progressVal = i.toFloat() / fullQuote.length
            if (i % 6 == 0) {
                DetectiveSoundEngine.playTypewriterClack()
            }
            delay(35)
        }

        delay(400)
        DetectiveSoundEngine.playClueDiscovered()
        // الانتقال التلقائي لمكتب التحقيق بعد اكتمال العرض دون إلزام المستخدم بالانتظار
        delay(1400)
        onEnterGame()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable {
                DetectiveSoundEngine.playStampImpact()
                onEnterGame()
            }
            .background(
                Brush.verticalGradient(
                    listOf(
                        NoirObsidian,
                        Color(0xFF141019),
                        NoirSurface
                    )
                )
            )
            .testTag("splash_screen_container")
    ) {
        // خلفية الأجواء النوارية الخافتة
        Image(
            painter = painterResource(id = R.drawable.img_cairo_night_banner),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.22f)
        )

        // زر تخطي علوي فوري للمعاينة والمحاكي
        Surface(
            color = NoirDarkCard.copy(alpha = 0.9f),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.6f)),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 16.dp)
                .clickable {
                    DetectiveSoundEngine.playStampImpact()
                    onEnterGame()
                }
                .testTag("quick_skip_intro_badge")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "دخول فوري ⚡",
                    color = AmberGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "تخطي",
                    tint = AmberGold,
                    modifier = Modifier.size(13.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(tween(900)) + slideInVertically(tween(900)) { it / 4 },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
                    .widthIn(max = 520.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // شارة التحقيق الخاصة
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    CrimsonGlow.copy(alpha = 0.35f),
                                    Color.Transparent
                                )
                            )
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_cairo_app_icon),
                        contentDescription = "أيقونة ملفات القاهرة",
                        modifier = Modifier
                            .size(86.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // بطاقة "سري للغاية"
                Surface(
                    color = CrimsonThread.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, CrimsonThread.copy(alpha = 0.7f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = CrimsonGlow,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ملف سري للغاية • ١٩٤٨",
                            color = CrimsonGlow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "ملفات القاهرة",
                    color = AmberGold,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "مكتب التحريات الجنائية الخاص • المحروسة",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(24.dp))

                // بطاقة المقتبس بالآلة الكاتبة
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = NoirDarkCard.copy(alpha = 0.85f)),
                    border = BorderStroke(1.dp, NoirSurfaceHighlight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "« $typewriterProgress »",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.Serif
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        LinearProgressIndicator(
                            progress = { progressVal },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = AmberGold,
                            trackColor = NoirSurfaceHighlight,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // زر الدخول المباشر إلى مكتب التحقيق
                Button(
                    onClick = {
                        DetectiveSoundEngine.playStampImpact()
                        onEnterGame()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmberGold,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .alpha(pulseAlpha)
                        .testTag("enter_investigation_office_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "فتح مكتب التحقيق وبدء اللعب",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "💡 انقر في أي مكان بالشاشة لتخطي المقدمة فوراً",
                    color = AmberGold.copy(alpha = 0.85f),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "الإصدار التجاري الأول جاهز للنشر والمشاركة • بدون إنترنت",
                    color = TextMuted,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
