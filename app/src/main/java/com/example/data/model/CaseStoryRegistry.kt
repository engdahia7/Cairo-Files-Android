package com.example.data.model

data class InvestigationStoryStep(
    val stepIndex: Int,
    val title: String,
    val locationDescription: String,
    val narrativeEvent: String,
    val interactiveChallenge: String,
    val choiceOptions: List<StoryChoiceOption>
)

data class StoryChoiceOption(
    val choiceId: Int,
    val label: String,
    val detailedReaction: String,
    val clueUnlocked: String,
    val isProgressAdvancing: Boolean,
    val impactTag: String
)

object CaseStoryRegistry {

    fun getStoryStepsForCase(caseId: Int): List<InvestigationStoryStep> {
        return when (caseId) {
            1 -> getCase1Steps()
            2 -> getCase2Steps()
            3 -> getCase3Steps()
            4 -> getCase4Steps()
            5 -> getCase5Steps()
            6 -> getCase6Steps()
            7 -> getCase7Steps()
            8 -> getCase8Steps()
            9 -> getCase9Steps()
            10 -> getCase10Steps()
            else -> getDynamicCaseSteps(caseId)
        }
    }

    private fun getCase1Steps(): List<InvestigationStoryStep> = listOf(
        InvestigationStoryStep(
            stepIndex = 1,
            title = "المحطة الأولى: معاينة الشقة المغلقة في شارع صبري أبو علم",
            locationDescription = "وسط البلد — عمارة رقم 14، الدور الخامس، الشقة معتمة ورائحة البخور تخفي شيئاً آخر",
            narrativeEvent = "تصل إلى شقة الشاكية نورا حمدي بصحبة الشاويش فرغلي. الباب الخشبي سليم من الخارج، لكن عند الدخول تجد الأوراق مبعثرة بطريقة انتقائية، وهناك ساعة حائط نحاسية قديمة عقاربها متوقفة عمداً عند 11:47 تماماً.",
            interactiveChallenge = "أين توجه تركيزك الجنائي أولاً لكشف لغز اختفاء شقيقها؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "فك غطاء الساعة النحاسية وفحص التروس الداخلية",
                    detailedReaction = "خلف زنبرك الساعة عثرت على ورقة مطوية بعناية تحمل ختماً كربونياً برمز (M-01) ومكتوب عليها بالرصاص: 'إذا توقفت الساعة.. اهرب من منور الخدمة'.",
                    clueUnlocked = "دليل ورقة الشفرة M-01",
                    isProgressAdvancing = true,
                    impactTag = "دليل حاسم 🔍"
                ),
                StoryChoiceOption(
                    choiceId = 2,
                    label = "فحص الخزنة الحديدية في غرفة المكتب",
                    detailedReaction = "الخزنة مفتوحة بمفتاحها الأصلي.. الأموال والمجوهرات لم تُمس، لكن الملفات ذات الأغلفة الحمراء قد اختفت بالكامل!",
                    clueUnlocked = "ملاحظة: السارق لم يكن يبحث عن المال",
                    isProgressAdvancing = false,
                    impactTag = "خيط فرعي"
                ),
                StoryChoiceOption(
                    choiceId = 3,
                    label = "معاينة نوافذ الصالة المطلة على الشارع الرئيسي",
                    detailedReaction = "النوافذ مغلقة بمزالجها من الداخل، لكن هناك آثار غبار ممسوح على الحافة يشير لشخص راقب الشارع طويلاً قبل المغادرة.",
                    clueUnlocked = "ملاحظة ترصد ومراقبة مسبقة",
                    isProgressAdvancing = false,
                    impactTag = "خيط مراقبة"
                )
            )
        ),
        InvestigationStoryStep(
            stepIndex = 2,
            title = "المحطة الثانية: ملاحقة الأثر في منور العمارة الخلفي",
            locationDescription = "المنور الرطب — درجات سلم الخدمة القديمة المتآكلة تؤدي إلى ممر مغلق بين عمارتين",
            narrativeEvent = "تتبع مسار هروب شقيق نورا عبر باب الخدمة الخلفي. تنزل الدرجات الحجرية في عتمة الليل، فتسمع صوتاً خافتاً لأقدام تبتعد بسرعة باتجاه درب الشمندر!",
            interactiveChallenge = "كيف تتعامل مع المطاردة في الأزقة الضيقة؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "إطلاق نداء تحذيري وإشهار شارة التحريات والركض بحذر",
                    detailedReaction = "الرجل تعثر في صناديق خشبية وسقط منه جهاز هاتف نوكيا قديم مشفر بكلمة سر قبل أن يختفي في زحام شارع شريف!",
                    clueUnlocked = "هاتف نوكيا قديم برقم سري متكرر",
                    isProgressAdvancing = true,
                    impactTag = "هاتف المشتبه به 📱"
                ),
                StoryChoiceOption(
                    choiceId = 2,
                    label = "التوقف لتفحص آثار الأقدام الموحلة على الأرضية",
                    detailedReaction = "آثار حذاء جلدي إيطالي مقاس 43.. لا يخص شقيق نورا البسيط، بل يخص شخصاً كان يلاحقه أو يقوده!",
                    clueUnlocked = "بصمة حذاء إيطالي فاخر مقاس 43",
                    isProgressAdvancing = false,
                    impactTag = "أثر المطاردة"
                )
            )
        ),
        InvestigationStoryStep(
            stepIndex = 3,
            title = "المحطة الثالثة: مقهى اللواء ومواجهة الشاهد الصامت",
            locationDescription = "مقهى شعبي عتيق يطل على ممر بهلر — دخان الشيشة ووجوه حذرة تراقب الغرباء",
            narrativeEvent = "رقم الهاتف المشفر أجرى آخر مكالمة إلى كابينة هاتفية ملاصقة لمقهى اللواء. تجلس مع عم 'رمضان' القهوجي الذي كان يخدم الوردية ليلة الحادث.",
            interactiveChallenge = "كيف تنتزع الحقيقة من عم رمضان الخائف على لقمة عيشه؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "إظهار صورة شقيق نورا وتقديم ضمان حماية شخصية له ولعائلته",
                    detailedReaction = "عم رمضان يبتلع ريقه ويهمس: 'الراجل ده جاله اتنين لابسين بدل رمادية بعربيات سودا.. ركب معاهم برضاه في شنطة العربية بعد ما سلمهم ظرف أصفر عليه علامة مراية مكسورة!'",
                    clueUnlocked = "اعتراف عم رمضان: سيارات شبكة المرآة الرمادية",
                    isProgressAdvancing = true,
                    impactTag = "اعتراف جوهري ⚖️"
                ),
                StoryChoiceOption(
                    choiceId = 2,
                    label = "التهديد بإغلاق المقهى وسحبه للقسم للتحقيق الرسمي",
                    detailedReaction = "عم رمضان يغلق فمه تماماً ويقسم أنه لم ير أحداً طوال الليل بسبب انقطاع النور العام!",
                    clueUnlocked = "انسداد التحقيق بسبب الضغط الزائد",
                    isProgressAdvancing = false,
                    impactTag = "طريق مسدود ⚠️"
                )
            )
        ),
        InvestigationStoryStep(
            stepIndex = 4,
            title = "المحطة الرابعة: مخزن الأرشيف السري لشركة نايل داتا",
            locationDescription = "قبو تحت الأرض في شارع رشدي — ملفات قديمة وخوادم تصدر أزيزاً مستمراً",
            narrativeEvent = "تقودك الخيوط إلى مقر فرعي تابع لشركة نايل داتا. كود M-01 الذي وجدته في الساعة يفتح الباب الحديدي! في الداخل تجد ملفاً كبيراً مفتوحاً على مكتب باسم 'SUBJECT 01' وبداخله اسمك أنت: 'محمد عبده'!",
            interactiveChallenge = "اللحظة الحاسمة: كيف تفك لغز ملفك الشخصي داخل أرشيف القضية؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "ربط تاريخ فتح الملف بتاريخ اختفاء صديقك الصحفي يوسف كامل",
                    detailedReaction = "صدمة مدوية! الملف فُتح في نفس الليلة التي ادعوا فيها وفاة يوسف، وشقيق نورا لم يكن إلا ساعي بريد سري كلفه يوسف بتهريب ملفك قبل تصفيته!",
                    clueUnlocked = "الرابط المباشر بين يوسف وشبكة المرآة",
                    isProgressAdvancing = true,
                    impactTag = "كشف المؤامرة الكبرى 💥"
                ),
                StoryChoiceOption(
                    choiceId = 2,
                    label = "تصوير المستندات بسرعة بالهاتف ومغادرة المكان فوراً",
                    detailedReaction = "التقطت صوراً لثلاث صفحات سرية قبل أن يطلق جهاز الإنذار صفيراً مدوياً وتضطر للانسحاب عبر مخرج الطوارئ!",
                    clueUnlocked = "وثائق مسربة من قبو نايل داتا",
                    isProgressAdvancing = true,
                    impactTag = "وثائق رسمية 📄"
                )
            )
        )
    )

    private fun getCase2Steps(): List<InvestigationStoryStep> = listOf(
        InvestigationStoryStep(
            stepIndex = 1,
            title = "المحطة الأولى: كوبري قصر النيل — موقع الحادث في الفجر",
            locationDescription = "على الكوبري وسط الضباب البارد — آثار فرامل غريبة ودماء جافة بالقرب من تمثال الأسد",
            narrativeEvent = "ملف حادث الصحفي يوسف كامل تم قيده 'حادث دهس وهروب ضد مجهول'. تقف في مسرح الحادث وتعاين عمود الإنارة المصدوم. التقرير الرسمي يزعم أن سيارة النقل دهسته، لكن الزجاج المتناثر ينتمي لمصباح سيارة دفع رباعي مصفحة!",
            interactiveChallenge = "كيف تفند الرواية الرسمية الكاذبة؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "جمع شظايا الزجاج وفحص أرقام المصنع المطبوعة عليها",
                    detailedReaction = "شظايا الزجاج تحمل كود (GL-SPEC-99) وهي زجاج مصفح مخصص حصرياً لسيارات الحراسات الخاصة لشركة نايل داتا!",
                    clueUnlocked = "دليل الزجاج المصفح لسيارة الحراسات",
                    isProgressAdvancing = true,
                    impactTag = "دليل فني قاطع 🔬"
                ),
                StoryChoiceOption(
                    choiceId = 2,
                    label = "فحص تسجيلات كاميرات المرور فوق الكوبري",
                    detailedReaction = "الضابط المسؤول يخبرك أن الكاميرات تعرضت لـ 'عطل فني مفاجئ' استمر 20 دقيقة بدأت قبل الحادث بـ 5 دقائق!",
                    clueUnlocked = "شبهة تلاعب رسمي بكاميرات المراقبة",
                    isProgressAdvancing = true,
                    impactTag = "شبهة تلاعب 🎥"
                )
            )
        ),
        InvestigationStoryStep(
            stepIndex = 2,
            title = "المحطة الثانية: مشرحة زينهم — لغز الجثة الغائبة",
            locationDescription = "قبو مشرحة زينهم — برودة صامتة ورائحة المطهرات القوية",
            narrativeEvent = "تقابل الدكتور نبيل خبير الطب الشرعي الصديق القديم. تطلب منه مراجعة ملف تشريح جثة يوسف كامل. ينظر حوله بقلق ويغلق باب الغرفة بالمفتاح!",
            interactiveChallenge = "كيف تدفع الطبيب للكشف عن السر الخطير؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "تذكيره بالقسم المهني وطلب التقرير الأولي قبل التعديل",
                    detailedReaction = "الدكتور نبيل يخرج ملفاً من خزنته السرية: 'يا محمد.. الجثة اللي دفنوها مكنتش جثة يوسف! بصمات الأسنان وفصيلة الدم كانت لشخص تاني مجهول، وجالي أمر عسكري بإغلاق التقرير فوراً!'",
                    clueUnlocked = "تقرير الطب الشرعي السري: يوسف لم يمت!",
                    isProgressAdvancing = true,
                    impactTag = "حقيقة صادمة ⚡"
                ),
                StoryChoiceOption(
                    choiceId = 2,
                    label = "مقارنة صور الحادث مع جثة الضحية في الدفتر",
                    detailedReaction = "تكتشف أن طول الضحية المدون 182 سم، بينما يوسف كان طوله 174 سم فقط.. التناقض واضح كالشمس!",
                    clueUnlocked = "تناقض في بيانات الضحية الجثمانية",
                    isProgressAdvancing = true,
                    impactTag = "دليل مادي"
                )
            )
        ),
        InvestigationStoryStep(
            stepIndex = 3,
            title = "المحطة الثالثة: سطح العمارة المهجورة بروض الفرج",
            locationDescription = "سطح عمارة تطل على النيل — غرفة غسيل قديمة تحولت لمقر تنصت سري",
            narrativeEvent = "يقودك أثر بطاقة الشحن إلى غرفة معزولة فوق سطح بروض الفرج. في الداخل تجد أجهزة تسجيل صوتي، وصوراً معلقة بخيوط حمراء لكل أعضاء مجلس إدارة نايل داتا، وميكروفوناً ما زال يعمل!",
            interactiveChallenge = "ماذا تفعل داخل وكر الصحفي المختفي؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "تشغيل شريط الكاسيت الموجود داخل جهاز التسجيل الرئيسي",
                    detailedReaction = "تسمع صوت يوسف يتحدث: 'محمد.. إذا وصلت إلى هنا فأنت تعرف الآن أنني حي. المنظمة اسمها المرآة، ورئيسها ليس غريباً عنك.. إنه شخص تقابله كل يوم في الميدان!'",
                    clueUnlocked = "تسجيل يوسف الصوتي السري رقم 02",
                    isProgressAdvancing = true,
                    impactTag = "اعتراف يوسف 🎙️"
                ),
                StoryChoiceOption(
                    choiceId = 2,
                    label = "تفتيش الخزانة الخشبية تحت طاولة الأجهزة",
                    detailedReaction = "عثرت على مسدس كولت 9 ملم مرخص وجواز سفر أجنبي مزور باسم مستعار يحمل صورة يوسف!",
                    clueUnlocked = "جواز سفر مزور وخطة هروب يوسف",
                    isProgressAdvancing = false,
                    impactTag = "خيط فرار"
                )
            )
        )
    )

    private fun getCase3Steps(): List<InvestigationStoryStep> = listOf(
        InvestigationStoryStep(
            stepIndex = 1,
            title = "المحطة الأولى: برج النيل بالدقي — اختفاء وثيقة الاستحواذ",
            locationDescription = "الدور الـ 22 — مكاتب زجاجية فاخرة وخزنة سويسرية مفتوحة دون كسر",
            narrativeEvent = "رئيس الشؤون القانونية يستقبلك مذهولاً. وثيقة استحواذ بمليارات الجنيهات اختفت من الخزنة التي لا تفتح إلا ببصمتين إلكترونيتين ورمز رقمي. في مكان الوثيقة، وضع الجاني رزمة ورق أبيض مختومة بعبارة: NILE DATA ARCHIVE.",
            interactiveChallenge = "كيف تكشف ثغرة الدخول إلى الخزنة الحصينة؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "استخراج سجل ولوج الخزنة الرقمي وفحص التوقيت الدقيق للفتح",
                    detailedReaction = "السجل يظهر أن الباب فُتح الساعة 03:14 فجراً باستخدام حساب مدير تكنولوجيا المعلومات الذي توفي بحادث سير قبل أسبوعين!",
                    clueUnlocked = "استخدام هوية رقمية لشخص متوفى",
                    isProgressAdvancing = true,
                    impactTag = "ثغرة رقمية 💻"
                ),
                StoryChoiceOption(
                    choiceId = 2,
                    label = "رفع البصمات الكامنة عن لوحة الأرقام ومقبض الخزنة",
                    detailedReaction = "المقبض تم تنظيفه بمادة كيميائية تزيل البصمات، لكن قطرة صغيرة سقطت على السجادة تفوح منها رائحة أسيتون نقي.",
                    clueUnlocked = "أثر مذيب كيميائي ممسوح عمداً",
                    isProgressAdvancing = false,
                    impactTag = "أثر كيميائي"
                )
            )
        ),
        InvestigationStoryStep(
            stepIndex = 2,
            title = "المحطة الثانية: سيرفر الشركة السري ومصيدة الفيروس",
            locationDescription = "غرفة السيرفرات المبردة — خوادم تومض باللون الأحمر ومؤشر اختراق نشط",
            narrativeEvent = "تدخل غرفة السيرفرات بصحبة مهندس الصيانة. تلاحظ أن كابلات الشبكة موصولة بجهاز راوتر خارجي مخفي خلف لوحة الإطفاء، يقوم بنسخ بيانات العقود مباشرة إلى خادم سحابي خارجي!",
            interactiveChallenge = "ما هي خطوتك لمحاصرة تدفق البيانات المسروقة؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "تتبع مسار عنوان IP الخادم السحابي المستلم قبل قطع الكابل",
                    detailedReaction = "نجحت في التقاط عنوان الـ IP النهائي.. البيانات تُرسل مباشرة إلى خادم داخل مبنى الأرشيف القديم لشركة نايل داتا بشارع رشدي!",
                    clueUnlocked = "عنوان IP لخادم الأرشيف السري",
                    isProgressAdvancing = true,
                    impactTag = "رابط شبكة المرآة 🌐"
                ),
                StoryChoiceOption(
                    choiceId = 2,
                    label = "فصل التيار الكهربائي العمومي فوراً لحماية ما تبقى من ملفات",
                    detailedReaction = "التيار انقطع وحمى الملفات، لكن السيرفر تم مسح ذاكرته المؤقتة وفقدت أثر مسار الإرسال المباشر.",
                    clueUnlocked = "حماية جزئية مع ضياع أثر التتبع",
                    isProgressAdvancing = false,
                    impactTag = "إجراء دفاعي"
                )
            )
        ),
        InvestigationStoryStep(
            stepIndex = 3,
            title = "المحطة الثالثة: مواجهة نائب رئيس الشركة في جراج المبنى",
            locationDescription = "جراج B3 المعتم تحت الأرض — سيارة مرسيدس سوداء تستعد للمغادرة",
            narrativeEvent = "تعترض طريق سيارة نائب رئيس مجلس الإدارة قبل خروجها للشارع. تطلب تفتيش الحقيبة الجلدية التي وضعها على المقعد الخلفي للسيارة بتوتر واضح.",
            interactiveChallenge = "كيف تواجه المسؤول النافذ وتنتزع الوثيقة؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "مواجهته بسجل اتصالاته المتطابق مع توقيت فتح الخزنة ونسخ البيانات",
                    detailedReaction = "الرجل يسقط على المقعد ويرتعش: 'أنا مضطر يا كابتن محمد.. هددوني بتسجيلات وفضائح ليا لو ما سلمتش الوثيقة للمندوب بتاعهم!'",
                    clueUnlocked = "اعتراف بالابتزاز المنظم من شبكة المرآة",
                    isProgressAdvancing = true,
                    impactTag = "اعتراف مدوّي 📑"
                )
            )
        )
    )

    private fun getCase4Steps(): List<InvestigationStoryStep> = listOf(
        InvestigationStoryStep(
            stepIndex = 1,
            title = "المحطة الأولى: شارع جامعة الدول — مسرح الخطف الليلي",
            locationDescription = "خلف فندق شهير في المهندسين — زقاق مظلم تفر منه شاحنة مغلقة",
            narrativeEvent = "بلاغ عاجل عن اختطاف رجل أعمال من أمام مطعم فاخر. 'عم سيد' عامل النظافة كان ينظف الرصيف المقابل حين توقفت شاحنة مغلقة وقام ملثمون بسحب الرجل داخلها. عم سيد يقف ممسكاً بمكنسته ويرتجف من الرعب.",
            interactiveChallenge = "كيف تبدأ التحقيق مع الشاهد الوحيد في الموقع؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "تهدئة عم سيد والابتعاد به عن أعين الفضوليين وضباط الدورية",
                    detailedReaction = "عم سيد يشعر بالأمان ويخرج من جيب صديري النظافة قصاصة ورقية سقطت من الخاطفين عليها رقم هاتف ولوحة شاحنة مموهة.",
                    clueUnlocked = "قصاصة رقم هاتف الخاطفين الميداني",
                    isProgressAdvancing = true,
                    impactTag = "دليل مسرح الجريمة 🧾"
                ),
                StoryChoiceOption(
                    choiceId = 2,
                    label = "استجوابه بحزم رسمي وكتابة محضر فوري في الشارع",
                    detailedReaction = "عم سيد يخاف من تكرار اسمه في المحاضر الرسمية ويتلعثم ويقول: 'أنا نظري ضعيف ومشوفتش ملامحهم يا بيه'.",
                    clueUnlocked = "تردد وخوف الشاهد",
                    isProgressAdvancing = false,
                    impactTag = "تحقيق معطل"
                )
            )
        ),
        InvestigationStoryStep(
            stepIndex = 2,
            title = "المحطة الثانية: بيت عم سيد في بولاق الدكرور — رسالة التهديد",
            locationDescription = "حارة ضيقة في بولاق — الباب الخشبي عليه علامة طلاء حمراء جديدة",
            narrativeEvent = "ترافق عم سيد إلى منزله لحمايته. تجد زوجته تبكي عند الباب؛ شخص مجهول ألقى حجراً داخل الصالة مربوطاً بخرطوش فارغ ورسالة نصها: 'ابنك في مدرسة الصنايع.. لسانك لو نطق هتدفنه بايدك'.",
            interactiveChallenge = "كيف تحمي الشاهد وتحافظ على شهادته الجنائية في نفس الوقت؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "تنسيق مأوى آمن لعائلته فوراً في شقة تابعة لحماية الشهود وإرسال حراسة للابن",
                    detailedReaction = "عم سيد يبكي من الامتنان ويعترف: 'العربية الشاحنة كان على بابها لزقة مكتوب عليها نايل لوجستيكس وكان معاهم جهاز تشويش لاسلكي!'.",
                    clueUnlocked = "تحديد هوية شاحنة شركة نايل لوجستيكس",
                    isProgressAdvancing = true,
                    impactTag = "حماية وكشف الشاحنة 🛡️"
                ),
                StoryChoiceOption(
                    choiceId = 2,
                    label = "أخذ الخرطوش والرسالة وتحريزها للمختبر الجنائي فوراً",
                    detailedReaction = "المختبر يؤكد وجود بصمات ملوثة بالبارود ولكن التحليل يستغرق 48 ساعة دون توفير حماية للشاهد.",
                    clueUnlocked = "تحريز مادي متأخر",
                    isProgressAdvancing = false,
                    impactTag = "إجراء بطيء"
                )
            )
        ),
        InvestigationStoryStep(
            stepIndex = 3,
            title = "المحطة الثالثة: مداهمة مخزن الشاحنات في المنيب",
            locationDescription = "مستودع شاحنات منعزل بالقرب من الطريق الدائري",
            narrativeEvent = "تقتحم المستودع بناءً على شهادة عم سيد. تجد الشاحنة مركونة هناك بعد تغيير لوحاتها، والمخطوف محتجز في غرفة حراسة خلفية مقيداً بحبال بلاستيكية.",
            interactiveChallenge = "كيف تحسم المداهمة وتحرر المخطوف؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "مباغتة الحارس الوحيد وتحرير الرهينة والتحفظ على أجهزة التسجيل في الشاحنة",
                    detailedReaction = "تم تحرير الضحية بسلام! الضحية يعترف أن الخاطفين كانوا يريدون منه التنازل عن أسهم شركة اتصالات لصالح واجهة تابعة لمنظمة المرآة!",
                    clueUnlocked = "إفادة المخطوف حول دافع شبكة المرآة الاستثماري",
                    isProgressAdvancing = true,
                    impactTag = "تحرير ناجح وكشف الدافع 🎯"
                )
            )
        )
    )

    private fun getCase5Steps(): List<InvestigationStoryStep> = listOf(
        InvestigationStoryStep(
            stepIndex = 1,
            title = "المحطة الأولى: غرفة المراقبة المركزية — ميدان التحرير",
            locationDescription = "مجمع التحكم في كاميرات العاصمة — عشرات الشاشات التلفزيونية الحية",
            narrativeEvent = "الرائد عمر الديب يستدعيك وهو في حيرة بالغة. أربع كاميرات في أربعة ميادين (التحرير، طلعت حرب، الأوبرا، باب اللوق) تظهر نفس المشتبه به يرتدي معطفاً بنياً في تمام الساعة 03:20:15 فجراً في نفس الثانية بالضبط!",
            interactiveChallenge = "كيف تفسر التواجد المستحيل للمشتبه به في 4 أماكن متزامنة؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "فحص الكود البرمجي لحزمة البث الرقمي (Timestamp) للكاميرات الأربع",
                    detailedReaction = "اكتشاف عبقري! البث في ثلاثة ميادين لم يكن حياً، بل تم حقن فيديو مسجل مسبقاً بفارق 12 دقيقة عبر خادم وسيط!",
                    clueUnlocked = "دليل التلاعب بالبث وحقن الفيديو المسجل",
                    isProgressAdvancing = true,
                    impactTag = "كشف تزييف البث 💻"
                ),
                StoryChoiceOption(
                    choiceId = 2,
                    label = "افتراض وجود أربعة أشخاص متطابقين يرتدون نفس المعطف كخطة تمويه",
                    detailedReaction = "فحص تفاصيل المشية والملامح يثبت أنه نفس الشخص بذات الندبة في رقبته.. فرضية الشبيه التوأم تسقط تماماً.",
                    clueUnlocked = "استبعاد فرضية الشبيه",
                    isProgressAdvancing = false,
                    impactTag = "تحقق بصري"
                )
            )
        ),
        InvestigationStoryStep(
            stepIndex = 2,
            title = "المحطة الثانية: السيرفر المضيف لخوارزمية المرور",
            locationDescription = "مبنى إدارة المرور القديم — كابينة التحكم في الألياف الضوئية",
            narrativeEvent = "تتبع مسار الألياف الضوئية التي تم حقن الفيديو المسجل من خلالها. تجد جهاز فلاش درايف مصغر مغروس داخل فتحة سيرفر المراقبة يحمل توقيعاً كودياً مألوفاً: 'MARWAN-S-SYS'.",
            interactiveChallenge = "ما دلالة كود مروان صبري خبير التقنية في هذه القضية؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "استخراج سجل البيانات من الفلاش درايف وتحديد مسار هروب المشتبه به الحقيقي",
                    detailedReaction = "الكاميرا الرابعة في باب اللوق كانت هي الحقيقية الوحيدة! المشتبه به اتجه فعلياً نحو سوق الفلكي وليس ميدان التحرير!",
                    clueUnlocked = "تحديد موقع المشتبه به الفعلي في سوق الفلكي",
                    isProgressAdvancing = true,
                    impactTag = "كشف المسار الحقيقي 📍"
                )
            )
        )
    )

    private fun getCase6Steps(): List<InvestigationStoryStep> = listOf(
        InvestigationStoryStep(
            stepIndex = 1,
            title = "المحطة الأولى: مقهى الحناوي بالسيدة زينب — اختفاء الزبون اليومي",
            locationDescription = "حارة ضيقة هادئة بالسيدة — طاولات خشبية عتيقة وشاي بالنعناع",
            narrativeEvent = "عم صابر صاحب المقهى يطلبك على انفراد. زبون غامض كان يجلس يومياً في الركن المعتم يدون ملاحظات في مفكرة جلدية سوداء. الرجل لم يحضر منذ 3 أيام وترك مفكرته مخبأة تحت المقعد الخشبي.",
            interactiveChallenge = "كيف تفك طلاسم مفكرة الزبون المختفي؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "استخدام قلم الرصاص وتمريره برفق على الصفحة الفارغة التالية لكشف الكتابة المضغوطة",
                    detailedReaction = "ظهرت مخططات دقيقة تربط 4 أرقام سيارات، وتاريخ مقابلة سرية مع الصحفي يوسف كامل قبل اختفائه بيوم واحد!",
                    clueUnlocked = "مخطط مفكرة مقهى الحناوي المشفر",
                    isProgressAdvancing = true,
                    impactTag = "مخطط مشفر 📓"
                )
            )
        ),
        InvestigationStoryStep(
            stepIndex = 2,
            title = "المحطة الثانية: اللوحة الجنائية وتوصيل الخيوط",
            locationDescription = "مكتب محمد عبده — لوحة الفلين المعلقة وأوتاد التثبيت الحمراء",
            narrativeEvent = "تأخذ بيانات المفكرة وتثبتها على لوحة الفلين. لأول مرة تتصل خيوط الشقة المقفولة بالقضية الأولى، بحادث سيارة الصحفي، بأرقام نايل داتا!",
            interactiveChallenge = "ما هو الاستنتاج الذي يربط الزبون بالقضايا السابقة؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "ربط هوية الزبون بالمحقق الخاص السابق الذي كلفته عائلة يوسف بالبحث عنه",
                    detailedReaction = "تطابق مذهل! الزبون لم يكن سوى المحقق 'عادل كمال' الذي اختطفته المنظمة بعد أن اقترب من فضح مقرهم الرئيسي!",
                    clueUnlocked = "كشف هوية الزبون: المحقق عادل كمال",
                    isProgressAdvancing = true,
                    impactTag = "خيط اللوحة الأحمر 📌"
                )
            )
        )
    )

    private fun getCase7Steps(): List<InvestigationStoryStep> = listOf(
        InvestigationStoryStep(
            stepIndex = 1,
            title = "المحطة الأولى: شقة الدور السابع بعمارة الأوقاف",
            locationDescription = "العباسية — شقة مغلقة من الخارج بعدة أقفال تستهلك طاقة كهربائية تعادل مصنعاً صغيراً",
            narrativeEvent = "تدخل الشقة المظلمة بعد كسر الأقفال. في منتصف الصالة مصفوفة شاشات متصلة بكاميرات تجسس موجهة مباشرة نحو نافذة مكتبك في شارع طلعت حرب! وعلى المكتب ملف ضخم بعنوان: 'الهدف: محمد عبده'.",
            interactiveChallenge = "كيف تتعامل مع صدمة اكتشاف أنك كنت تحت المراقبة على مدار الساعة؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "تفتيش ملف 'محمد عبده' وقراءة التقييم النفسي والتحركات المرصودة",
                    detailedReaction = "الملف يوثق كل قضية حققت فيها منذ 3 سنوات، مع تقييم دقيق: 'محمد يتميز بالهوس بالعدالة.. يمكن التنبؤ بقراراته عبر دفعه لحماية أصدقائه'!",
                    clueUnlocked = "الملف النفسي الاستخباري لمحمد عبده",
                    isProgressAdvancing = true,
                    impactTag = "كشف ملفك الشخصي 📁"
                ),
                StoryChoiceOption(
                    choiceId = 2,
                    label = "تتبع مصدر بث الشاشات وتحديد خادم التخزين السحابي للغرفة",
                    detailedReaction = "الكاميرات كانت تبث إشارتها مباشرة إلى مقر برج النيل التابع لمنظمة المرآة!",
                    clueUnlocked = "مصدر إشارة بث كاميرات التجسس",
                    isProgressAdvancing = true,
                    impactTag = "مصدر البث 📡"
                )
            )
        )
    )

    private fun getCase8Steps(): List<InvestigationStoryStep> = listOf(
        InvestigationStoryStep(
            stepIndex = 1,
            title = "المحطة الأولى: مكتب بريد وسط البلد — الطرد المؤجل",
            locationDescription = "مكتب بريد العتبة التاريخي — طرد ورقي قديم يحمل أختاماً باهتة",
            narrativeEvent = "ساعي البريد يسلمك طرداً كان محتجزاً في الأمانات لمدة 90 يوماً بتعليمات خاصة. الطرد يحمل خط يد يوسف كامل الشخصي، وبداخله مفتاح نحاسي ثقيل وشريط تسجيل.",
            interactiveChallenge = "ما هي رسالة يوسف الموجهة إليك عبر الطرد؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "تشغيل شريط التسجيل وفحص الكلمات الأخيرة للصحفي",
                    detailedReaction = "صوت يوسف يتردد: 'محمد.. المفتاح ده يفتح خزانة الأمانات 104 في محطة قطار رمسيس.. هناك حطيت الملف اللي بسببه حاولوا يقتلوني.. لا تثق بأحد حتى لو كان رئيسك!'.",
                    clueUnlocked = "مفتاح خزانة أمانات محطة رمسيس رقم 104",
                    isProgressAdvancing = true,
                    impactTag = "طرد يوسف السري 🔑"
                )
            )
        )
    )

    private fun getCase9Steps(): List<InvestigationStoryStep> = listOf(
        InvestigationStoryStep(
            stepIndex = 1,
            title = "المحطة الأولى: كورنيش المعادي ومشرحة زينهم",
            locationDescription = "ضفاف النيل الباردة — جثة مجهولة انتشلها رجال الإنقاذ النهري",
            narrativeEvent = "الدكتورة سلمى طبيبة التشريح تشير إلى جثة شاب يرتدي ملابس رسمية. في جيب سترته بطاقة دخول ممغنطة لشركة NILE DATA. تقرير السموم يكشف مادة شلل عضلي نادرة لا تترك أثراً.",
            interactiveChallenge = "كيف تربط مقتل المبرمج بقضية سرقة الوثيقة في القضية رقم 3؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "مطابقة بصمة المبرمج مع قاعدة بيانات موظفي شركة البيانات المسربة",
                    detailedReaction = "تطابق تام! الضحية هو مبرمج الأنظمة الذي حاول تسريب قاعدة بيانات المشتركين والسياسيين في شبكة المرآة وتم اغتياله لمنعه من الكلام!",
                    clueUnlocked = "هوية مبرمج شبكة المرآة الذي تمت تصفيته",
                    isProgressAdvancing = true,
                    impactTag = "تصفية مبرمج الشبكة 💀"
                )
            )
        )
    )

    private fun getCase10Steps(): List<InvestigationStoryStep> = listOf(
        InvestigationStoryStep(
            stepIndex = 1,
            title = "المحطة الأولى: مكتب محمد عبده — اكتمال خيوط الفصل الأول",
            locationDescription = "شارع طلعت حرب — الساعة 02:13 ص، جدار اللوحة مغطى بالخيوط الحمراء",
            narrativeEvent = "تجلس أمام الجدار الممتلئ بالصور والوثائق من القضايا التسع السابقة. تتطابق الخيوط الحمراء لتشكل شكلاً هندسياً سداسياً يقود إلى اسم واحد مشفر: 'ORGANIZATION 00: THE MIRROR'.",
            interactiveChallenge = "اللحظة التاريخية: كيف تعلن المواجهة المفتوحة مع شبكة المرآة؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "تثبيت خيط المركز وفتح الملف رقم 00 وتأكيد أن يوسف كامل حي ويقود التحريات معك",
                    detailedReaction = "جرس الهاتف يرن فوراً.. ترفع السماعة، لتسمع صوت يوسف بوضوح: 'مبروك يا محمد.. انت كده خلصت الفصل الأول ودخلت عش الدبابير بنفسك!'.",
                    clueUnlocked = "فتح الملف المرجعي رقم 00 وبداية الحرب الرسمية ضد المرآة",
                    isProgressAdvancing = true,
                    impactTag = "انفجار لغز الفصل الأول 🏆"
                )
            )
        )
    )

    private fun getDynamicCaseSteps(caseId: Int): List<InvestigationStoryStep> = listOf(
        InvestigationStoryStep(
            stepIndex = 1,
            title = "المحطة الأولى: مسرح الواقعة الجنائية ومعاينة الآثار",
            locationDescription = "القاهرة — موقع القضية رقم $caseId المليء بالتناقضات والآثار المشبوهة",
            narrativeEvent = "تصل إلى مسرح القضية بصحبة فريق المعاينة. الشواهد الأولية تدل على تدخل منظم من جهة محترفة تسعى لإخفاء الحقائق وطمس الأدلة قبل وصول النيابة.",
            interactiveChallenge = "ما هي خطوتك الاستطلاعية الأولى لتثبيت الأدلة؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "تحريز الأدلة الرقمية وفحص كاميرات المراقبة المحيطة بدقة",
                    detailedReaction = "عثرت على تسجيل رقمي يثبت تواجد سيارة سوداء بزجاج معتم تابعة لشبكة المرآة في نفس توقيت الواقعة تماماً!",
                    clueUnlocked = "تسجيل رقمي يثبت تورط عناصر المرآة في قضية $caseId",
                    isProgressAdvancing = true,
                    impactTag = "خيط رقمي حاسم 💾"
                ),
                StoryChoiceOption(
                    choiceId = 2,
                    label = "استجواب الشهود المتواجدين في محيط العقار",
                    detailedReaction = "أحد الشهود يؤكد تلقيه اتصالاً تحذيرياً لعدم الإدلاء بأي تفاصيل تخص هوية الزوار!",
                    clueUnlocked = "شهادة ترهيب موثقة",
                    isProgressAdvancing = false,
                    impactTag = "شهادة ميدانية"
                )
            )
        ),
        InvestigationStoryStep(
            stepIndex = 2,
            title = "المحطة الثانية: مواجهة المشتبه به وفك الشفرة",
            locationDescription = "مكان استجواب حذر — المشتبه به يحاول المراوغة وإنكار الصلة بالمنظمة",
            narrativeEvent = "تضع المشتبه به أمام الأدلة الجنائية المستخلصة من مسرح الواقعة. الارتباك يبدأ في الظهور على ملامحه مع كل وثيقة تسحبها من ملف القضية.",
            interactiveChallenge = "كيف تكسر دفاعات المشتبه به وتنتزع اعترافه؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "مواجهته بالأثر المادي المتطابق مع قاعدة بيانات المرآة وسجل الاتصالات",
                    detailedReaction = "المشتبه به ينهار ويعترف بالخيط السري والموقع الذي نُقلت إليه باقي الوثائق السرية!",
                    clueUnlocked = "اعتراف رسمي بصلة القضية رقم $caseId بالشبكة",
                    isProgressAdvancing = true,
                    impactTag = "اعتراف كامل ⚖️"
                ),
                StoryChoiceOption(
                    choiceId = 2,
                    label = "الضغط المعنوي بتسليمه لجهات التحقيق العليا",
                    detailedReaction = "المشتبه به يطلب الأمان ويسلمك شريحة هاتف مشفرة تثبت الأوامر الصادرة إليه!",
                    clueUnlocked = "شريحة هاتف الأوامر السرية",
                    isProgressAdvancing = true,
                    impactTag = "شريحة سرية 📱"
                )
            )
        ),
        InvestigationStoryStep(
            stepIndex = 3,
            title = "المحطة الثالثة: مطابقة خيوط القضية والجاهزية للاستنتاج",
            locationDescription = "مكتب المحقق — إحراز الأدلة وربطها بالملف الرئيسي",
            narrativeEvent = "أصبحت خيوط القضية رقم $caseId متسلسلة ومحكمة بالكامل. تم تحريز الأدلة واكتمال التحريات الميدانية للتوجه نحو غرفة الاستنتاج الجنائي.",
            interactiveChallenge = "هل أنت مستعد لرفع الملف إلى غرفة الاستنتاج النهائي؟",
            choiceOptions = listOf(
                StoryChoiceOption(
                    choiceId = 1,
                    label = "اعتماد التقرير الميداني وتأكيد اكتمال تحريات القضية رقم $caseId",
                    detailedReaction = "تم تجهيز ملف الاتهام بدقة.. يمكنك الآن الانتقال لغرفة الاستنتاج لحسم القضية رسمياً وإغلاقها.",
                    clueUnlocked = "الملف الجنائي لقضية $caseId جاهز للحسم النهائي",
                    isProgressAdvancing = true,
                    impactTag = "جاهز للحسم 🎯"
                )
            )
        )
    )
}
