package com.usbutil.ps2.domain.util

/**
 * CRC للـ OPL محسوب على اسم العرض (MSB-first, POLY=0x04C11DB7, init=0,
 * بلا انعكاس ولا XOR نهائي).
 * ⚠️ يجب التحقق من تطابقه مع اسم ملف ul.* حقيقي قبل الاعتماد عليه (القسم 5).
 */
object OplCrc {
    private const val POLY = 0x04C11DB7L

    fun compute(name: String): Long {
        var crc = 0L
        for (b in name.toByteArray(Charsets.US_ASCII)) {
            crc = crc xor ((b.toLong() and 0xFF) shl 24)
            repeat(8) {
                crc = if (crc and 0x80000000L != 0L)
                    ((crc shl 1) xor POLY) and 0xFFFFFFFFL
                else (crc shl 1) and 0xFFFFFFFFL
            }
        }
        return crc and 0xFFFFFFFFL
    }

    fun computeHex(name: String): String = "%08X".format(compute(name))
}
