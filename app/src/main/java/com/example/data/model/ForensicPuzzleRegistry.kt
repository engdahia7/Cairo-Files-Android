package com.example.data.model

data class CipherPuzzle(
    val title: String,
    val narrativeContext: String,
    val clueText: String,
    val encryptedCodeSnippet: String,
    val options: List<CipherOption>,
    val correctCode: String,
    val decryptionSuccessNote: String
)

data class CipherOption(
    val code: String,
    val label: String,
    val explanation: String
)

data class TriangulationClue(
    val id: String,
    val title: String,
    val description: String,
    val isCorrect: Boolean,
    val rejectionReason: String? = null
)

data class ForensicDeductionHypothesis(
    val id: Int,
    val title: String,
    val explanation: String,
    val isCorrect: Boolean,
    val feedback: String,
    val forensicConfidenceTag: String
)

data class CaseForensicPuzzle(
    val caseId: Int,
    val cipherPuzzle: CipherPuzzle,
    val triangulationClues: List<TriangulationClue>,
    val hypotheses: List<ForensicDeductionHypothesis>
)

object ForensicPuzzleRegistry {

    fun getPuzzleForCase(caseId: Int): CaseForensicPuzzle {
        return when (caseId) {
            1 -> getCase1Puzzle()
            2 -> getCase2Puzzle()
            3 -> getCase3Puzzle()
            4 -> getCase4Puzzle()
            5 -> getCase5Puzzle()
            6 -> getCase6Puzzle()
            7 -> getCase7Puzzle()
            8 -> getCase8Puzzle()
            9 -> getCase9Puzzle()
            10 -> getCase10Puzzle()
            else -> getProceduralPuzzle(caseId)
        }
    }

    private fun getCase1Puzzle(): CaseForensicPuzzle {
        return CaseForensicPuzzle(
            caseId = 1,
            cipherPuzzle = CipherPuzzle(
                title = "شفرة زنبرك ساعة الإيموبيليا",
                narrativeContext = "الشقة مغلقة من الداخل، وعقارب الساعة النحاسية تم إيقافها عمداً على توقيت محدد لنقل رسالة تحذيرية للمختفي.",
                clueText = "خلف تروس الساعة عثرت على ورقة كربونية برمز M-01، ومكتوب بالرصاص: 'توقف الساعة يحدد كود الخزانة السرية قبل موعد هروبك'. العقارب متوقفة عند الساعة 11 و47 دقيقة.",
                encryptedCodeSnippet = "[ LOCK-M : 1 1 ? 7 ]",
                options = listOf(
                    CipherOption("1147", "1147 (توقيت وقوف العقارب)", "كود الوقت الدقيق المثبت في زنبرك الساعة المشفر"),
                    CipherOption("0213", "0213 (توقيت المكالمة الهاتفية)", "توقيت اتصال يوسف كامل الليلي وليس كود الساعة"),
                    CipherOption("0732", "0732 (وقت وصول الشاكية)", "وقت حضور نورا حمدي للمكتب ولا صلة له بالشفرة"),
                    CipherOption("0934", "0934 (الفارق الزمني)", "حساب تقديري خاطئ لا يطابق تروس الساعة")
                ),
                correctCode = "1147",
                decryptionSuccessNote = "تم فك قفل زنبرك الساعة! عثرت على الميكروفيلم السري الذي يوثق رصد سيارة بيضاء تراقب الشارع."
            ),
            triangulationClues = listOf(
                TriangulationClue(
                    id = "clue_1_clock",
                    title = "عقارب الساعة المتوقفة عمداً عند 11:47",
                    description = "تثبت وجود شخص تلاعب بالتروس لتسجيل لحظة الخروج المحددة.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_1_m01",
                    title = "قصاصة كربونية مختومة برمز (M-01)",
                    description = "تثبت صلة الاختفاء بالشبكة المنظمة المسماة 'المرآة'.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_1_window",
                    title = "غبار النافذة الممسوح المطل على الشارع",
                    description = "يؤكد مراقبة الشارع والفرار عبر منور الخدمة الخلفي تجنباً للكمين.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_1_safe_money",
                    title = "أموال ومجوهرات لم تُمس في الخزنة",
                    description = "دليل سلبي ينفي السرقة الجنائية العادية لكنه لا يثبت مسار الهروب.",
                    isCorrect = false,
                    rejectionReason = "هذا الدليل يثبت فقط غياب دافع السرقة العادية، ولا يربط الجناة بالواقعة!"
                ),
                TriangulationClue(
                    id = "clue_1_incense",
                    title = "بقايا رماد بخور في مدخل الشقة",
                    description = "أثر بيتي عادي تركته والدة الشاكية قبل يومين.",
                    isCorrect = false,
                    rejectionReason = "خيط مضلل! فحص المعمل أثبت أنه بخور تجاري عادي غير مرتبط بالجريمة."
                ),
                TriangulationClue(
                    id = "clue_1_coffee",
                    title = "فنجان قهوة بارد على منضدة الصالة",
                    description = "يعود لشقيق نورا ولا يحتوي على أية مادة سامة أو مهدئة.",
                    isCorrect = false,
                    rejectionReason = "فنجان القهوة نظيف وخالٍ من أية بصمات مشبوهة أو مواد كيميائية."
                )
            ),
            hypotheses = listOf(
                ForensicDeductionHypothesis(
                    id = 1,
                    title = "الفرضية (أ): هروب تكتيكي عبر منور الخدمة بعد تلقي إنذار بشفرة M-01",
                    explanation = "المختفي تلقى تحذيراً من سيارة الرصد فتوقف عند 11:47 تاركاً شفرة الزنبرك ونفذ هروباً آمناً عبر مخرج الطوارئ بمساعدة طرف ثالث.",
                    isCorrect = true,
                    feedback = "استنتاج جنائي فذ ومحكم! ربطت بين الشفرة المادية ومسار الهروب بدقة متناهية.",
                    forensicConfidenceTag = "مطابقة جنائية كاملة 98%"
                ),
                ForensicDeductionHypothesis(
                    id = 2,
                    title = "الفرضية (ب): اختطاف قسري تم عبر نافذة الصالة المطلة على الشارع",
                    explanation = "الجناة تسلقوا العمارة واقتحموا الشقة من الشرفة ثم اختطفوه بالقوة.",
                    isCorrect = false,
                    feedback = "فحص الشبابيك أثبت إغلاق المزالج الحديدية من الداخل بالكامل، مما يستحيل معه الاقتحام الخارجي!",
                    forensicConfidenceTag = "ثغرة في مسرح الجريمة"
                ),
                ForensicDeductionHypothesis(
                    id = 3,
                    title = "الفرضية (ج): جريمة سرقة ملفات منظمة قام بها لصوص محترفون",
                    explanation = "لصوص متخصصون في الخزائن سرقوا المستندات وأجبروا المختفي على المغادرة معهم.",
                    isCorrect = false,
                    feedback = "الخزنة فُتحت بمفتاحها الأصلي دون أي أثر عنف، ولم تُمس النقود الذهبية إطلاقاً!",
                    forensicConfidenceTag = "تناقض مع الأدلة المادية"
                ),
                ForensicDeductionHypothesis(
                    id = 4,
                    title = "الفرضية (د): اختفاء طوعي بسبب خلافات عائلية وديون شخصية",
                    explanation = "المختفي دبر واقعة الاختفاء لتضليل أسرته والهروب من التزاماته.",
                    isCorrect = false,
                    feedback = "وجود شفرة منظمة 'المرآة' M-01 ومراقبة سيارة الرصد ينفي تماماً الطابع الشخصي!",
                    forensicConfidenceTag = "تفسير سطحي مردود"
                )
            )
        )
    }

    private fun getCase2Puzzle(): CaseForensicPuzzle {
        return CaseForensicPuzzle(
            caseId = 2,
            cipherPuzzle = CipherPuzzle(
                title = "شفرة شريحة كاميرا سيارة نايل داتا",
                narrativeContext = "سيارة النقل التابعة لشركة نايل داتا اصطدمت بسيارة يوسف كامل على كورنيش المقطم ثم فر السائق.",
                clueText = "فحص شريحة المراقبة التالفة أظهر ملفاً مقتطعاً برقم تتبع مشفر: 'ق هـ ر - 824'. كود فتح سجل التتبع هو رقم اللوحة الرقمي.",
                encryptedCodeSnippet = "[ PLATE-ID : 8 ? 4 0 ]",
                options = listOf(
                    CipherOption("8240", "8240 (رقم تتبع شاحنة الرصد)", "المطابق لسجل شركة نايل داتا التابع لمنظمة المرآة"),
                    CipherOption("1988", "1988 (سنة تصنيع السيارة)", "سنة التصنيع ولا علاقة لها بنظام التتبع الرقمي"),
                    CipherOption("3140", "3140 (كود مقهى الحناوي)", "كود موقع آخر لا ينتمي للشاحنة"),
                    CipherOption("0000", "0000 (الرمز المصنعي الافتراضي)", "تم تغيير الرمز المصنعي برمز مشفر خاص")
                ),
                correctCode = "8240",
                decryptionSuccessNote = "تم فك تشفير مسار الشاحنة! يظهر السجل أنها انتظر يوسف 45 دقيقة قبل الاصطدام المتعمد."
            ),
            triangulationClues = listOf(
                TriangulationClue(
                    id = "clue_2_brake",
                    title = "غياب تام لآثار فرامل على الأسفلت",
                    description = "يثبت تعمد الاصطدام المباشر دون أي محاولة للتفادي أو التوقف.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_2_nile_data",
                    title = "لوحة ترخيص نايل داتا (ق هـ ر 824)",
                    description = "تربط الحادث بشركة واجهة الاتصالات التابعة للمرآة.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_2_camera_chip",
                    title = "شريحة كاميرا محترقة جزئياً بكود 8240",
                    description = "تثبت ترصد الشاحنة للضحية قبل وقت الحادث بساعة كاملة.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_2_rain",
                    title = "مياه أمطار سطحية على جانب الطريق",
                    description = "حالة جوية اعتيادية حاول تقرير المرور الأولي استغلالها كذريعة.",
                    isCorrect = false,
                    rejectionReason = "مياه الأمطار مجرد تمويه من تقرير المرور المزور لتسجيل الحادث قضاءً وقدراً!"
                ),
                TriangulationClue(
                    id = "clue_2_broken_glass",
                    title = "زجاج مكسور من مصباح سيارة أجرة عابرة",
                    description = "حطام قديم على حافة الرصيف لا ينتمي للسيارتين.",
                    isCorrect = false,
                    rejectionReason = "فحص المعمل أثبت أن الزجاج يعود لحادث سابق قبل 3 أيام ولا صلة له بالواقعة."
                ),
                TriangulationClue(
                    id = "clue_2_cigarette",
                    title = "عقب سيجارة أجنبية قرب الكورنيش",
                    description = "تركه أحد المارة المتفرجين أثناء التجمع بعد الحادث.",
                    isCorrect = false,
                    rejectionReason = "بصمة الـ DNA لعقب السيجارة تعود لأحد أفراد الإسعاف الذين وصلوا متأخرين."
                )
            ),
            hypotheses = listOf(
                ForensicDeductionHypothesis(
                    id = 1,
                    title = "الفرضية (أ): اغتيال موجه ومدروس لإسكات يوسف وإتلاف أجهزته الميدانية",
                    explanation = "شاحنة نايل داتا كمنت للصحفي وصدمته عمداً بزاوية تستهدف حقيبة التسجيلات الصوتية ومعدات البث.",
                    isCorrect = true,
                    feedback = "تحليل جنائي بارع! الحادث كان كميناً مدبراً وليس انزلاقاً عرضياً.",
                    forensicConfidenceTag = "مطابقة أدلة الاتصالات 96%"
                ),
                ForensicDeductionHypothesis(
                    id = 2,
                    title = "الفرضية (ب): انزلاق عرضي بسبب مبلل الأسفلت والسرعة الزائدة",
                    explanation = "السائق فقد السيطرة على المنعطف الحاد بسبب الأمطار الغزيرة.",
                    isCorrect = false,
                    feedback = "غياب علامات الفرامل وبيانات التتبع المسجلة تثبت الانتظار المسبق والسرعة الثابتة!",
                    forensicConfidenceTag = "تقرير مزيف ومردود"
                ),
                ForensicDeductionHypothesis(
                    id = 3,
                    title = "الفرضية (ج): محاولة سطو مسلح فاشلة لسرقة السيارة الصحفية",
                    explanation = "عصابة قطع طرق حاولت إيقافه لسرقة السيارة الثمينة ولاذت بالفرار.",
                    isCorrect = false,
                    feedback = "أجهزة يوسف وأمواله لم تلمس، والشاحنة لم تتوقف لحظة واحدة بعد الصدمة!",
                    forensicConfidenceTag = "يتناقض مع تسجيل المسار"
                ),
                ForensicDeductionHypothesis(
                    id = 4,
                    title = "الفرضية (د): خطأ بشري من سائق شاحنة مخمور",
                    explanation = "سائق خاص يقود تحت تأثير الكحول تسبب بالحادث دون تخطيط مسبق.",
                    isCorrect = false,
                    feedback = "شريحة الرصد 8240 أثبتت اتصاله ببرج تحكم شركة نايل داتا قبل الحادث بدقائق!",
                    forensicConfidenceTag = "تضليل إعلامي مفضوح"
                )
            )
        )
    }

    private fun getCase3Puzzle(): CaseForensicPuzzle {
        return CaseForensicPuzzle(
            caseId = 3,
            cipherPuzzle = CipherPuzzle(
                title = "شفرة خزنة وزارة الاتصالات",
                narrativeContext = "سُرقت وثيقة مشروع البنية التحتية من الخزنة السويسرية في الطابق الخامس دون أي كسر فيزيائي.",
                clueText = "سجل الولوج الرقمي يظهر استخدام كود المشرف في تمام الساعة 03:14 فجراً تحت حساب الموظف 418. رمز التصريح الأمني يطابق توقيت الاختراق الرقمي.",
                encryptedCodeSnippet = "[ VAULT-KEY : 0 3 ? 4 ]",
                options = listOf(
                    CipherOption("0314", "0314 (توقيت فتح الخزنة المشفر)", "التوقيت الدقيق لاختراق الخزنة السويسرية"),
                    CipherOption("0418", "0418 (رقم الموظف المتوفى)", "الحساب المستخدم كغطاء وليس المفتاح الرقمي"),
                    CipherOption("1200", "1200 (رمز الطوارئ الافتراضي)", "تم تعطيل كود الطوارئ مسبقاً"),
                    CipherOption("9999", "9999 (رمز تجاوز الحماية)", "فشل في تجاوز التشفير الثنائي")
                ),
                correctCode = "0314",
                decryptionSuccessNote = "تم فك شيفرة سجل الولوج! نائب رئيس الشركة استعمل بصمة مصطنعة لتسريب الوثيقة."
            ),
            triangulationClues = listOf(
                TriangulationClue(
                    id = "clue_3_time_stamp",
                    title = "طابع الولوج الرقمي الساعة 03:14 فجراً",
                    description = "يوثق وقت فتح الخزنة بدقة خارج أوقات العمل الرسمية.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_3_dead_account",
                    title = "استخدام حساب موظف متوفى منذ شهرين",
                    description = "يثبت تورط شخصية إدارية عليا تملك صلاحيات إدارة الحسابات.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_3_blackmail_tape",
                    title = "شريط ابتزاز صوتي لنائب رئيس الشركة",
                    description = "يكشف الدافع الحقيقي: ابتزازه بتسجيلات سرية من شبكة المرآة.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_3_security_guard",
                    title = "شهادة حارس البوابة بأنه كان نائماً",
                    description = "إهمال وظيفي اعتيادي لا يشير إلى منفذ الاختراق الفعلي.",
                    isCorrect = false,
                    rejectionReason = "نوم الحارس سهّل الدخول لكنه لا يفسر امتلاك الجاني لمفاتيح الخزنة السويسرية المصفحة!"
                ),
                TriangulationClue(
                    id = "clue_3_cut_wires",
                    title = "سلك هاتف أرضي مقطوع في الممر",
                    description = "أثر قديم تركه عمال الصيانة أثناء ترميم السقف.",
                    isCorrect = false,
                    rejectionReason = "سلك الهاتف مقطوع منذ أسبوع ولا يؤثر على شبكة الخزنة المربوطة بالألياف الضوئية."
                ),
                TriangulationClue(
                    id = "clue_3_coffee_spill",
                    title = "بقعة شاي على لوحة المفاتيح الجدارية",
                    description = "حادث عرضي لموظف النظافة الصباحي.",
                    isCorrect = false,
                    rejectionReason = "بقعة الشاي لا تمت بصلة للبيانات الرقمية المشفرة."
                )
            ),
            hypotheses = listOf(
                ForensicDeductionHypothesis(
                    id = 1,
                    title = "الفرضية (أ): تسريب داخلي قسري نفذه نائب الرئيس تحت وطأة ابتزاز منظمة المرآة",
                    explanation = "المسؤول استغل حساب الموظف 418 في 03:14 فجراً لفتح الخزنة الرقمية ونقل الوثيقة مقابل استرجاع تسجيلاته الفاضحة.",
                    isCorrect = true,
                    feedback = "استنتاج بالغ الذكاء والمصداقية! فككت آلية الاختراق الداخلي والدوافع الخفية.",
                    forensicConfidenceTag = "مطابقة بروتوكول التشفير 97%"
                ),
                ForensicDeductionHypothesis(
                    id = 2,
                    title = "الفرضية (ب): قرصنة سيبرانية خارجية من قراصنة إنترنت هواة",
                    explanation = "هجوم فدية إلكتروني تم عبر ثغرة في جدار حماية الوزارة من خارج البلاد.",
                    isCorrect = false,
                    feedback = "الخزنة السويسرية غير متصلة بالإنترنت الخارجي وتتطلب مفتاحاً مادياً وبصمة محلية!",
                    forensicConfidenceTag = "مستحيل تقنياً وفق الفحص"
                ),
                ForensicDeductionHypothesis(
                    id = 3,
                    title = "الفرضية (ج): عملية سطو فيزيائي قامت بها عصابة مسلحة",
                    explanation = "مجموعة ملثمة اقتحمت المبنى وقامت بقص الفولاذ باستخدام أجهزة الليزر الحراري.",
                    isCorrect = false,
                    feedback = "لا يوجد أي خدش أو احتراق على بدن الخزنة الفولاذي، الأقفال فُتحت بشكل نظامي!",
                    forensicConfidenceTag = "يناقض المعاينة الجنائية"
                ),
                ForensicDeductionHypothesis(
                    id = 4,
                    title = "الفرضية (د): فقدان الوثيقة بسبب خطأ أرشفة إداري روتيني",
                    explanation = "أحد الموظفين نقل الملف إلى مستودع دار المحفوظات بالخطأ أثناء الجرد السنوي.",
                    isCorrect = false,
                    feedback = "استخدام كود التوقيت 03:14 وحساب الموظف المتوفى ينفي تماماً أسطورة الخطأ الإداري!",
                    forensicConfidenceTag = "محاولة تستر فاشلة"
                )
            )
        )
    }

    private fun getCase4Puzzle(): CaseForensicPuzzle {
        return CaseForensicPuzzle(
            caseId = 4,
            cipherPuzzle = CipherPuzzle(
                title = "شفرة إحداثيات مستودع المنيب",
                narrativeContext = "رجل الأعمال المختطف تم نقله في شاحنة مغلقة إلى مستودعات المنيب الجنوبية المهجورة.",
                clueText = "عم سيد سائق التاكسي شاهد الشاحنة تدخل القطاع رقم 7 بجوار صوامع الغلال. إيصال الوقود المضبوط يحمل كود القطاع المشفر: SEC-07.",
                encryptedCodeSnippet = "[ SECTOR-ID : S E C - 0 ? ]",
                options = listOf(
                    CipherOption("SEC-07", "SEC-07 (قطاع مستودعات الصوامع)", "القطاع المعزول جنوب المنيب حيث يحتجز الضحية"),
                    CipherOption("SEC-01", "SEC-01 (مبنى إدارة الميناء النهري)", "مبنى إداري مأهول ولا يمكن احتجاز رهينة فيه"),
                    CipherOption("HUB-44", "HUB-44 (محطة الركاب المركزية)", "موقع عام مزدحم لا يتطابق مع إيصال الشاحنة"),
                    CipherOption("DOC-12", "DOC-12 (أرصفة صيانة السفن)", "أرصفة مفتوحة كلياً بدون هناجر مغلقة")
                ),
                correctCode = "SEC-07",
                decryptionSuccessNote = "تم تأكيد الموقع بدقة! الهنجر رقم 7 في المنيب يحتوي على الحراسة والضحية."
            ),
            triangulationClues = listOf(
                TriangulationClue(
                    id = "clue_4_witness",
                    title = "شهادة عم سيد وتحديد مسار الشاحنة",
                    description = "رصد توقيت وحركة شاحنة نايل لوجستيكس نحو جنوب القاهرة بدقة.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_4_sector_receipt",
                    title = "إيصال وقود مختوم بكود SEC-07",
                    description = "يثبت تواجد شاحنة الخاطفين في القطاع رقم 7 بمستودعات المنيب.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_4_stock_papers",
                    title = "عقود التنازل الإجباري عن أسهم الاتصالات",
                    description = "تثبت دافع الخطف: الاستحواذ القسري على الشبكة لصالح شركة المرآة.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_4_ransom_note",
                    title = "رسالة فدية مزيفة كُتبت بجريدة قديمة",
                    description = "محاولة تضليل لصرف التحقيق نحو دافع مالي عادي.",
                    isCorrect = false,
                    rejectionReason = "رسالة الفدية كُتبت بعد الخطف بـ 12 ساعة لتضليل الشرطة، بينما الخاطفون طالبوا بالأسهم مباشرة!"
                ),
                TriangulationClue(
                    id = "clue_4_broken_watch",
                    title = "ساعة يد مكسورة على رصيف الزمالك",
                    description = "ساعة مقلدة تعود لأحد المارة في موقع الاختطاف.",
                    isCorrect = false,
                    rejectionReason = "رجل الأعمال كان يرتدي ساعته السويسرية الأصلية التي عُثر عليها معه في الهنجر."
                ),
                TriangulationClue(
                    id = "clue_4_tire_mud",
                    title = "طين زراعي على إطارات سيارة الضحية",
                    description = "تجمع أثناء رحلته لمزرعته بطريق مصر الإسكندرية في اليوم السابق.",
                    isCorrect = false,
                    rejectionReason = "الطين يعود لليوم السابق ولا صلة له بمسار الخطف نحو المنيب."
                )
            ),
            hypotheses = listOf(
                ForensicDeductionHypothesis(
                    id = 1,
                    title = "الفرضية (أ): احتجاز قسري في قطاع المنيب SEC-07 لإجباره على التنازل عن أسهم الشبكة",
                    explanation = "الجناة احتجزوه في الهنجر التابع لشركة نايل لوجستيكس لاستكمال التوقيع الجبري على وثائق أسهم البنية التحتية.",
                    isCorrect = true,
                    feedback = "أصبت كبد الحقيقة الجنائية! تتبعت الخيوط وحميت حياة الشاهد والضحية.",
                    forensicConfidenceTag = "مطابقة ميدانية موثقة 99%"
                ),
                ForensicDeductionHypothesis(
                    id = 2,
                    title = "الفرضية (ب): اختطاف جنائي تقليدي لطلب فدية نقدية عاجلة",
                    explanation = "عصابة خطف عادية تطلب 10 ملايين جنيه لتسديد ديون قمار خاصة.",
                    isCorrect = false,
                    feedback = "الخاطفون لم يطلبوا أموالاً من العائلة بل جهزوا عقود نقل ملكية الأسهم لشركة المرآة!",
                    forensicConfidenceTag = "يناقض المستندات المضبوطة"
                ),
                ForensicDeductionHypothesis(
                    id = 3,
                    title = "الفرضية (ج): نزاع عائلي بين الورثة لتوزيع التركة مسبقاً",
                    explanation = "أشقاء الضحية دبروا احتجازه للضغط عليه لتعديل وصيته المالية.",
                    isCorrect = false,
                    feedback = "شاحنة نايل لوجستيكس وسجلات أجهزة التنصت تثبت إدارة منظمة المرآة للعملية بالكامل!",
                    forensicConfidenceTag = "فرضية شخصية بلا أدلة"
                ),
                ForensicDeductionHypothesis(
                    id = 4,
                    title = "الفرضية (د): اختفاء مصطنع من رجل الأعمال للهروب من الضرائب",
                    explanation = "الضحية نسق العملية بنفسه لتهريب أمواله إلى حسابات أوفشور خارجية.",
                    isCorrect = false,
                    feedback = "آثار التقييد والعنف الجسدي المضبوطة في الهنجر تنفي تماماً أي تمثيلية طوعية!",
                    forensicConfidenceTag = "منافٍ لتقرير الطب الشرعي"
                )
            )
        )
    }

    private fun getCase5Puzzle(): CaseForensicPuzzle {
        return CaseForensicPuzzle(
            caseId = 5,
            cipherPuzzle = CipherPuzzle(
                title = "شفرة طابع التزييف الزمني لكاميرات المراقبة",
                narrativeContext = "المشتبه به ظهر في كاميرات أربعة ميادين مختلفة في نفس التوقيت المزعوم للجريمة.",
                clueText = "فحص مهندس الألياف الضوئية أثبت وجود فارق زمني متعمد قدره 120 ثانية (Buffer Delay) في كود بث كاميرات باب اللوق لحقن تسجيل مسبق.",
                encryptedCodeSnippet = "[ TIME-DELAY : 1 ? 0 SEC ]",
                options = listOf(
                    CipherOption("120", "120 ثانية (بافر الحقن الزمني المكتشف)", "التأخير التقني الذي سمح بحقن البث المزور"),
                    CipherOption("060", "60 ثانية (دقيقة البث الطبيعي)", "المعدل الطبيعي للبث الحي دون تلاعب"),
                    CipherOption("300", "300 ثانية (5 دقائق)", "تأخير كبير كان سيكشف التقطيع فوراً"),
                    CipherOption("000", "0 ثانية (بث متزامن)", "مستحيل تقنياً وفق تحليل الترددات المضبوطة")
                ),
                correctCode = "120",
                decryptionSuccessNote = "تم كشف التزييف الزمني! المشتبه به كان متواجداً فقط في باب اللوق وتم تزييف باقي التسجيلات."
            ),
            triangulationClues = listOf(
                TriangulationClue(
                    id = "clue_5_buffer",
                    title = "فارق التأخير الزمني 120 ثانية في الألياف الضوئية",
                    description = "الدليل الرقمي القاطع على التلاعب ببث كاميرات المراقبة بالحقن المسبق.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_5_flash_drive",
                    title = "فلاش ميموري مروان صبري المبرمج",
                    description = "يحمل كود التزييف وبرنامج تكرار البث المتزامن على الخوادم.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_5_market_witness",
                    title = "شهادة بائع سوق الفلكي في باب اللوق",
                    description = "يؤكد تواجد الجاني الحقيقي وحيداً في باب اللوق وقت الواقعة.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_5_twin_photos",
                    title = "صور أربعة أشخاص يرتدون نفس المعطف الرمادي",
                    description = "أشخاص عشوائيون التقطتهم الكاميرات في الشوارع المجاورة.",
                    isCorrect = false,
                    rejectionReason = "المعاطف الرمادية شائعة في شتاء القاهرة، وتشابه الملابس مجرد صدفة بصرية استغلها المزور!"
                ),
                TriangulationClue(
                    id = "clue_5_metro_ticket",
                    title = "تذكرة مترو مستخدمة في محطة الشهداء",
                    description = "سقطت من أحد الركاب قرب مخرج المحطة.",
                    isCorrect = false,
                    rejectionReason = "التذكرة لا تحمل بصمات المشتبه به واستُخدمت قبل الحادث بساعتين."
                ),
                TriangulationClue(
                    id = "clue_5_power_cut",
                    title = "انقطاع كهربائي مؤقت في عمود إنارة شارع شريف",
                    description = "عطل فني في كابل الكهرباء الأرضي.",
                    isCorrect = false,
                    rejectionReason = "عطل الكهرباء عام في المربع السكني ولم يؤثر على تغذية كاميرات المراقبة المستقلة."
                )
            ),
            hypotheses = listOf(
                ForensicDeductionHypothesis(
                    id = 1,
                    title = "الفرضية (أ): تزييف رقمي زمني بالحقن المسبق لإثبات حجة غياب خادعة",
                    explanation = "الجاني تواجد فقط في باب اللوق، بينما استخدم فلاش مروان صبري وبافر 120 ثانية لعرض صورته في باقي الميادين في ذات اللحظة.",
                    isCorrect = true,
                    feedback = "قمة الذكاء والبراعة التحليلية! أسقطت حجة الغياب التقنية وفككت لغز الأوجه الخمسة.",
                    forensicConfidenceTag = "إثبات رقمي قطعي 100%"
                ),
                ForensicDeductionHypothesis(
                    id = 2,
                    title = "الفرضية (ب): شبكة من 4 أفراد توائم نفذوا عملية تشتيت جماعية",
                    explanation = "أربعة إخوة توائم تقاسموا الأدوار في 4 ميادين لتشتيت فرق البحث الجنائي.",
                    isCorrect = false,
                    feedback = "فحص الحمض النووي وسجلات الأحوال المدنية يثبت أن المشتبه به وحيد بلا أشقاء!",
                    forensicConfidenceTag = "يناقض السجلات الرسمية"
                ),
                ForensicDeductionHypothesis(
                    id = 3,
                    title = "الفرضية (ج): خلل برمجي عشوائي في خوادم كاميرات المحافظة",
                    explanation = "الخوادم قامت بتكرار بث مقاطع قديمة نتيجة عطل فني عام في السيرفرات.",
                    isCorrect = false,
                    feedback = "وجود ملف الحقن المبرمج بفلاش مروان صبري ينفي الصدفة ويثبت التخطيط الجنائي المسبق!",
                    forensicConfidenceTag = "يتجاهل أداة الجريمة التقنية"
                ),
                ForensicDeductionHypothesis(
                    id = 4,
                    title = "الفرضية (د): تشابه شكلي بحت بين أشخاص مختلفين في ميادين القاهرة",
                    explanation = "الملامح متقاربة بالصدفة ونظام التعرف على الوجوه أخطأ في تقدير الهوية.",
                    isCorrect = false,
                    feedback = "فارق التوقيت الدقيق وتطابق كود الحقن الرقمي M-CAM يثبت أنها عملية اختراق متعمدة!",
                    forensicConfidenceTag = "تفسير بدائي سطحي"
                )
            )
        )
    }

    private fun getCase6Puzzle(): CaseForensicPuzzle {
        return CaseForensicPuzzle(
            caseId = 6,
            cipherPuzzle = CipherPuzzle(
                title = "شفرة أجندة مقهى الحناوي",
                narrativeContext = "زبون دائم اختفى فجأة وترك أجندة جلدية مشفرة تحت المقعد الخشبي بمقهى الحناوي.",
                clueText = "الأجندة تخص المحقق المختفي 'عادل كمال'. في الهامش كتب شفرة صندوق الأمانات السري الذي يحتفظ فيه بتسجيلات لقاء يوسف كامل: BOX-104.",
                encryptedCodeSnippet = "[ SAFE-BOX : B O X - 1 ? 4 ]",
                options = listOf(
                    CipherOption("BOX-104", "BOX-104 (صندوق أمانات محطة رمسيس)", "الرقم السري لصندوق أمانات الوثائق الاستقصائية"),
                    CipherOption("CAFE-22", "CAFE-22 (طاولة المقهى رقم 22)", "رقم الطاولة وليس كود الخزانة السرية"),
                    CipherOption("DOC-999", "DOC-999 (كود القضية المحفوظة)", "رقم أرشيفي قديم لا يفتح أي قفل"),
                    CipherOption("TEL-0213", "TEL-0213 (توقيت المكالمة)", "رقم هاتف وليس صندوق أمانات")
                ),
                correctCode = "BOX-104",
                decryptionSuccessNote = "تم تأكيد هوية الزبون وموقعه! الأجندة توثق مراقبة سيارات نايل داتا لاجتماع يوسف كامل."
            ),
            triangulationClues = listOf(
                TriangulationClue(
                    id = "clue_6_agenda",
                    title = "أجندة المحقق عادل كمال المكتوبة بالرموز",
                    description = "تثبت توثيقه لتحركات خلايا الرصد قبل اختفائه القسري بأيام.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_6_safe_code",
                    title = "كود صندوق الأمانات BOX-104 بمحطة رمسيس",
                    description = "المفتاح الرابط بين اختفاء عادل وملف يوسف كامل واللوحة الجنائية.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_6_waiter_witness",
                    title = "شهادة جرسون المقهى حول سيارة المراقبة",
                    description = "تؤكد ترصد نفس السيارة البيضاء للمقهى قبل اختفاء الزبون بدقائق.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_6_unpaid_bill",
                    title = "شيك حساب المشروبات غير المدفوع",
                    description = "مبلغ بسيط قدره 45 جنيهاً تركه الزبون على الطاولة.",
                    isCorrect = false,
                    rejectionReason = "عدم دفع الحساب كان نتيجة سحب الزبون المفاجئ وليس دافعاً للهروب!"
                ),
                TriangulationClue(
                    id = "clue_6_shisha_hose",
                    title = "مبسم شيشة بلاستيكي مستعمل",
                    description = "استخدمه زبون آخر كان يجلس على الطاولة المجاورة.",
                    isCorrect = false,
                    rejectionReason = "خيط لا صلة له بالضحية ولا يحمل بصمات عادل كمال."
                ),
                TriangulationClue(
                    id = "clue_6_newspaper",
                    title = "جريدة الأهرام الصباحية مطوية على الصفحة الرياضية",
                    description = "تركها أحد رواد المقهى بعد قراءتها.",
                    isCorrect = false,
                    rejectionReason = "الجريدة عادية ولا تحتوي على أي هوامش سرية أو شفرات."
                )
            ),
            hypotheses = listOf(
                ForensicDeductionHypothesis(
                    id = 1,
                    title = "الفرضية (أ): الزبون هو المحقق عادل كمال واختُطف بعد رصده شبكة سيارات المرآة",
                    explanation = "عادل كمال ترك الأجندة متعمداً كرسالة لك، وتوثق مراقبته لخيوط لقاء يوسف كامل وتوجهه لصندوق BOX-104.",
                    isCorrect = true,
                    feedback = "إنجاز جنائي باهر! كشفت أول خيط رابط بين زميلك المختفي ويوسف كامل.",
                    forensicConfidenceTag = "تطابق خطوط اللوحة الجنائية 98%"
                ),
                ForensicDeductionHypothesis(
                    id = 2,
                    title = "الفرضية (ب): زبون عادي هرب للتهرب من ديون المقهى المتراكمة",
                    explanation = "شخص يعاني من ضائقة مالية هرب لعدم دفع حسابه المتأخر.",
                    isCorrect = false,
                    feedback = "الأجندة تحوي رموز أمنية متقدمة وأرقام سيارات ومفتاح صندوق محطة مصر!",
                    forensicConfidenceTag = "سطحي للغاية ويتجاهل الأجندة"
                ),
                ForensicDeductionHypothesis(
                    id = 3,
                    title = "الفرضية (ج): عميل مزدوج ينقل شفرات لمنظمة تجسس أجنبية",
                    explanation = "الأجندة كانت مخصصة للاستلام من طرف مخابراتي خارجي.",
                    isCorrect = false,
                    feedback = "الوثائق تخص قضايا محلية بوسط البلد وموجهة بخط عادل إلى محمد عبده شخصياً!",
                    forensicConfidenceTag = "شطح استنتاجي بلا سند"
                ),
                ForensicDeductionHypothesis(
                    id = 4,
                    title = "الفرضية (د): مشاجرة عابرة في الشارع انتهت بالقبض عليه عشوائياً",
                    explanation = "دورية أمنية احتجزته بسبب مشادة كلامية مع أحد المارة.",
                    isCorrect = false,
                    feedback = "سجلات أقسام الشرطة خالية من اسمه، وسيارة نايل داتا هي من رصدته في الموقع!",
                    forensicConfidenceTag = "ينفيه فحص سجلات الأقسام"
                )
            )
        )
    }

    private fun getCase7Puzzle(): CaseForensicPuzzle {
        return CaseForensicPuzzle(
            caseId = 7,
            cipherPuzzle = CipherPuzzle(
                title = "شفرة جهاز التنصت في وكر باب اللوق",
                narrativeContext = "شقة مهجورة بالدور السابع في العمارة المقابلة لمكتبك تم تحويلها لمركز تجسس متقدم.",
                clueText = "أجهزة الاستقبال موجهة مباشرة لنوافذ مكتب محمد عبده. الملصق على جهاز البث اللاسلكي يحدد تردد الميغاهرتز المشفر: 104.5 MHz.",
                encryptedCodeSnippet = "[ FREQ-MHZ : 1 0 ? . 5 ]",
                options = listOf(
                    CipherOption("104.5", "104.5 MHz (تردد البث التجسسي المضبوط)", "التردد المشفر لنقل تسجيلات وميكروفونات مكتبك"),
                    CipherOption("098.2", "98.2 MHz (إذاعة القرآن الكريم)", "تردد إذاعي عام لا يحمل أي إشارة تجسسية"),
                    CipherOption("108.0", "108.0 MHz (أقصى نطاق FM)", "تردد خارج نطاق جهاز التنصت المعدل"),
                    CipherOption("091.5", "91.5 MHz (إذاعة الشباب والرياضة)", "محطة راديو تجارية اعتيادية")
                ),
                correctCode = "104.5",
                decryptionSuccessNote = "تم كشف التردد والولوج لسجل التسجيلات! المنظمة كانت تسجل حواراتك الهاتفية خطوة بخطوة."
            ),
            triangulationClues = listOf(
                TriangulationClue(
                    id = "clue_7_freq",
                    title = "جهاز تنصت مضبوط على تردد 104.5 MHz موجه لمكتبك",
                    description = "يثبت أن محمد عبده ليس مجرد محقق خارجي، بل هدف مرصود بدقة متناهية.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_7_psycho_file",
                    title = "ملف التحليل النفسي وسجل قراراتك السابقة",
                    description = "يكشف تخطيط المنظمة للتنبؤ بردود أفعالك واختياراتك الأخلاقية مسبقاً.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_7_camera_stand",
                    title = "عدسة تقريب تلسكوبية ترصد لوحة مكتبك الجنائية",
                    description = "تثبت معرفتهم المسبقة بكل خيط قمت بربطه على اللوحة.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_7_dusty_chair",
                    title = "كرسي خيزران قديم مغطى بالغبار",
                    description = "قطعة أثاث متبقية من السكان الأصليين للعمارة قبل 10 سنوات.",
                    isCorrect = false,
                    rejectionReason = "أثاث الشقة القديم لا علاقة له بالأجهزة الإلكترونية الحديثة التي تم تركيبها مؤخراً!"
                ),
                TriangulationClue(
                    id = "clue_7_pizza_box",
                    title = "علبة طعام سريع فارغة من مطعم بالتحرير",
                    description = "تاريخها يعود للأمس وتخص أحد عمال المراقبة.",
                    isCorrect = false,
                    rejectionReason = "علبة الطعام تثبت وجود شخص في المكان لكنها لا تثبت هوية الجهة ولا أهداف التجسس!"
                ),
                TriangulationClue(
                    id = "clue_7_water_bottle",
                    title = "زجاجة مياه معدنية نصف فارغة",
                    description = "عثر عليها بجوار مدخل الشرفة.",
                    isCorrect = false,
                    rejectionReason = "أثر بيولوجي عادي خالٍ من أية دلالة استخباراتية أو فنية."
                )
            ),
            hypotheses = listOf(
                ForensicDeductionHypothesis(
                    id = 1,
                    title = "الفرضية (أ): وكر تجسس متقدم مخصص لإدارة وتوجيه قرارات المحقق محمد عبده",
                    explanation = "منظمة المرآة استأجرت الشقة لرصد مكتبك وتحركاتك والتنبؤ بقراراتك عبر تردد 104.5 MHz وملفك النفسي.",
                    isCorrect = true,
                    feedback = "كشف مرعب وصادم! أدركت أنك تخوض حرباً نفسية محسوبة وأن خطواتك كانت موضوعة تحت المجهر.",
                    forensicConfidenceTag = "مطابقة رصد استخباراتي 100%"
                ),
                ForensicDeductionHypothesis(
                    id = 2,
                    title = "الفرضية (ب): مكتب لشركة تسويق وإعلانات تجري مسحاً لجمهور وسط البلد",
                    explanation = "شركة دعاية تستخدم الكاميرات لقياس كثافة المشاة في شوارع القاهرة.",
                    isCorrect = false,
                    feedback = "الكاميرات والملفات تحمل اسمك الشخصي وصورك العائلية وأسرار استقالتك من الشرطة!",
                    forensicConfidenceTag = "مرفوض تماماً ويتجاهل الملف الشخصي"
                ),
                ForensicDeductionHypothesis(
                    id = 3,
                    title = "الفرضية (ج): وكر لص تجسس على شقق السكان بهدف السطو الليلي",
                    explanation = "عصابة سرقة منازل تراقب شقق العمارة لمعرفة أوقات فراغها.",
                    isCorrect = false,
                    feedback = "الأجهزة الإلكترونية المضبوطة تفوق قيمتها ملايين الجنيهات ولا يستخدمها لصوص المنازل!",
                    forensicConfidenceTag = "مستوى التجهيز يفوق العصابات العادية"
                ),
                ForensicDeductionHypothesis(
                    id = 4,
                    title = "الفرضية (د): مراقبة رسمية من جهة أمنية تتابع تحرياتك الخاصة",
                    explanation = "إدارة التفتيش بالداخلية تراقب سلوكك المهني بعد استقالتك.",
                    isCorrect = false,
                    feedback = "الأجهزة مشفرة بأكواد M-NET ومربوطة بسيرفرات نايل داتا التابعة لمنظمة المرآة!",
                    forensicConfidenceTag = "يناقض الكود البرمجي للمعدات"
                )
            )
        )
    }

    private fun getCase8Puzzle(): CaseForensicPuzzle {
        return CaseForensicPuzzle(
            caseId = 8,
            cipherPuzzle = CipherPuzzle(
                title = "شفرة طرد أمانات محطة رمسيس",
                narrativeContext = "وصلك طرد بريدي مؤجل الإرسال لـ 90 يوماً ومختوم بشمع أحمر، مودع في محطة قطارات رمسيس.",
                clueText = "الشريط الصوتي المرفق بصوت يوسف كامل يحمل وصيته: 'إذا سمعت هذا الشريط فأنا لست بينكم.. مفتاح الحقيقة في خزانة الأمانات رقم 104'.",
                encryptedCodeSnippet = "[ RAMSES-LOCK : 1 0 ? ]",
                options = listOf(
                    CipherOption("104", "104 (رقم خزانة أمانات رمسيس)", "الرقم المنقوش على السلسلة النحاسية في الطرد"),
                    CipherOption("208", "208 (ضعف الرقم المشفر)", "حساب تضليلي لا يتطابق مع مفتاح الخزانة"),
                    CipherOption("090", "090 (أيام تأجيل الطرد)", "عدد أيام الحفظ البريدي وليس رقم الخزانة"),
                    CipherOption("777", "777 (رقم رصيف القطار)", "رقم الرصيف ولا صلة له بالخزائن الحديدية")
                ),
                correctCode = "104",
                decryptionSuccessNote = "فُتحت الخزانة رقم 104! عثرت على أصل وثائق منظمة المرآة والهيكل التنظيمي الكامل."
            ),
            triangulationClues = listOf(
                TriangulationClue(
                    id = "clue_8_safe_key",
                    title = "مفتاح خزانة أمانات رمسيس رقم 104",
                    description = "المفتاح المادي الذي تركه يوسف كصمام أمان لتسليمك الأرشيف الحقيقي.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_8_delayed_post",
                    title = "إيصال البريد المؤجل بـ 90 يوماً",
                    description = "يثبت التخطيط المسبق ليوسف تحسباً لاغتياله أو اختفائه القسري.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_8_audio_tape",
                    title = "الشريط الصوتي بنبرة يوسف كامل الحقيقية",
                    description = "فحص نبرة الصوت بالموجات الصوتية يثبت أصالة التسجيل وسلامته من التزييف.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_8_wax_seal",
                    title = "بقايا شمع أحمر مكسور من الختم",
                    description = "مادة الختم الخارجية للطرد البريدي.",
                    isCorrect = false,
                    rejectionReason = "الشمع الأحمر يثبت فقط أن الطرد لم يُفتح أثناء الشحن، لكنه لا يقود للمستندات السرية!"
                ),
                TriangulationClue(
                    id = "clue_8_train_ticket",
                    title = "تذكرة قطار إسكندرية درجة ثانية مكيفة",
                    description = "عثر عليها بجوار مكاتب الأمانات ولا صلة لها بالطرد.",
                    isCorrect = false,
                    rejectionReason = "تذكرة قطار قديمة تركها مسافر عابر على مقاعد المحطة."
                ),
                TriangulationClue(
                    id = "clue_8_postman_pen",
                    title = "قلم حبر جاف أزرق من مكتب بريد المحطة",
                    description = "استخدمه موظف الاستلام لتدوين التوقيع.",
                    isCorrect = false,
                    rejectionReason = "أداة كتابة روتينية لموظف البريد ولا تحمل أية قيمة جنائية."
                )
            ),
            hypotheses = listOf(
                ForensicDeductionHypothesis(
                    id = 1,
                    title = "الفرضية (أ): خطة أمان مسبقة من يوسف كامل لإيصال ملف المرآة 00 لمحمد عبده",
                    explanation = "يوسف أودع الوثائق في خزانة 104 وجدول إرسال الطرد المؤجل لتصلك الحقيقة حتى لو نجحت المنظمة في تصفيته.",
                    isCorrect = true,
                    feedback = "استنتاج جنائي حاسم ولا تشوبه شائبة! أثبتت وفاء يوسف وكشفت صمام أمانه الأخير.",
                    forensicConfidenceTag = "يقين قضائي تام 100%"
                ),
                ForensicDeductionHypothesis(
                    id = 2,
                    title = "الفرضية (ب): فخ واستدراج محكم نصبته المنظمة لتصفية محمد عبده بمحطة رمسيس",
                    explanation = "صوت يوسف تم تزييفه بالذكاء الاصطناعي لاستدراجك إلى كمين الخزائن.",
                    isCorrect = false,
                    feedback = "فحص التردد الصوتي والمفتاح المادي وشهادة موظف البريد تؤكد أصالة يوسف الشخصية!",
                    forensicConfidenceTag = "يناقض الفحص الصوتي الجنائي"
                ),
                ForensicDeductionHypothesis(
                    id = 3,
                    title = "الفرضية (ج): طرد أرسله عادل كمال لإخفاء مسؤوليته عن اختفاء يوسف",
                    explanation = "عادل كمال يحاول إلصاق تهمة التسريب بيوسف لتبرئة نفسه.",
                    isCorrect = false,
                    feedback = "تاريخ إيداع الطرد يعود لشهرين قبل اختفاء عادل كمال وبتوقيع يوسف الحي!",
                    forensicConfidenceTag = "تضارب تاريخ الإيداع البريدي"
                ),
                ForensicDeductionHypothesis(
                    id = 4,
                    title = "الفرضية (د): رسالة قديمة غير ذات أهمية أُرسلت بالخطأ",
                    explanation = "مجرد مسودة تحقيق صحفي قديم تم حفظه وإرساله دون قصد استراتيجي.",
                    isCorrect = false,
                    feedback = "محتوى الخزانة 104 يحتوي على أرشيف المنظمة المالي المشفر وعقود الاستحواذ السرية!",
                    forensicConfidenceTag = "يتجاهل ثقل المستندات المحرزة"
                )
            )
        )
    }

    private fun getCase9Puzzle(): CaseForensicPuzzle {
        return CaseForensicPuzzle(
            caseId = 9,
            cipherPuzzle = CipherPuzzle(
                title = "شفرة مصل السم في تقرير الطب الشرعي",
                narrativeContext = "جثة مبرمج شركة نايل داتا انتُشلت من كورنيش المعادي دون أية كدمات ظاهرة، وتضاربت أقوال الطبيب الشرعي الأولي.",
                clueText = "د. سلمى أثبتت وجود وخز إبرة مجهرية خلف الرقبة وحقن بمركب كيميائي لشل عضلات التنفس. كود المركب السمي في المعمل: TX-719.",
                encryptedCodeSnippet = "[ TOXIN-CODE : T X - 7 ? 9 ]",
                options = listOf(
                    CipherOption("TX-719", "TX-719 (مركب شلل عضلي فوري)", "المادة السامة المستخدمة في الاغتيال الصامت لمبرمج نايل داتا"),
                    CipherOption("CN-900", "CN-900 (سيانيد البوتاسيوم)", "سم كلاسيكي يترك آثاراً بصرية لم تظهر في الجثة"),
                    CipherOption("ARS-12", "ARS-12 (زرنيخ بطيء المفعول)", "يحتاج أياماً للقتل ولا يطابق لحظة التصفية الفورية"),
                    CipherOption("KCL-01", "KCL-01 (كلوريد البوتاسيوم)", "يتطلب جرعة وريدية ضخمة لم تُسجل في التحليل")
                ),
                correctCode = "TX-719",
                decryptionSuccessNote = "تم فك لغز السم! د. سلمى أكدت أن الضحية قُتل بالحقنة قبل رميه في مياه النيل لتمويه الوفاة كغرق."
            ),
            triangulationClues = listOf(
                TriangulationClue(
                    id = "clue_9_needle_mark",
                    title = "أثر وخزة إبرة مجهرية خلف الرقبة",
                    description = "يثبت الاغتيال الكيميائي الاحترافي الخاطف دون مقاومة جسدية.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_9_clean_lungs",
                    title = "خلو الرئتين تماماً من مياه النيل العكرة",
                    description = "الدليل الجنائي القاطع على أن الوفاة حدثت قبل إلقاء الجثة في النيل بساعات.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_9_nile_badge",
                    title = "بطاقة دخول مبرمج أول بشركة نايل داتا",
                    description = "تربط القتيل مباشرة بمركز خوادم منظمة المرآة والوثائق المسربة.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_9_river_mud",
                    title = "طمي نهري وطحالب على ملابس الضحية",
                    description = "عوالق طبيعية التصقت بالقماش بعد بقائه في الماء.",
                    isCorrect = false,
                    rejectionReason = "الطمي النهري عوالق سطحية لا تفسر سبب توقف عضلة القلب الحقيقي!"
                ),
                TriangulationClue(
                    id = "clue_9_wallet_cash",
                    title = "محفظة جلدية مبللة تحوي 600 جنيه وبطاقة ائتمان",
                    description = "تثبت عدم وجود دافع السرقة العادية للجثة.",
                    isCorrect = false,
                    rejectionReason = "بقاء الأموال يؤكد دافع التصفية الاستخباراتية لكنه لا يثبت نوع السم أو أداة الاغتيال!"
                ),
                TriangulationClue(
                    id = "clue_9_fishing_hook",
                    title = "خيط صنارة صيد عالق بحذاء القتيل",
                    description = "التصق به أثناء سحب الصيادين للجثة إلى الشاطئ.",
                    isCorrect = false,
                    rejectionReason = "خيط الصيد عائد للصياد الذي عثر على الجثة وأبلغ النجدة."
                )
            ),
            hypotheses = listOf(
                ForensicDeductionHypothesis(
                    id = 1,
                    title = "الفرضية (أ): اغتيال كيميائي محترف بمركب TX-719 لتصفية مبرمج نايل داتا بعد تسريبه قاعدة البيانات",
                    explanation = "المنظمة اكتشفت تسريب المبرمج للملف 00 فحقنته بالسم المشل للعضلات ثم ألقت الجثة بالنيل لإظهارها كحادث غرق عرضي.",
                    isCorrect = true,
                    feedback = "تحليل تشريحي وجنائي فائق الدقة! فضحت محاولة طمس معالم الاغتيال وكشفت توغل المرآة.",
                    forensicConfidenceTag = "إثبات سموم قطعي 99%"
                ),
                ForensicDeductionHypothesis(
                    id = 2,
                    title = "الفرضية (ب): غرق عرضي إثر انزلاق قدمه أثناء سهرة ليلية على الكورنيش",
                    explanation = "الضحية كان يتمشى على حافة الكورنيش وسقط في المياه لعدم إجادته السباحة.",
                    isCorrect = false,
                    feedback = "الرئتان سليمتان وخاليتان من ماء النيل وتقرير السموم أثبت وفاته قبل ملامسة الماء!",
                    forensicConfidenceTag = "تقرير أولي باطل ومفضوح"
                ),
                ForensicDeductionHypothesis(
                    id = 3,
                    title = "الفرضية (ج): انتحار بإلقاء نفسه في النيل بسبب ضغوط العمل التقنية",
                    explanation = "المبرمج عانى من أزمة نفسية وقرر إنهاء حياته بالقفز من أعلى كوبري المعادي.",
                    isCorrect = false,
                    feedback = "وخزة الإبرة خلف الرقبة وحقن مركب TX-719 يستحيل قيام الشخص بها بنفسه!",
                    forensicConfidenceTag = "مستحيل ميكانيكياً وتشريحياً"
                ),
                ForensicDeductionHypothesis(
                    id = 4,
                    title = "الفرضية (د): مشاجرة ليلية مع عصابة سطو انتهت بإلقائه حياً",
                    explanation = "لصوص اعتدوا عليه بدافع سرقة أجهزته وألقوه في النهر.",
                    isCorrect = false,
                    feedback = "محفظته وأمواله وبطاقة الشركة عُثر عليها كاملة دون أي كدمات ضرب أو عنف ميكانيكي!",
                    forensicConfidenceTag = "يناقض سلامة الجثة من الرضوض"
                )
            )
        )
    }

    private fun getCase10Puzzle(): CaseForensicPuzzle {
        return CaseForensicPuzzle(
            caseId = 10,
            cipherPuzzle = CipherPuzzle(
                title = "شفرة الملف الرئيسي: رأس أفعى المرآة",
                narrativeContext = "تقف أمام اللوحة الجنائية بمكتبك بعد حل القضايا التسع. تتجمع كل الخيوط لتكشف الملف السري 00 وهوية يوسف الحقيقية.",
                clueText = "الملف السري رقم 00 وبطاقة يوسف المشفرة تحمل الكود التأسيسي لمنظمة المرآة والمسمى السري لقائد الخلية في القاهرة: SUBJECT-01.",
                encryptedCodeSnippet = "[ MASTER-KEY : S U B J E C T - 0 ? ]",
                options = listOf(
                    CipherOption("SUBJECT-01", "SUBJECT-01 (الملف الرئيسي للشخصية المستهدفة)", "الكود السري الذي يربط بين يوسف كامل والمحقق محمد عبده"),
                    CipherOption("MIRROR-99", "MIRROR-99 (رمز السيرفرات الاحتياطية)", "سيرفر تضليلي تم إحراقه مسبقاً"),
                    CipherOption("CAIRO-000", "CAIRO-000 (كود أرشيف المحافظة)", "رقم عام لا يحمل صلاحيات القيادة"),
                    CipherOption("SHADOW-07", "SHADOW-07 (خلية الرصد الميداني)", "إحدى الخلايا التنفيذية التابعة وليس الرأس")
                ),
                correctCode = "SUBJECT-01",
                decryptionSuccessNote = "انكشفت الحقيقة الكبرى! يوسف كامل يقود خلية المقاومة الداخلية للمرآة، وأنت المحقق المختار لإسقاطها."
            ),
            triangulationClues = listOf(
                TriangulationClue(
                    id = "clue_10_master_file",
                    title = "ملف المرآة رقم 00 المكتوب بالرمز SUBJECT-01",
                    description = "الوثيقة التأسيسية الشاملة التي تجمع قضايا الفصول الخمسة في كيان هرمي واحد.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_10_corkboard_web",
                    title = "تطابق جميع الخيوط الحمراء على لوحة مكتبك",
                    description = "يربط بين سيارات الرصد، أرشيف نايل داتا، مكالمات الفجر، وسجلات وزارة الاتصالات.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_10_youssef_call",
                    title = "المكالمة الهاتفية الحية المشفرة في 02:13 ص",
                    description = "الدليل القاطع على بقاء يوسف كامل على قيد الحياة وتوجيهه للمعركة من الظل.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_10_fake_newspaper",
                    title = "خبر نعي يوسف كامل المنشور في صحيفة رسمية",
                    description = "الإعلان التضليلي الذي نشرته المنظمة للتغطية على اختفائه.",
                    isCorrect = false,
                    rejectionReason = "نعي الصحيفة كان أداة تمويه لتضليل الرأي العام وإقناعك بموته للتوقف عن البحث!"
                ),
                TriangulationClue(
                    id = "clue_10_broken_mirror",
                    title = "شظايا مرآة مكسورة في أحد الأزقة",
                    description = "زجاج مكسور لا علاقة له باسم المنظمة الاستعارية.",
                    isCorrect = false,
                    rejectionReason = "اسم 'المرآة' رمز تنظيمي استخباراتي وليس مرآة زجاجية فيزيائية!"
                ),
                TriangulationClue(
                    id = "clue_10_empty_cartridge",
                    title = "فارغ طلقة عيار 9 ملم من ميدان التحرير",
                    description = "أثر أمني قديم عثر عليه أحد المخبرين في الشارع العام.",
                    isCorrect = false,
                    rejectionReason = "فارغ الطلقة قديم ولا ينتمي لأي من عمليات الاغتيال الكيميائي للمنظمة."
                )
            ),
            hypotheses = listOf(
                ForensicDeductionHypothesis(
                    id = 1,
                    title = "الفرضية (أ): جميع القضايا التسع خلايا لمنظمة 'المرآة'، ويوسف يقود حرب الاختراق الداخلي بالتعاون معك",
                    explanation = "المنظمة شبكة استخباراتية مالية كبرى تسيطر على البنية الرقمية، ويوسف لم يمت بل استخدم موته كغطاء لقيادة المقاومة وتسليمك قيادة المواجهة.",
                    isCorrect = true,
                    feedback = "انتصار جنائي وتاريخي أسطوري! أنهيت الفصل الأول كأعظم محقق في تاريخ القاهرة ودخلت قلب المعركة الكبرى!",
                    forensicConfidenceTag = "الحقيقة المطلقة للملف 00: 100%"
                ),
                ForensicDeductionHypothesis(
                    id = 2,
                    title = "الفرضية (ب): مجرد جرائم صدفة متفرقة ضخمها المحقق بهواجسه وشكوكه الشخصية",
                    explanation = "لا يوجد رابط عضوي منظم بين الحوادث التسعة وكل جريمة ارتكبها جاني مستقل.",
                    isCorrect = false,
                    feedback = "تطابق أكواد M-01 ونايل داتا والألياف الضوئية والشفرة الموحدة يثبت يقيناً وجود الكيان الهرمي!",
                    forensicConfidenceTag = "إنكار للحقائق المادية الدامغة"
                ),
                ForensicDeductionHypothesis(
                    id = 3,
                    title = "الفرضية (ج): يوسف كامل هو العقل المدبر لشبكة المرآة وكان يضللك من البداية",
                    explanation = "يوسف أسس المنظمة واستدرجك لتصفية شركائه ومساعدته على الانفراد بالسلطة.",
                    isCorrect = false,
                    feedback = "الوثائق وخزانة 104 تثبت تضحية يوسف ومحاولات المنظمة المتكررة لتصفيته واغتيال مبرمجيه!",
                    forensicConfidenceTag = "فرضية خبيثة تناقض الأدلة الموثقة"
                ),
                ForensicDeductionHypothesis(
                    id = 4,
                    title = "الفرضية (د): مؤامرة أمنية تهدف لاختبار صلاحية محمد عبده للعودة للخدمة",
                    explanation = "الداخلية صممت هذه القضايا التسع كاختبار عملي لمهاراتك الاستنتاجية.",
                    isCorrect = false,
                    feedback = "دماء الضحايا الحقيقية والمصل السمي TX-719 والخزائن السويسرية لا يمكن أن تكون مجرد مناورة تدريبية!",
                    forensicConfidenceTag = "يتجاهل دماء الضحايا والجرائم الحقيقية"
                )
            )
        )
    }

    private fun getProceduralPuzzle(caseId: Int): CaseForensicPuzzle {
        val cipherCode = "CODE-$caseId"
        return CaseForensicPuzzle(
            caseId = caseId,
            cipherPuzzle = CipherPuzzle(
                title = "شفرة التحري الميداني: قضية #$caseId",
                narrativeContext = "أوراق التحريات السرية تتضمن تسلسلاً رقمياً مشفراً في هامش تقرير المعاينة.",
                clueText = "الرقم المنقوش في مسرح الجريمة يتطابق مع تسلسل الخلية $caseId التابعة للمرآة.",
                encryptedCodeSnippet = "[ CIPHER : M - 0 $caseId ]",
                options = listOf(
                    CipherOption("M-0$caseId", "M-0$caseId (كود الخلية المباشر)", "الشفرة الميدانية المعتمدة للقضية رقم $caseId"),
                    CipherOption("0000", "0000 (تصفير الأمان)", "رمز خاطئ يعيد قفل السجل"),
                    CipherOption("9999", "9999 (تجاوز محظور)", "محاولة تجاوز غير مصرح بها"),
                    CipherOption("5555", "5555 (كود وهمي)", "رقم لا صلة له بمسار التحقيق")
                ),
                correctCode = "M-0$caseId",
                decryptionSuccessNote = "تم فك قفل التحريات! الملف أصبح جاهزاً للربط الجنائي."
            ),
            triangulationClues = listOf(
                TriangulationClue(
                    id = "clue_proc_${caseId}_1",
                    title = "الدليل المادي الرئيسي لقضية #$caseId",
                    description = "أثر مادي محرز من مسرح الجريمة يربط الجاني بالضحية.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_proc_${caseId}_2",
                    title = "طابع التوقيت المتناقض في سجلات المراقبة",
                    description = "يكشف ثغرة حجة الغياب التي حاول الجاني التستر خلفها.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_proc_${caseId}_3",
                    title = "وثيقة الاتصال المشفرة مع خلية المرآة",
                    description = "تثبت تلقي المشتبه به أوامر مباشرة من تنظيم المرآة.",
                    isCorrect = true
                ),
                TriangulationClue(
                    id = "clue_proc_${caseId}_4",
                    title = "بصمة مجهولة على زجاج النافذة الخارجية",
                    description = "تعود لأحد عمال النظافة قبل وقوع الحادث بيوم.",
                    isCorrect = false,
                    rejectionReason = "فحص الأدلة الجنائية أثبت أن البصمة لا صلة لها بأطراف القضية!"
                ),
                TriangulationClue(
                    id = "clue_proc_${caseId}_5",
                    title = "ساعة يد قديمة متوقفة في موقع الحادث",
                    description = "متروكة في المكان منذ سنوات ولا تخص أياً من المشتبه بهم.",
                    isCorrect = false,
                    rejectionReason = "أثر قديم غير مرتبط بزمن وقوع الجريمة."
                ),
                TriangulationClue(
                    id = "clue_proc_${caseId}_6",
                    title = "فاتورة مشتريات عادية من متجر قريب",
                    description = "فاتورة لأغراض بقالة شخصية لا علاقة لها بالتخطيط الجنائي.",
                    isCorrect = false,
                    rejectionReason = "مشتريات غذائية روتينية لا تقدم أي خيط استدلالي."
                )
            ),
            hypotheses = listOf(
                ForensicDeductionHypothesis(
                    id = 1,
                    title = "الفرضية (أ): تورط الخلية في تنفيذ توجيهات المرآة وطمس الأدلة الرقمية",
                    explanation = "تطابق الأدلة المادية ووثائق التشفير M-0$caseId يثبت التخطيط المنظم لتنفيذ الجريمة.",
                    isCorrect = true,
                    feedback = "أحسنت! فككت خيوط القضية ببراعة واقتدار جنائي عالٍ.",
                    forensicConfidenceTag = "مطابقة جنائية كاملة"
                ),
                ForensicDeductionHypothesis(
                    id = 2,
                    title = "الفرضية (ب): ارتكاب الواقعة بدافع سرقة عشوائية فردية",
                    explanation = "لص عابر ارتكب الحادث ولا توجد خلفيات تنظيمية.",
                    isCorrect = false,
                    feedback = "وجود وثائق التشفير وأدلة التلاعب الرقمي ينفي السرقة العشوائية كلياً!",
                    forensicConfidenceTag = "فرضية مضللة ومردودة"
                ),
                ForensicDeductionHypothesis(
                    id = 3,
                    title = "الفرضية (ج): حادث عرضي ناجم عن إهمال غير مقصود",
                    explanation = "الحادث وقع نتيجة صدفة بحتة دون نية مسبقة.",
                    isCorrect = false,
                    feedback = "تناقض حجة الغياب وأكواد التتبع تثبت الترصد والنية المبيتة!",
                    forensicConfidenceTag = "يناقض وقائع المعاينة"
                ),
                ForensicDeductionHypothesis(
                    id = 4,
                    title = "الفرضية (د): تصفية حسابات تجارية خاصة بين أفراد مستقلين",
                    explanation = "خلاف مالي بحت بين طرفين دون صلة بأي شبكة أوسع.",
                    isCorrect = false,
                    feedback = "كود الخلية M-0$caseId يربط الواقعة مباشرة بسيرفرات نايل داتا!",
                    forensicConfidenceTag = "يتجاهل الأدلة التقنية"
                )
            )
        )
    }
}
