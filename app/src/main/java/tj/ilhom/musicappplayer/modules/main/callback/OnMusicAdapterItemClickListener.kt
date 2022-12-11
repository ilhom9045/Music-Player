package tj.ilhom.musicappplayer.modules.main.callback

import tj.ilhom.musicappplayer.modules.main.model.MusicItemDTO

interface OnMusicAdapterItemClickListener {
    fun onItemClicked(item: MusicItemDTO)
}