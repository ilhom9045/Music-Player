package tj.ilhom.musicappplayer.extention

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import tj.ilhom.musicappplayer.service.MusicNotificationBroadcast
import tj.ilhom.musicappplayer.service.MusicService
import tj.ilhom.musicappplayer.service.model.MusicItem

fun Context.newActionIntent(action: String): Intent {
    return Intent(this, MusicNotificationBroadcast::class.java).setAction(
        action
    )
}

fun Context.newPendingIntent(intent: Intent): PendingIntent {
    val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
    return PendingIntent.getBroadcast(this, 0, intent, flag)
}

fun Context.startMusicService(model: MusicItem) {
    val intent = Intent(this, MusicService::class.java)
    intent.putExtra(MusicService.NOTIFICATION_MODEL, model)
    startService(intent)
}
