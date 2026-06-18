package com.usbutil.ps2

import android.content.*
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.usbutil.ps2.data.storage.LibaumsUsbStorage
import com.usbutil.ps2.data.storage.SafUsbStorage
import com.usbutil.ps2.ui.theme.Ps2Theme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var safStorage: SafUsbStorage
    @Inject lateinit var libaums: LibaumsUsbStorage

    private val pickTree = registerForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { uri -> uri?.let { safStorage.onTreeSelected(it) } }

    private val pickIso = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri -> /* viewModel.onIsoPicked(it) */ }

    private val createIso = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri -> /* viewModel.onIsoDestinationPicked(it) */ }

    private val usbReceiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context, i: Intent) {
            if (i.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                libaums.onPermissionGranted()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safStorage.restorePersistedRoot()
        val flags = if (Build.VERSION.SDK_INT >= 33) RECEIVER_NOT_EXPORTED else 0
        registerReceiver(usbReceiver, IntentFilter("com.usbutil.ps2.USB_PERMISSION"), flags)

        setContent {
            Ps2Theme(darkTheme = true) { // الوضع الداكن أساسي
                // AppNavHost(...) — يربط Dashboard/Progress/Sheets
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        runCatching { unregisterReceiver(usbReceiver) }
    }
}
