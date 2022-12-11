package tj.ilhom.musicappplayer.extention

import tj.ilhom.musicappplayer.repository.localStorage.latestMusic.LatestMusicModel
import tj.ilhom.musicappplayer.service.model.MusicItem

fun MusicItem.toLatestMusicModel(durationPosition: Int): LatestMusicModel {
    return LatestMusicModel(
        isPlay = this.isPlay,
        id = this.id,
        musicPath = this.musicPath,
        title = this.title,
        artist = this.artist,
        duration = this.duration,
        stopedPosition = durationPosition
    )
}