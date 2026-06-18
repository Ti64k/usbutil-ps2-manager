# USBUtil PS2 Manager

تطبيق أندرويد احترافي (Kotlin · Jetpack Compose · Clean Architecture + MVVM) لإدارة وتحويل ألعاب **PlayStation 2** إلى أقراص USB عبر **OTG** — بديل محمول لبرنامج سطح المكتب `USBUtil v2.2`.

## المزايا

- إضافة ألعاب من ملفات ISO مع تقطيع تلقائي (أجزاء ≤ 1GiB) بالصيغة `ul.[CRC].[GameID].[partNN]`.
- استخراج مُعرّف اللعبة من `SYSTEM.CNF` (مثل `SLUS_209.46`) وتحديد المنطقة (NTSC/PAL).
- إدارة ثنائية كاملة لملف `ul.cfg` (قراءة/إضافة/إعادة تسمية/حذف/استرداد — 64 بايت/لعبة).
- إعادة بناء ISO أصلي من الأجزاء.
- دعم FAT32 و exFAT عبر SAF، وبديل libaums لـ FAT32.
- عمليات ثقيلة في الخلفية عبر WorkManager + Coroutines مع خدمة مقدّمة.
- واجهة Material 3 بالوضع الداكن: لوحة قيادة، شريط استخدام USB، بطاقات ألعاب، شاشة تقدّم.

## البنية المعمارية

```
com.usbutil.ps2
├─ di/              وحدات Hilt
├─ domain/          النماذج، الواجهات، UseCases
├─ data/            ul.cfg، التخزين، معالجة ISO، العمال
└─ ui/              theme، dashboard، progress، actions
```

## التقنيات

Kotlin · Jetpack Compose (BOM 2024.06.00) · Hilt 2.51.1 · WorkManager 2.9.0 · Coroutines 1.8.1 · libaums 0.10.0 · minSdk 24 · targetSdk 34 · JVM 17.

## ⚠️ ملاحظة حرجة

خوارزمية `OplCrc` تحتاج تحققًا عمليًا مقابل اسم ملف `ul.*` حقيقي مُولّد بـ USBUtil/OPL قبل الاعتماد عليها. راجع `OplCrcTest`.

## الترخيص

MIT
