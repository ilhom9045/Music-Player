package tj.ilhom.musicappplayer.extention

import com.ilhom.core.localStorage.latestMusic.LatestMusicModel
import tj.ilhom.musicappplayer.service.model.MusicItem

fun MusicItem.toLatestMusicModel(durationPosition: Int): com.ilhom.core.localStorage.latestMusic.LatestMusicModel {
    return com.ilhom.core.localStorage.latestMusic.LatestMusicModel(
        isPlay = this.isPlay,
        id = this.id,
        musicPath = this.musicPath,
        title = this.title,
        artist = this.artist,
        duration = this.duration,
        stopedPosition = durationPosition
    )
}