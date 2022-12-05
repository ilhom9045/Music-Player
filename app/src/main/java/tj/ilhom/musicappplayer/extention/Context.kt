package tj.ilhom.musicappplayer.extention

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import tj.ilhom.musicappplayer.service.MusicService
import tj.ilhom.musicappplayer.service.NotificationUtil
import tj.ilhom.musicappplayer.service.model.MusicItem

fun Context.checkReadStoragePermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Environment.isExternalStorageManager()
    } else {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val result1 = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.nextMusic() {
    sendBroadcast(newActionIntent(NotificationUtil.NEXT))
}

fun Context.previousMusic() {
    sendBroadcast(newActionIntent(NotificationUtil.PREVIOUS))
}

fun Context.playMusic() {
    sendBroadcast(newActionIntent(NotificationUtil.PLAY))
}

fun Context.playMusic(model: MusicItem) {
    val intent = Intent(this, MusicService::class.java)
    intent.putExtra(MusicService.NOTIFICATION_MODEL, model)
    startService(intent)
}