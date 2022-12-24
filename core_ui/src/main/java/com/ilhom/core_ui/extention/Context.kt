package com.ilhom.core_ui.extention

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import tj.ilhom.musicappplayer.service.MusicService
import tj.ilhom.musicappplayer.service.NotificationUtil
import tj.ilhom.musicappplayer.service.model.MusicItem

fun Context.checkReadStoragePermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
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

fun Context.stopMusic() {
    sendBroadcast(newActionIntent(NotificationUtil.STOP))
}

fun Context.playMusic(model: MusicItem) {
    val intent = Intent(this, MusicService::class.java)
    intent.putExtra(MusicService.NOTIFICATION_MODEL, model)
    startService(intent)
}