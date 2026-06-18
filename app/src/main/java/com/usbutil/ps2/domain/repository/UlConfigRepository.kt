package com.usbutil.ps2.domain.repository

import com.usbutil.ps2.domain.model.Ps2Game

interface UlConfigRepository {
    suspend fun readGames(): List<Ps2Game>
    suspend fun appendGame(game: Ps2Game)
    suspend fun renameGame(gameId: String, newName: String)
    suspend fun removeGameEntry(gameId: String)
    suspend fun rebuildFromUsb(): List<Ps2Game>
}
