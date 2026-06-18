package com.usbutil.ps2.domain.storage

import java.nio.channels.SeekableByteChannel

data class UsbFileRef(val name: String, val sizeBytes: Long, val isDirectory: Boolean)

interface UsbStorage {
    enum class AccessMode { READ, WRITE, READ_WRITE }
    suspend fun isReady(): Boolean
    suspend fun readFile(relativePath: String): ByteArray?
    suspend fun writeFile(relativePath: String, data: ByteArray)
    suspend fun deleteFile(relativePath: String): Boolean
    suspend fun listFiles(predicate: (String) -> Boolean = { true }): List<UsbFileRef>
    suspend fun openChannel(relativePath: String, mode: AccessMode): SeekableByteChannel
    suspend fun totalBytes(): Long
    suspend fun freeBytes(): Long
}
