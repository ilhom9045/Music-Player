package tj.ilhom.musicappplayer.module.main.callback

import tj.ilhom.musicappplayer.module.main.model.MusicItemDTO

interface OnMusicAdapterItemClickListener {
    fun onItemClicked(item: MusicItemDTO)
}