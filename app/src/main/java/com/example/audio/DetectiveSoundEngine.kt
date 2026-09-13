package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * المحرك الصوتي التناظري لأجواء القاهرة 1948 (Detective Sound & Noir Audio Engine).
 * يقوم بتوليد مؤثرات صوتية حقيقية وتناظرية (PCM Synthesizer) دون الحاجة لملفات ضخمة خارجية:
 * - نقرات الآلة الكاتبة الحقيقية للتقارير
 * - رنين كشف الأدلة والقرائن
 * - ختم المستندات الجنائية
 * - تشويش وضبط راديو التنصت التناظري
 * - جاز نوار ليلي مع رذاذ المطر وطنين الأجواء الغامضة
 */
object DetectiveSoundEngine {

    private const val TAG = "DetectiveSoundEngine"
    private const val SAMPLE_RATE = 22050
    private val random = Random()
    private val audioScope = CoroutineScope(Dispatchers.Default)

    private var ambienceJob: Job? = null
    private var isAmbiencePlaying = false

    var soundEffectsEnabled: Boolean = true
    var atmosphericMusicEnabled: Boolean = true

    /**
     * تشغيل نقرة آلة كاتبة ميكانيكية كلاسيكية من طراز 1948
     */
    fun playTypewriterClack() {
        if (!soundEffectsEnabled) return
        audioScope.launch {
            try {
                val durationMs = 45
                val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)

                val clickFreq = 1100.0 + random.nextInt(300)

                for (i in 0 until numSamples) {
                    val time = i.toDouble() / SAMPLE_RATE
                    val decay = exp(-time * 120.0) // اضمحلال سريع جداً
                    val tone = sin(2 * PI * clickFreq * time)
                    val noise = (random.nextDouble() * 2.0 - 1.0) * 0.4
                    val sample = ((tone * 0.6 + noise) * decay * Short.MAX_VALUE * 0.7).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playBuffer(buffer)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to play typewriter clack", e)
            }
        }
    }

    /**
     * رنين اكتشاف خيط سري أو ربط قرينة على لوحة التحقيق
     */
    fun playClueDiscovered() {
        if (!soundEffectsEnabled) return
        audioScope.launch {
            try {
                val durationMs = 420
                val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)

                val f1 = 523.25 // نغمة C5
                val f2 = 783.99 // نغمة G5
                val f3 = 1046.50 // نغمة C6

                for (i in 0 until numSamples) {
                    val time = i.toDouble() / SAMPLE_RATE
                    val decay = exp(-time * 7.5)
                    val tone = (sin(2 * PI * f1 * time) * 0.5 +
                            sin(2 * PI * f2 * time) * 0.35 +
                            sin(2 * PI * f3 * time) * 0.15)
                    val sample = (tone * decay * Short.MAX_VALUE * 0.75).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playBuffer(buffer)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to play clue discovered sound", e)
            }
        }
    }

    /**
     * ختم ملف جنائي أو إغلاق قضية (صوت ضربة ختم خشبي ثقيل)
     */
    fun playStampImpact() {
        if (!soundEffectsEnabled) return
        audioScope.launch {
            try {
                val durationMs = 180
                val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val time = i.toDouble() / SAMPLE_RATE
                    val decay = exp(-time * 28.0)
                    // انزلاق ترددي سريع للأسفل يعطي إحساس الصدمة الخشبية
                    val freq = 160.0 * (1.0 - time * 3.5).coerceAtLeast(0.2)
                    val tone = sin(2 * PI * freq * time)
                    val woodNoise = (random.nextDouble() * 2.0 - 1.0) * 0.2
                    val sample = ((tone * 0.8 + woodNoise) * decay * Short.MAX_VALUE * 0.9).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playBuffer(buffer)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to play stamp impact", e)
            }
        }
    }

    /**
     * صوت تشويش موجات الراديو التناظري لجهاز التنصت (Radio Static & Frequency Hiss)
     */
    fun playRadioStatic(durationMs: Int = 220, intensity: Float = 0.6f) {
        if (!soundEffectsEnabled) return
        audioScope.launch {
            try {
                val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)

                var filter = 0.0
                for (i in 0 until numSamples) {
                    val rawWhiteNoise = random.nextDouble() * 2.0 - 1.0
                    // Low-pass filter لمحاكاة صوت راديو كلاسيكي قديم دافئ
                    filter += (rawWhiteNoise - filter) * 0.35
                    val crackle = if (random.nextFloat() > 0.985f) (random.nextDouble() * 1.5 - 0.75) else 0.0
                    val sample = ((filter * 0.7 + crackle) * intensity * Short.MAX_VALUE * 0.6).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playBuffer(buffer)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to play radio static", e)
            }
        }
    }

    /**
     * رنين فك شفرة برقية مورس أو تسجيل سري (Cipher Decoded)
     */
    fun playCipherDecoded() {
        if (!soundEffectsEnabled) return
        audioScope.launch {
            try {
                val durationMs = 320
                val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)

                val half = numSamples / 2
                for (i in 0 until numSamples) {
                    val time = i.toDouble() / SAMPLE_RATE
                    val freq = if (i < half) 659.25 else 880.0 // E5 ثم A5
                    val decay = exp(-(time % 0.16) * 16.0)
                    val tone = sin(2 * PI * freq * time)
                    val sample = (tone * decay * Short.MAX_VALUE * 0.65).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playBuffer(buffer)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to play cipher decoded", e)
            }
        }
    }

    /**
     * تقليب أوراق الملفات الجنائية القديمة وفتح الإضبارة (Paper Rustle)
     */
    fun playPaperRustle() {
        if (!soundEffectsEnabled) return
        audioScope.launch {
            try {
                val durationMs = 150
                val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)

                var filter = 0.0
                for (i in 0 until numSamples) {
                    val time = i.toDouble() / SAMPLE_RATE
                    val decay = exp(-time * 18.0)
                    val noise = random.nextDouble() * 2.0 - 1.0
                    filter += (noise - filter) * 0.2
                    val rustle = (filter * decay * Short.MAX_VALUE * 0.45).toInt()
                    buffer[i] = rustle.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playBuffer(buffer)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to play paper rustle", e)
            }
        }
    }

    /**
     * تكتكة ساعة الجيب السويسرية وتقديم الوقت (Pocket Watch Mechanical Tick)
     */
    fun playClockTick() {
        if (!soundEffectsEnabled) return
        audioScope.launch {
            try {
                val durationMs = 80
                val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val time = i.toDouble() / SAMPLE_RATE
                    val decay = exp(-time * 45.0)
                    val tone = sin(2 * PI * 1850.0 * time)
                    val metalClick = (random.nextDouble() * 2.0 - 1.0) * 0.35
                    val sample = ((tone * 0.65 + metalClick) * decay * Short.MAX_VALUE * 0.85).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playBuffer(buffer)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to play clock tick", e)
            }
        }
    }

    /**
     * صوت التفاعل الكيميائي وتمازج الكواشف في المعمل الجنائي (Chemical Reagent Drops & Reaction)
     */
    fun playChemicalReaction() {
        if (!soundEffectsEnabled) return
        audioScope.launch {
            try {
                val durationMs = 380
                val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val time = i.toDouble() / SAMPLE_RATE
                    val decay = exp(-time * 8.0)
                    // محاكاة قطرات وفقاعات كيميائية بتعديل ترددي سريع
                    val bubbleFreq = 300.0 + sin(time * 65.0) * 180.0
                    val bubble = sin(2 * PI * bubbleFreq * time)
                    val fizz = (random.nextDouble() * 2.0 - 1.0) * 0.4
                    val sample = ((bubble * 0.6 + fizz) * decay * Short.MAX_VALUE * 0.7).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playBuffer(buffer)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to play chemical reaction", e)
            }
        }
    }

    /**
     * أزيز مصباح الأشعة فوق البنفسجية UV في غرفة الفحص المظلمة (Wood's Lamp Electric Hum)
     */
    fun playUvHum() {
        if (!soundEffectsEnabled) return
        audioScope.launch {
            try {
                val durationMs = 280
                val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val time = i.toDouble() / SAMPLE_RATE
                    val decay = exp(-time * 5.0)
                    val hum = sin(2 * PI * 120.0 * time) * 0.7 + sin(2 * PI * 240.0 * time) * 0.3
                    val sample = (hum * decay * Short.MAX_VALUE * 0.5).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playBuffer(buffer)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to play uv hum", e)
            }
        }
    }

    /**
     * صوت خربشة ريشة قلم الحبر في دفتر الملاحظات (Fountain Pen Scratch)
     */
    fun playFountainPen() {
        if (!soundEffectsEnabled) return
        audioScope.launch {
            try {
                val durationMs = 120
                val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val time = i.toDouble() / SAMPLE_RATE
                    val decay = exp(-time * 16.0)
                    val scratch = (random.nextDouble() * 2.0 - 1.0) * 0.5
                    val sample = (scratch * decay * Short.MAX_VALUE * 0.5).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playBuffer(buffer)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to play fountain pen", e)
            }
        }
    }

    /**
     * تشغيل الموسيقى النوارية والأجواء الليلية (Noir Ambience & Rain Atmosphere)
     */
    fun syncAmbienceState(enabled: Boolean) {
        atmosphericMusicEnabled = enabled
        if (enabled) {
            startNoirAtmosphere()
        } else {
            stopNoirAtmosphere()
        }
    }

    private fun startNoirAtmosphere() {
        if (isAmbiencePlaying) return
        isAmbiencePlaying = true

        ambienceJob = audioScope.launch {
            try {
                val bufferSize = SAMPLE_RATE // 1 second chunks
                val buffer = ShortArray(bufferSize)

                val minBufferSize = AudioTrack.getMinBufferSize(
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(SAMPLE_RATE)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(minBufferSize.coerceAtLeast(bufferSize * 2))
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack.play()

                var phaseDrone1 = 0.0
                var phaseDrone2 = 0.0
                var phaseSub = 0.0

                val freq1 = 110.0 // A2 تشيللو عميق
                val freq2 = 164.81 // E3 هارموني نوار
                val freqSub = 55.0 // A1 قرار مكتوم

                while (isActive && isAmbiencePlaying && atmosphericMusicEnabled) {
                    for (i in 0 until bufferSize) {
                        phaseDrone1 += 2 * PI * freq1 / SAMPLE_RATE
                        phaseDrone2 += 2 * PI * freq2 / SAMPLE_RATE
                        phaseSub += 2 * PI * freqSub / SAMPLE_RATE

                        // موجات دافئة تماثل آلات النفخ والوتريات الخافتة
                        val drone = sin(phaseDrone1) * 0.25 +
                                sin(phaseDrone2) * 0.18 +
                                sin(phaseSub) * 0.2

                        // صوت خربشة الفينيل ومطر القاهرة الناعم (Vinyl & Gentle Rain)
                        val rainHiss = (random.nextDouble() * 2.0 - 1.0) * 0.035
                        val vinylCrack = if (random.nextFloat() > 0.997f) (random.nextDouble() * 0.25 - 0.12) else 0.0

                        val combined = (drone + rainHiss + vinylCrack) * 0.35
                        val sample = (combined * Short.MAX_VALUE).toInt()
                        buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                    }

                    audioTrack.write(buffer, 0, bufferSize)
                    delay(10)
                }

                audioTrack.stop()
                audioTrack.release()
            } catch (e: Exception) {
                Log.e(TAG, "Error in noir ambience stream", e)
            } finally {
                isAmbiencePlaying = false
            }
        }
    }

    private fun stopNoirAtmosphere() {
        isAmbiencePlaying = false
        ambienceJob?.cancel()
        ambienceJob = null
    }

    /**
     * مسار مساعد لكتابة وتفريغ مصفوفة PCM عبر AudioTrack فوري
     */
    private fun playBuffer(buffer: ShortArray) {
        try {
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()

            // تحرير تلقائي بعد انتهاء التشغيل
            val durationMs = (buffer.size * 1000L) / SAMPLE_RATE
            audioScope.launch {
                delay(durationMs + 100)
                try {
                    audioTrack.stop()
                    audioTrack.release()
                } catch (_: Exception) {}
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to write buffer to AudioTrack", e)
        }
    }
}
