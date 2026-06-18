package com.usbutil.ps2.domain.model

import com.usbutil.ps2.domain.util.OplCrc

enum class MediaType(val code: Int) { CD(0x12), DVD(0x14) }

enum class GameRegion(val display: String, val flag: String) {
    NTSC_U("US", "\uD83C\uDDFA\uD83C\uDDF8"),
    PAL("EU", "\uD83C\uDDEA\uD83C\uDDFA"),
    NTSC_J("JP", "\uD83C\uDDEF\uD83C\uDDF5"),
    UNKNOWN("??", "\uD83C\uDFAE");

    companion object {
        fun fromId(id: String): GameRegion = when {
            id.startsWith("SLUS") || id.startsWith("SCUS") -> NTSC_U
            id.startsWith("SLES") || id.startsWith("SCES") -> PAL
            id.startsWith("SLPS") || id.startsWith("SLPM") ||
                id.startsWith("SCPS") || id.startsWith("SLKA") -> NTSC_J
            else -> UNKNOWN
        }
    }
}

data class Ps2Game(
    val displayName: String,
    val gameId: String,
    val parts: Int,
    val mediaType: MediaType = MediaType.DVD,
    val region: GameRegion = GameRegion.fromId(gameId),
    val sizeBytes: Long = 0L,
) {
    val crcHex: String get() = OplCrc.computeHex(displayName)

    /** ul.[CRC8].[GameID].[partNN] — مثال: ul.8AC12E08.SLUS_209.46.00 */
    fun partFileName(index: Int): String =
        "ul.%s.%s.%02d".format(crcHex, gameId, index)
}
