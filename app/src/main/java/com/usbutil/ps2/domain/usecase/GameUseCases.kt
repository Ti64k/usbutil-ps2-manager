package com.usbutil.ps2.domain.usecase

import android.net.Uri
import androidx.work.*
import com.usbutil.ps2.data.worker.ConvertContract
import com.usbutil.ps2.data.worker.ConvertGameWorker
import com.usbutil.ps2.domain.model.Ps2Game
import com.usbutil.ps2.domain.repository.UlConfigRepository
import com.usbutil.ps2.domain.storage.UsbStorage
import java.util.UUID
import javax.inject.Inject

class AddGameFromIsoUseCase @Inject constructor(
    private val workManager: WorkManager,
) {
    operator fun invoke(isoUri: Uri, gameName: String, isDvd: Boolean): UUID {
        val request = OneTimeWorkRequestBuilder<ConvertGameWorker>()
            .setInputData(
                workDataOf(
                    ConvertContract.KEY_ISO_URI to isoUri.toString(),
                    ConvertContract.KEY_GAME_NAME to gameName,
                    ConvertContract.KEY_MEDIA_DVD to isDvd,
                )
            )
            .build()
        workManager.enqueueUniqueWork(
            ConvertContract.UNIQUE_PREFIX + gameName,
            ExistingWorkPolicy.KEEP,
            request,
        )
        return request.id
    }
}

/** الحذف المزدوج: حذف ملفات ul.* من الـ USB + إزالة القيد من ul.cfg. */
class DeleteGameUseCase @Inject constructor(
    private val usb: UsbStorage,
    private val ulConfig: UlConfigRepository,
) {
    suspend operator fun invoke(game: Ps2Game) {
        (0 until game.parts).forEach { usb.deleteFile(game.partFileName(it)) }
        ulConfig.removeGameEntry(game.gameId)
    }
}

/** تعديل بايتات الاسم فقط في ul.cfg مع إبقاء المعرف سليمًا. */
class RenameGameUseCase @Inject constructor(
    private val ulConfig: UlConfigRepository,
) {
    suspend operator fun invoke(gameId: String, newName: String) {
        require(newName.isNotBlank()) { "الاسم لا يمكن أن يكون فارغًا" }
        require(newName.toByteArray(Charsets.US_ASCII).size <= 32) {
            "الاسم يتجاوز 32 بايت المسموح بها في ul.cfg"
        }
        ulConfig.renameGame(gameId, newName.trim())
    }
}

class RecoverUlConfigUseCase @Inject constructor(
    private val ulConfig: UlConfigRepository,
) {
    suspend operator fun invoke(): List<Ps2Game> = ulConfig.rebuildFromUsb()
}
