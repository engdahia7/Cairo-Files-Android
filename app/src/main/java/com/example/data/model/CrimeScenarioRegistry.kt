package com.example.data.model

object CrimeScenarioRegistry {

    fun getAllScenarios(): List<CrimeScenario> = listOf(
        CrimeScenario(
            id = "scenario_blackout",
            title = "سيناريو الإظلام المتعمد واختطاف يوسف كامل",
            category = ScenarioCategory.BLACKOUT_ABDUCTION,
            thesis = "لم يكن انقطاع التيار الكهربائي في عمارة الإيموبيليا عطلاً عشوائياً، بل كان عملية إظلام استخباراتية محكمة لتعطيل مصاعد العمارة وإجبار يوسف كامل على التراجع نحو منور الخدمة الخلفي حيث نُصب له الكمين دون إثارة انتباه الحراسة.",
            requiredConnections = listOf(
                "ev_01_clock" to "noura",
                "noura" to "ev_01_code_m01",
                "ev_01_code_m01" to "youssef"
            ),
            timelineSteps = listOf(
                ScenarioReconstructionStep(
                    stepOrder = 1,
                    timeLabel = "11:42 م",
                    headline = "التسلل لقبو عمارة الإيموبيليا",
                    narrativeDetails = "تسلل عنصران ملثمان إلى غرفة القواطع الرئيسية في القبو، وقاما بفصل الخط المغذي للدور الخامس فقط لخلق إظلام تكتيكي.",
                    supportingClue = "شهادة نورا حمدي حول استمرار إنارة الشارع الخارجي",
                    iconEmoji = "⚡"
                ),
                ScenarioReconstructionStep(
                    stepOrder = 2,
                    timeLabel = "11:47 م",
                    headline = "توقف ساعة الحائط واقتحام الشقة",
                    narrativeDetails = "مع انقطاع النور المفاجئ، توقفت ساعة الحائط السويسرية نتيجة اصطدام المقتحم بمسند الحائط أثناء محاولة فتح الخزنة بحثاً عن ملفات شبكة المرآة.",
                    supportingClue = "زنبرك الساعة المتوقف عند 11:47 تماماً",
                    iconEmoji = "🕰️"
                ),
                ScenarioReconstructionStep(
                    stepOrder = 3,
                    timeLabel = "11:53 م",
                    headline = "المطاردة عبر منور الخدمة الخلفي",
                    narrativeDetails = "طبق يوسف الوصية المخبأة خلف التروس: 'إذا توقفت الساعة.. اهرب من منور الخدمة'، لكن الخاطفين كانوا قد أغلقوا الباب الحديدي السفلي.",
                    supportingClue = "وثيقة الشفرة M-01 المحروقة الأطراف",
                    iconEmoji = "🏃"
                )
            ),
            perpetratorMotive = "مصادرة مسودات التحقيق الاستقصائي الذي كان يوسف كامل يوشك على نشره في جريدة الوقائع لفضح كبار المسؤولين المتورطين في شبكة المرآة.",
            modusOperandi = "تكتيك العمليات الخاصة: عزل الهدف جغرافياً بالقطع الكهربائي، الاقتحام السريع دون استخدام أسلحة نارية لتفادي الضجيج، والاقتياد عبر ممر الشمندر.",
            keySuspectId = "youssef",
            fatalFlaw = "سقوط قصاصة الشفرة M-01 خلف تروس الساعة أثناء ارتباك المقتحم، وتطابق زمن توقف الساعة مع سجلات اتصالات الفجر.",
            deductionInsight = "يوسف كامل لم يمت ليلة الحادث ولم يهرب خائناً؛ بل خُطف بنية انتزاع مفاتيح فك تشفير أرشيف الخوادم السرية.",
            rewardInsightPoints = 200
        ),

        CrimeScenario(
            id = "scenario_mirror_launder",
            title = "سيناريو غسيل أموال شبكة المرآة عبر تجارة الذهب",
            category = ScenarioCategory.MONEY_LAUNDERING,
            thesis = "صابر الصايغ وواجهة دكانه في خان الخليلي لم يكونوا مجرد تجار مجوهرات، بل حلقة وصل سرية لتحويل أموال الابتزاز وتهريب العملة الصعبة تحت غطاء 'قطع غيار ساعات وتحف أثرية سويسرية' لتمويل خوادم NILE DATA.",
            requiredConnections = listOf(
                "saber" to "ev_01_clock",
                "saber" to "ev_03_nile_data",
                "ev_02_photo" to "ev_03_nile_data"
            ),
            timelineSteps = listOf(
                ScenarioReconstructionStep(
                    stepOrder = 1,
                    timeLabel = "قبل الحادث بيومين",
                    headline = "شراء زنبرك الساعات السويسرية النادر",
                    narrativeDetails = "قام صابر الصايغ باستيراد شحنة قطع ميكانيكية دقيقة من جنيف تحتوي على قوالب تروس مشفرة استُخدم أحدها في ساعة مسرح الجريمة.",
                    supportingClue = "فاتورة صابر الصايغ لقطع ساعات نادرة مطابقة لترس 11:47",
                    iconEmoji = "⚙️"
                ),
                ScenarioReconstructionStep(
                    stepOrder = 2,
                    timeLabel = "يوم الجريمة — 04:00 م",
                    headline = "تحويلات مصرفية بملايين الجنيهات",
                    narrativeDetails = "تم إيداع مبالغ نقدية ضخمة في حساب شركة واجهة بالزمالك تدعى NILE DATA تحت بند 'استشارات تقنية وتجهيزات كهروميكانيكية'.",
                    supportingClue = "كشوفات التحويلات البنكية المشبوهة لـ NILE DATA",
                    iconEmoji = "💳"
                ),
                ScenarioReconstructionStep(
                    stepOrder = 3,
                    timeLabel = "ليلة الحادث — 10:15 م",
                    headline = "تسليم أجهزة التنصت والتسجيل للخلية الميدانية",
                    narrativeDetails = "شوهدت سيارة نقل بضائع تابعة لمؤسسة صابر تقف أمام مقر خوادم الزمالك لتفريغ صناديق تحمل شارات خوادم الأرشيف.",
                    supportingClue = "صورة الحادث الليلية التي تظهر مبنى NILE DATA وسيارات النقل",
                    iconEmoji = "📦"
                )
            ),
            perpetratorMotive = "بناء إمبراطورية مراقبة رقمية واقتصادية تبتز الشخصيات العامة والسياسية دون الاعتماد على ميزانيات رسمية مكشوفة للرقابة العامة.",
            modusOperandi = "استغلال سرية سوق الذهب في خان الخليلي لتحويل السيولة النقدية وشراء معدات الاستخبارات الأجنبية باسم تجارة الأنتيكات النادرة.",
            keySuspectId = "saber",
            fatalFlaw = "التطابق التام بين الترقيم التسلسلي لزنبرك الساعة المعثور عليه في شقة يوسف وبين دفاتر واردات صابر الصايغ المسجلة في الغرفة التجارية.",
            deductionInsight = "شبكة المرآة ليست مجرد شبكة تجسس سياسي، بل كيان اقتصادي متوحش يمول أذرعه من عمليات غسيل الذهب والابتزاز المالي المنظم.",
            rewardInsightPoints = 250
        ),

        CrimeScenario(
            id = "scenario_police_complicity",
            title = "سيناريو الاختراق والتواطؤ الأمني لتعطيل الإنقاذ",
            category = ScenarioCategory.STATE_COMPLICITY,
            thesis = "الرائد عمر لم يتواجد في مسرح الجريمة بدافع الواجب الأمني؛ بل كُلف بمهمة رسمية من قيادة عليا لتطويق المنطقة وتضليل شهود العيان، ومنع أي استجابة لشرطة النجدة حتى إتمام انسحاب سيارة الخاطفين نحو مقر الزمالك.",
            requiredConnections = listOf(
                "omar" to "ev_02_photo",
                "omar" to "marwan",
                "ev_01_code_m01" to "ev_secret_subject01"
            ),
            timelineSteps = listOf(
                ScenarioReconstructionStep(
                    stepOrder = 1,
                    timeLabel = "11:30 م",
                    headline = "إعادة توجيه دوريات شرطة وسط البلد",
                    narrativeDetails = "أصدر الرائد عمر إشارة لاسلكية بإخلاء مربع شارع صبري أبو علم بدعوى وجود 'تمرين أمني لمكافحة الشغب'.",
                    supportingClue = "سجل إشارات شرطة قصر النيل ليلة الحادث",
                    iconEmoji = "📻"
                ),
                ScenarioReconstructionStep(
                    stepOrder = 2,
                    timeLabel = "11:55 م",
                    headline = "تأمين هروب سيارة الخاطفين السوداء",
                    narrativeDetails = "وقفت سيارة الدورية رقم (482 شرطة) عند ناصية شارع قصر النيل لفتح الإشارة وتأمين عبور السيارة التي تقل يوسف كامل نحو كوبري قصر النيل.",
                    supportingClue = "ظهور رقم لوحة الدورية في زاوية صورة الحادث المسربة",
                    iconEmoji = "🚓"
                ),
                ScenarioReconstructionStep(
                    stepOrder = 3,
                    timeLabel = "01:20 ص",
                    headline = "التحري المشبوه عن مهندس الاتصالات مروان",
                    narrativeDetails = "بدأ الرائد عمر فور وقوع الحادث في الاستفسار السري عن هوية مروان صبري وموقع أجهزة الراديو الخاصة به لإسكات أي محاولة لتتبع الترددات.",
                    supportingClue = "محاضر التحريات الأمنية الموجهة ضد مروان صبري",
                    iconEmoji = "🔍"
                )
            ),
            perpetratorMotive = "حماية أسرار مشروع SUBJECT 01 الذي يشرف عليه جنرالات كبار، مقابل وعود بالترقية لقيادة جهاز الأمن العام ومحو ملف قديم يدينه.",
            modusOperandi = "استغلال الصلاحيات الشرطية الرسمية: إصدار بلاغات كاذبة، ترهيب الشهود، وإتلاف سجلات الدوريات وتوجيه الاتهامات نحو لصوص عاديين.",
            keySuspectId = "omar",
            fatalFlaw = "انعكاس أضواء سيارة الدورية في واجهة المتجر في صورة الحادث، مما نسف ادعاء الرائد عمر بأنه كان متواجداً في قسم الدقي تلك الليلة.",
            deductionInsight = "جهاز الشرطة الرسمي مخترق في مستوياته القيادية؛ التحقيق لا يمكن أن ينجح عبر المذكرات الحكومية ويجب أن يعتمد كلياً على الجهد الاستقصائي المستقل.",
            rewardInsightPoints = 250
        ),

        CrimeScenario(
            id = "scenario_dawn_call_counterescape",
            title = "سيناريو مكالمة الفجر والهروب البديل",
            category = ScenarioCategory.STAGED_DISAPPEARANCE,
            thesis = "يوسف كامل نجح بفضل فطنته الاستقصائية في كسر احتجازه المؤقت في مبنى خوادم الزمالك، ولجأ لكابينة هاتف عمومي في شارع قصر النيل عند 02:13 ص لبث رسالة مشفرة إلى المحقق محمد عبده قبل أن يختفي في مخبأ آمن بحي الحسين.",
            requiredConnections = listOf(
                "mohamed" to "youssef",
                "marwan" to "ev_01_phone",
                "marwan" to "ev_03_nile_data"
            ),
            timelineSteps = listOf(
                ScenarioReconstructionStep(
                    stepOrder = 1,
                    timeLabel = "01:45 ص",
                    headline = "الهروب من قبو خوادم NILE DATA بالزمالك",
                    narrativeDetails = "استغل يوسف انشغال الحراس بنقل الأقراص الممغنطة، وتسلل عبر نافذة التهوية المطلة على حديقة الفيلا المجاورة.",
                    supportingClue = "سجلات كسر التشفير التي استخرجها مروان صبري",
                    iconEmoji = "🔓"
                ),
                ScenarioReconstructionStep(
                    stepOrder = 2,
                    timeLabel = "02:13 ص",
                    headline = "مكالمة الفجر المشفرة عبر هاتف قصر النيل",
                    narrativeDetails = "رن هاتف مكتب المحقق محمد عبده؛ خرج صوت يوسف متقطعاً يحمل نبرة حاسمة: 'المرآة ليست مجرد شبكة.. إنها في كل مكان.. ابحث في رمز M-01'.",
                    supportingClue = "تسجيل هاتف الفجر الصادر من كابينة قصر النيل العمومية",
                    iconEmoji = "☎️"
                ),
                ScenarioReconstructionStep(
                    stepOrder = 3,
                    timeLabel = "02:40 ص",
                    headline = "الانتقال للمخبأ الآمن وتضليل المطاردين",
                    narrativeDetails = "تخلص يوسف من معطفه وساعته القديمة لقطع أي أثر تتبع، وتوجه متخفياً إلى الأزقة القديمة الملتوية في درب الشمندر.",
                    supportingClue = "إشارة التردد اللاسلكي التي رصدها جهاز مروان صبري",
                    iconEmoji = "🏮"
                )
            ),
            perpetratorMotive = "إيصال وثائق الإدانة النهائية للمحقق محمد عبده دون الوقوع في قبضة عملاء شبكة المرآة أو دوريات الرائد عمر المتواطئة.",
            modusOperandi = "تكتيك التمويه المزدوج: الإيحاء بأنه قد تمت تصفيته لإيقاف حملة المطاردة الشرسة، والتواصل عبر شفرات مسجلة على أشرطة الفجر.",
            keySuspectId = "marwan",
            fatalFlaw = "التطابق بين صوت رنين الهاتف المسجل عند 02:13 ص وتردد جهاز التنصت التناظري الذي بحوزة مروان صبري.",
            deductionInsight = "المعركة لم تنتهِ بعد؛ يوسف كامل على قيد الحياة، ويمتلك الجزء الثاني من شفرة M-01، مما يفتح الباب للمواجهة الحاسمة لفك اللغز بأكمله.",
            rewardInsightPoints = 300
        )
    )

    fun getScenarioById(id: String): CrimeScenario? {
        return getAllScenarios().find { it.id == id }
    }
}
