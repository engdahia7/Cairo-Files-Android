package com.example.data.model

data class WiretapAudioTape(
    val id: String,
    val title: String,
    val dateLabel: String,
    val durationLabel: String,
    val sourceLocation: String,
    val caller: String,
    val receiver: String,
    val frequencyKhz: Float, // target frequency for tuning minigame (e.g. 104.2)
    val transcriptSnippet: String,
    val fullTranscript: String,
    val unlockedClue: String,
    val isDecrypted: Boolean = false,
    val noiseLevel: Float = 0.8f // 0f = crystal clear, 1f = heavy static
)

data class ColdCaseSideJob(
    val id: String,
    val title: String,
    val district: String,
    val informant: String,
    val rewardInsight: String,
    val narrative: String,
    val riddleQuestion: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanationOnSolve: String,
    val isCompleted: Boolean = false,
    val mirrorClueReward: String
)

enum class TimeOfDay(val label: String, val iconEmoji: String, val ambianceDesc: String) {
    NOON("ظهيرة مشمسة (12:00 م)", "☀️", "زحام خانق، أصوات أبواق السيارات، والشهود متوترون في المحلات ومواقف التاكسي."),
    DUSK("غروب غائم (06:30 م)", "🌆", "هدوء حذر على كورنيش النيل، إغلاق المحلات وتسلل الظلال في الأزقة القديمة."),
    MIDNIGHT("منتصف الليل (02:15 ص)", "🌙", "سكون بارد، أبواب مغلقة، كبائن الهواتف ترن، وظلال شبكة المرآة تتحرك بحرية.")
}

enum class WeatherCondition(val label: String, val iconEmoji: String, val effectNote: String) {
    FOGGY_RAIN("ضباب ومطر قاهري خفيف", "🌧️", "الرؤية ضئيلة في الزمالك، ومسارات الأقدام على الأرض تصبح واضحة."),
    DUSTY_WIND("رياح خماسينية محملة بالغبار", "🌪️", "صعوبة التقاط التسجيلات اللاسلكية وانعزال أحياء وسط البلد."),
    COLD_CHILL("برد قارس وسكون مطبق", "❄️", "الشوارع فارغة تماماً، أي حركة تثير الشك والريبة الفورية.")
}
