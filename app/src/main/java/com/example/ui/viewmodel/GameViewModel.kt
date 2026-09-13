package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.datasource.InvestigativeExtraData
import com.example.data.model.BoardLinkEntity
import com.example.data.model.CaseEntity
import com.example.data.model.CaseStatus
import com.example.data.model.ColdCaseSideJob
import com.example.data.model.EndingEntity
import com.example.data.model.EncounterEntity
import com.example.data.model.EvidenceEntity
import com.example.data.model.PlayerStatsEntity
import com.example.data.model.SuspectEntity
import com.example.data.model.TimeOfDay
import com.example.data.model.WeatherCondition
import com.example.data.model.WiretapAudioTape
import com.example.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class GameTab {
    OFFICE,         // مكتب محمد عبده (مركز القيادة وشخصية المحقق)
    CASES,          // سجل الـ 50 قضية والفصول الخمسة
    CORKBOARD,      // لوحة التحقيق التفاعلية والخيوط الحمراء
    INTERROGATION,  // الاستجواب والمواجهة مع الشخصيات
    CAIRO_MAP,      // خريطة أحياء القاهرة واللقاءات الحية
    ENDINGS,        // النهايات السبعة وملف SUBJECT 01
    SETTINGS        // الإعدادات والحفظ التلقائي وإدارة ملفات التحقيق
}

data class InterrogationDialogueOption(
    val title: String,
    val textResponse: String,
    val justiceDelta: Int = 0,
    val empathyDelta: Int = 0,
    val cautionDelta: Int = 0,
    val obsessionDelta: Int = 0,
    val trustDelta: Int = 0,
    val fearDelta: Int = 0,
    val loyaltyDelta: Int = 0,
    val consequenceNote: String = ""
)

data class ActiveInterrogation(
    val suspectId: String,
    val suspectName: String,
    val currentQuestion: String,
    val options: List<InterrogationDialogueOption>,
    val lastResponse: String? = null,
    val lastImpactNotice: String? = null
)

data class GameUiState(
    val selectedTab: GameTab = GameTab.OFFICE,
    val selectedChapter: Int = 1,
    val playerStats: PlayerStatsEntity? = null,
    val allCases: List<CaseEntity> = emptyList(),
    val activeCase: CaseEntity? = null,
    val activeCaseEvidence: List<EvidenceEntity> = emptyList(),
    val allSuspects: List<SuspectEntity> = emptyList(),
    val boardLinks: List<BoardLinkEntity> = emptyList(),
    val encounters: List<EncounterEntity> = emptyList(),
    val endings: List<EndingEntity> = emptyList(),
    val activeInterrogation: ActiveInterrogation? = null,
    val bannerNotice: String? = null,
    val selectedBoardSourceId: String? = null,
    val selectedBoardTargetId: String? = null,
    val boardValidationMessage: String? = null,
    val isNightCallPlaying: Boolean = false,
    val isAutoSaveEnabled: Boolean = true,
    val soundEffectsEnabled: Boolean = true,
    val atmosphericMusicEnabled: Boolean = true,
    val vibrationHapticsEnabled: Boolean = true,
    val textSpeed: String = "عادي",
    val lastSaveTimestamp: String = "محفوظ تلقائياً",
    // Features 3 & 4 & 5: Interactive Audio wiretaps, Cairo time & weather, Cold cases
    val wiretaps: List<WiretapAudioTape> = InvestigativeExtraData.initialWiretaps,
    val selectedWiretap: WiretapAudioTape? = null,
    val activeTuningFrequency: Float = 100.0f,
    val coldCases: List<ColdCaseSideJob> = InvestigativeExtraData.initialColdCases,
    val selectedColdCase: ColdCaseSideJob? = null,
    val currentTimeOfDay: TimeOfDay = TimeOfDay.MIDNIGHT,
    val currentWeather: WeatherCondition = WeatherCondition.FOGGY_RAIN,
    val isPhoneDialerOpen: Boolean = false,
    val dialedNumber: String = "",
    val phoneCallOutput: String? = null
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GameRepository
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        val db = AppDatabase.getInstance(application)
        repository = GameRepository(db.investigationDao())

        viewModelScope.launch {
            repository.initializeGameIfEmpty()
        }

        // Collect repository flows
        viewModelScope.launch {
            repository.playerStats.collect { stats ->
                _uiState.update { it.copy(playerStats = stats) }
            }
        }

        viewModelScope.launch {
            repository.allCases.collect { cases ->
                _uiState.update { state ->
                    val active = cases.firstOrNull { it.id == (state.playerStats?.activeCaseId ?: 1) }
                        ?: cases.firstOrNull()
                    state.copy(allCases = cases, activeCase = state.activeCase ?: active)
                }
            }
        }

        viewModelScope.launch {
            repository.allSuspects.collect { suspects ->
                _uiState.update { it.copy(allSuspects = suspects) }
            }
        }

        viewModelScope.launch {
            repository.boardLinks.collect { links ->
                _uiState.update { it.copy(boardLinks = links) }
            }
        }

        viewModelScope.launch {
            repository.encounters.collect { encs ->
                _uiState.update { it.copy(encounters = encs) }
            }
        }

        viewModelScope.launch {
            repository.endings.collect { end ->
                _uiState.update { it.copy(endings = end) }
            }
        }

        // Auto load evidence for active case
        viewModelScope.launch {
            uiState.collect { state ->
                val activeCaseId = state.activeCase?.id ?: 1
                repository.getEvidenceForCase(activeCaseId).collect { evList ->
                    _uiState.update { it.copy(activeCaseEvidence = evList) }
                }
            }
        }
    }

    fun selectTab(tab: GameTab) {
        _uiState.update { it.copy(selectedTab = tab, bannerNotice = null) }
    }

    fun selectChapter(chapter: Int) {
        _uiState.update { it.copy(selectedChapter = chapter) }
    }

    fun selectCase(caseEntity: CaseEntity) {
        _uiState.update { it.copy(activeCase = caseEntity, selectedChapter = caseEntity.chapter) }
    }

    fun triggerNightCall() {
        _uiState.update {
            it.copy(
                isNightCallPlaying = true,
                bannerNotice = "📞 مكالمة مسجلة في 02:13 ص: 'أنا يوسف كامل.. لا تثق بأول شخص يخبرك أنني مت.'"
            )
        }
    }

    fun dismissNightCall() {
        _uiState.update { it.copy(isNightCallPlaying = false) }
    }

    fun discoverClue(evidenceId: String) {
        viewModelScope.launch {
            repository.discoverEvidence(evidenceId)
            _uiState.update {
                it.copy(bannerNotice = "🔍 تم فحص الدليل وإضافته إلى لوحة التحقيق!")
            }
        }
    }

    fun advanceCaseProgress(caseId: Int) {
        viewModelScope.launch {
            val currentCase = _uiState.value.allCases.find { it.id == caseId } ?: return@launch
            val newProgress = (currentCase.progressPercent + 25).coerceAtMost(100)
            val newStatus = if (newProgress >= 100) CaseStatus.RESOLVED else CaseStatus.IN_PROGRESS
            repository.advanceActiveCase(caseId, newProgress, newStatus)
            _uiState.update {
                it.copy(
                    bannerNotice = if (newProgress >= 100)
                        "✨ تم إغلاق القضية #${caseId} بنجاح! كُشف خيط جديد للمرآة."
                    else
                        "⚡ تم إحراز تقدم بنسبة $newProgress% في القضية #${caseId}"
                )
            }
        }
    }

    // Interrogation System
    fun startInterrogation(suspect: SuspectEntity) {
        val initialOptions = when (suspect.id) {
            "noura" -> listOf(
                InterrogationDialogueOption(
                    title = "سؤال بهدوء: متى تأكدتِ من غياب شقيقك؟",
                    textResponse = "الساعة كانت 11:47 بالظبط يا أستاذ محمد.. شفت الساعة واقفة والتليفون رسالته نورت بعد كدة!",
                    empathyDelta = 2,
                    trustDelta = 8,
                    consequenceNote = "التعاطف +2 | زادت ثقة نورا بك وتذكرت تفصيلة الباب السري."
                ),
                InterrogationDialogueOption(
                    title = "مواجهة حادة: هل كان شقيقك مديوناً لأحد؟",
                    textResponse = "أخويا محترم يا بيه! بس.. كان بيتكلم كتير عن شركة اسمها نايل داتا وكان مرعوب.",
                    justiceDelta = 2,
                    fearDelta = 10,
                    cautionDelta = 1,
                    consequenceNote = "العدالة +2 | ارتفع خوف نورا لكنها كشفت اسم الشركة."
                ),
                InterrogationDialogueOption(
                    title = "فحص متشكك: هل تملكين نسخة مفاتيح أخرى؟",
                    textResponse = "أيوة معايا مفتاح باب الخدمة اللي بيفتح على منور العمارة القديم!",
                    cautionDelta = 3,
                    obsessionDelta = 1,
                    trustDelta = 4,
                    consequenceNote = "الحذر +3 | كشفت مسار الهروب الخلفي السري."
                )
            )
            "witness_cleaner" -> listOf(
                InterrogationDialogueOption(
                    title = "حماية مطلقة: 'ابنك في أمان.. لن أذكر اسمك بمحضر الشرطة'",
                    textResponse = "ربنا يباركلك يا ابني.. خد الورقة دي، ده رقم التليفون اللي الراجل رماه وهو بيقاوم!",
                    empathyDelta = 3,
                    justiceDelta = -1,
                    trustDelta = 15,
                    fearDelta = -10,
                    consequenceNote = "التعاطف +3 | العدالة -1 | الشاهد أصبح حليفاً وسيعود لدعمك بالقضية 14."
                ),
                InterrogationDialogueOption(
                    title = "ضغط قانوني: 'الشهادة واجب وسأطلب من الرائد عمر استدعاءك'",
                    textResponse = "بلاش الحكومة والنبي يا بيه! عيالي هيموتوا.. هقولك وخلاص بس متجيبش سيرتي!",
                    justiceDelta = 3,
                    empathyDelta = -2,
                    fearDelta = 20,
                    trustDelta = -15,
                    consequenceNote = "العدالة +3 | التعاطف -2 | الشاهد مرعوب وقد يختفي لاحقاً."
                )
            )
            "marwan" -> listOf(
                InterrogationDialogueOption(
                    title = "مصارحة ودية: 'مروان.. ماذا كانت مهمتك في نايل داتا؟'",
                    textResponse = "يا محمد كنت شغال مبرمج حماية شبكات.. مكنتش أعرف إن البيانات بتتباع لشبكة المرآة غير لما شفت ملفك عندهم!",
                    empathyDelta = 2,
                    trustDelta = 10,
                    loyaltyDelta = 10,
                    consequenceNote = "التعاطف +2 | مروان شعر بالأمان وقدم كود فك التشفير."
                ),
                InterrogationDialogueOption(
                    title = "تحقيق صارم: 'هل سربت شيئاً من مكتبي للمرآة؟'",
                    textResponse = "أنا أخونك يا محمد؟! أنا اللي حذرتك من ملف الدور السابع لما الكل سكت!",
                    obsessionDelta = 2,
                    fearDelta = 8,
                    trustDelta = -5,
                    consequenceNote = "الهوس +2 | توترت العلاقة مع مروان لكنه قدم دليلاً مضاداً."
                )
            )
            "omar" -> listOf(
                InterrogationDialogueOption(
                    title = "احترام بروتوكول: 'رائد عمر، هل لديك صلاحية لفحص أرشيف الكاميرات؟'",
                    textResponse = "القانون فوق راسي يا محمد، بس الكاميرات دي تبع جهة متعاقدة خاصة.. مش هقدر أفيدك رسمي.",
                    justiceDelta = 2,
                    cautionDelta = 2,
                    trustDelta = 5,
                    consequenceNote = "العدالة +2 | الحذر +2 | عمر وجهك نحو الأرشيف غير الرسمي."
                ),
                InterrogationDialogueOption(
                    title = "استدراج شخصي: 'اسمك في دفتر الأسماء يا عمر.. هل يبتزونك؟'",
                    textResponse = "أنت اتجننت يا محمد؟! أنا بضحي عشان أحميك من ملفات أعلى مننا كلنا!",
                    obsessionDelta = 3,
                    fearDelta = 15,
                    loyaltyDelta = -5,
                    consequenceNote = "الهوس +3 | كشفت نقطة حساسة في ماضي الرائد عمر."
                )
            )
            else -> listOf(
                InterrogationDialogueOption(
                    title = "استفسار عن تحركات ليلة الأمس",
                    textResponse = "كل حاجة كانت هادية.. بس في عربيات سودا كانت بتلف في المنطقة بعد نص الليل.",
                    cautionDelta = 1,
                    trustDelta = 3,
                    consequenceNote = "الحذر +1 | معلومة موثقة عن الرصد الليلي."
                ),
                InterrogationDialogueOption(
                    title = "سؤال مباشر عن علاقتهم بيوسف كامل",
                    textResponse = "يوسف؟! كل الناس بتقول مات في حادثة.. بس في ناس لسه بتسمع صوته!",
                    obsessionDelta = 2,
                    fearDelta = 5,
                    consequenceNote = "الهوس +2 | تعزيز خيط بقاء يوسف على قيد الحياة."
                )
            )
        }

        _uiState.update {
            it.copy(
                selectedTab = GameTab.INTERROGATION,
                activeInterrogation = ActiveInterrogation(
                    suspectId = suspect.id,
                    suspectName = suspect.name,
                    currentQuestion = "جلسة استجواب ومواجهة: ${suspect.name} (${suspect.roleTitle})",
                    options = initialOptions,
                    lastResponse = null,
                    lastImpactNotice = null
                )
            )
        }
    }

    fun chooseDialogueOption(option: InterrogationDialogueOption) {
        val inter = _uiState.value.activeInterrogation ?: return
        viewModelScope.launch {
            repository.makeMoralChoice(
                justiceDelta = option.justiceDelta,
                empathyDelta = option.empathyDelta,
                cautionDelta = option.cautionDelta,
                obsessionDelta = option.obsessionDelta
            )
            repository.updateSuspectRelationship(
                suspectId = inter.suspectId,
                trustDelta = option.trustDelta,
                fearDelta = option.fearDelta,
                loyaltyDelta = option.loyaltyDelta
            )
            _uiState.update { state ->
                state.copy(
                    activeInterrogation = inter.copy(
                        lastResponse = option.textResponse,
                        lastImpactNotice = option.consequenceNote
                    ),
                    bannerNotice = option.consequenceNote
                )
            }
        }
    }

    // Corkboard System
    fun selectBoardNode(nodeId: String) {
        val currentSource = _uiState.value.selectedBoardSourceId
        if (currentSource == null) {
            _uiState.update { it.copy(selectedBoardSourceId = nodeId, selectedBoardTargetId = null, boardValidationMessage = null) }
        } else if (currentSource != nodeId) {
            _uiState.update { it.copy(selectedBoardTargetId = nodeId) }
            testBoardHypothesis(currentSource, nodeId)
        } else {
            // Deselect
            _uiState.update { it.copy(selectedBoardSourceId = null, selectedBoardTargetId = null) }
        }
    }

    private fun testBoardHypothesis(sourceId: String, targetId: String) {
        val validPairs = setOf(
            "mohamed" to "ev_01_clock",
            "ev_01_clock" to "noura",
            "noura" to "ev_01_code_m01",
            "ev_01_code_m01" to "youssef",
            "youssef" to "ev_02_photo",
            "ev_02_photo" to "ev_03_nile_data",
            "ev_03_nile_data" to "marwan",
            "marwan" to "ev_01_phone",
            "ev_01_code_m01" to "ev_secret_subject01"
        )

        val isValid = validPairs.contains(sourceId to targetId) || validPairs.contains(targetId to sourceId)
        val hypothesisText = when {
            isValid -> "تم تأكيد الفرضية! هذا الرابط يفتح خيطاً مباشراً في بنية المرآة."
            else -> "الفرضية غير مؤكدة: لا توجد صلة مباشرة بين هذين العنصرين في ملف القضية."
        }

        viewModelScope.launch {
            repository.connectBoardNodes(sourceId, targetId, hypothesisText, isValid)
            _uiState.update {
                it.copy(
                    boardValidationMessage = hypothesisText,
                    bannerNotice = if (isValid) "🔗 تم شد خيط أحمر جديد وتأكيد الرابط!" else "⚠️ الفرضية غير مؤكدة.. فكر بعمق أكبر",
                    selectedBoardSourceId = null,
                    selectedBoardTargetId = null
                )
            }
        }
    }

    // Cairo Map Encounters
    fun visitCairoDistrict(encounter: EncounterEntity) {
        viewModelScope.launch {
            repository.visitEncounter(encounter.id)
            _uiState.update {
                it.copy(
                    bannerNotice = "📍 التقيت بـ ${encounter.characterName} في ${encounter.district}! تم فتح خيط جديد: ${encounter.hiddenClueUnlocked}"
                )
            }
        }
    }

    fun unlockEnding(endingId: Int) {
        viewModelScope.launch {
            repository.unlockEndingById(endingId)
            val ending = _uiState.value.endings.find { it.id == endingId }
            _uiState.update {
                it.copy(
                    bannerNotice = "🏆 تم فتح ${ending?.title ?: "النهاية"} بنجاح!"
                )
            }
        }
    }

    fun dismissBanner() {
        _uiState.update { it.copy(bannerNotice = null) }
    }

    // Settings & Save Handlers
    fun manualSaveGame() {
        viewModelScope.launch {
            repository.triggerManualSave()
            val time = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
            _uiState.update {
                it.copy(
                    lastSaveTimestamp = "تم الحفظ يدوياً ($time)",
                    bannerNotice = "💾 تم حفظ ملفات التحقيق بنجاح في قاعدة البيانات المحلية!"
                )
            }
        }
    }

    fun toggleAutoSave(enabled: Boolean) {
        _uiState.update {
            it.copy(
                isAutoSaveEnabled = enabled,
                bannerNotice = if (enabled) "✅ تم تفعيل الحفظ التلقائي مع كل تقدم" else "⏸️ تم إيقاف الحفظ التلقائي (احرص على الحفظ اليدوي)"
            )
        }
    }

    fun toggleSoundEffects(enabled: Boolean) {
        _uiState.update { it.copy(soundEffectsEnabled = enabled) }
    }

    fun toggleAtmosphericMusic(enabled: Boolean) {
        _uiState.update { it.copy(atmosphericMusicEnabled = enabled) }
    }

    fun toggleVibration(enabled: Boolean) {
        _uiState.update { it.copy(vibrationHapticsEnabled = enabled) }
    }

    fun setTextSpeed(speed: String) {
        _uiState.update { it.copy(textSpeed = speed) }
    }

    // --- Interactive Audio Wiretaps & Frequency Tuner (Feature 3) ---
    fun selectWiretap(tape: WiretapAudioTape?) {
        _uiState.update {
            it.copy(
                selectedWiretap = tape,
                activeTuningFrequency = tape?.frequencyKhz?.let { f -> f - 2.5f } ?: 100.0f
            )
        }
    }

    fun tuneFrequency(newFreq: Float) {
        _uiState.update { state ->
            val tape = state.selectedWiretap
            if (tape != null) {
                val diff = kotlin.math.abs(newFreq - tape.frequencyKhz)
                val isDecrypted = diff <= 0.3f
                val updatedTapes = state.wiretaps.map {
                    if (it.id == tape.id && isDecrypted) it.copy(isDecrypted = true, noiseLevel = 0.05f) else it
                }
                val updatedSelected = if (isDecrypted) tape.copy(isDecrypted = true, noiseLevel = 0.05f) else tape
                state.copy(
                    activeTuningFrequency = newFreq,
                    selectedWiretap = updatedSelected,
                    wiretaps = updatedTapes,
                    bannerNotice = if (isDecrypted && !tape.isDecrypted) "📡 تم ضبط التردد الصوتي بنجاح! فككت تشفير التسجيل السري." else state.bannerNotice
                )
            } else {
                state.copy(activeTuningFrequency = newFreq)
            }
        }
    }

    fun togglePhoneDialer(open: Boolean) {
        _uiState.update { it.copy(isPhoneDialerOpen = open, dialedNumber = "", phoneCallOutput = null) }
    }

    fun appendDialDigit(digit: String) {
        _uiState.update {
            val current = it.dialedNumber
            if (current.length < 8) it.copy(dialedNumber = current + digit) else it
        }
    }

    fun clearDialDigit() {
        _uiState.update {
            if (it.dialedNumber.isNotEmpty()) it.copy(dialedNumber = it.dialedNumber.dropLast(1)) else it
        }
    }

    fun callDialedNumber() {
        val number = _uiState.value.dialedNumber
        val response = when (number) {
            "122" -> "شرطة النجدة: 'خدمة الطوارئ المركزية، قسم قصر النيل على الخط، أي بلاغ عاجل مسجل في أرشيفات وسط البلد؟'"
            "0213" -> "تسجيل كابينة الفجر: 'محمد عبده.. لا تثق بوفاة يوسف كامل.. اسمع التسجيلات في جهاز التنصت!'"
            "1992" -> "شفرة زلزال 92: 'تم فتح الأرشيف المالي القديم.. شبكة المرآة اشترت 14 عقاراً وسط الركام!'"
            "2024" -> "تحويلة الأرشيف السري: 'كل خيوط القضايا تقود إلى SUBJECT 01.. النهاية أقرب مما تتخيل.'"
            else -> "نغمة هاتف قديمة: 'الرقم غير متاح في شبكة سنترال العاصمة، تأكد من الرقم المطلوب أو اطلب 0213 أو 122.'"
        }
        _uiState.update { it.copy(phoneCallOutput = response) }
    }

    // --- Dynamic Time & Weather (Feature 4) ---
    fun cycleTimeOfDay() {
        val times = TimeOfDay.values()
        val nextIndex = (uiState.value.currentTimeOfDay.ordinal + 1) % times.size
        val nextTime = times[nextIndex]
        _uiState.update {
            it.copy(
                currentTimeOfDay = nextTime,
                bannerNotice = "⏰ تغير توقيت القاهرة إلى: ${nextTime.label}"
            )
        }
    }

    fun cycleWeather() {
        val weathers = WeatherCondition.values()
        val nextIndex = (uiState.value.currentWeather.ordinal + 1) % weathers.size
        val nextWeather = weathers[nextIndex]
        _uiState.update {
            it.copy(
                currentWeather = nextWeather,
                bannerNotice = "🌤️ حالة الطقس في أحياء العاصمة: ${nextWeather.label}"
            )
        }
    }

    // --- Cairo Cold Cases & Side Jobs (Feature 5) ---
    fun selectColdCase(job: ColdCaseSideJob?) {
        _uiState.update { it.copy(selectedColdCase = job) }
    }

    fun solveColdCase(caseId: String, selectedOptionIndex: Int) {
        val currentCase = _uiState.value.coldCases.find { it.id == caseId } ?: return
        val isCorrect = selectedOptionIndex == currentCase.correctOptionIndex

        if (isCorrect) {
            val updatedJobs = _uiState.value.coldCases.map {
                if (it.id == caseId) it.copy(isCompleted = true) else it
            }
            viewModelScope.launch {
                val currentStats = _uiState.value.playerStats
                if (currentStats != null) {
                    val updatedStats = currentStats.copy(
                        mirrorNetworkKnowledge = (currentStats.mirrorNetworkKnowledge + 10).coerceAtMost(100),
                        justiceScore = currentStats.justiceScore + 5,
                        secretCluesFoundCount = currentStats.secretCluesFoundCount + 1
                    )
                    repository.updateStats(updatedStats)
                }
            }
            _uiState.update {
                it.copy(
                    coldCases = updatedJobs,
                    selectedColdCase = currentCase.copy(isCompleted = true),
                    bannerNotice = "🎯 استنتاج عبقري! تم حل البلاغ الجانبي: ${currentCase.title}"
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    bannerNotice = "❌ استنتاج غير دقيق.. راجع وقائع مسرح الجريمة ونوع الأدلة المتروكة."
                )
            }
        }
    }

    fun resetEntireGame() {
        viewModelScope.launch {
            repository.resetGameProgress()
            _uiState.update {
                it.copy(
                    selectedTab = GameTab.OFFICE,
                    selectedChapter = 1,
                    bannerNotice = "🔄 تم تصفير جميع القضايا وبدء ملف تحقيق جديد بالكامل!"
                )
            }
        }
    }
}
