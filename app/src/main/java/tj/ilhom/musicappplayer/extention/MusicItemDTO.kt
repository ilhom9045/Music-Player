package tj.ilhom.musicappplayer.extention

import tj.ilhom.musicappplayer.module.main.model.MusicItemDTO
import tj.ilhom.musicappplayer.service.model.MusicItem

fun MusicItemDTO.toMusicItem(): MusicItem {
    return MusicItem(
        id = this.id,
        musicPath = this.path,
        title = this.name,
        artist = this.artist ?: "",
        duration = this.duration
    )
}