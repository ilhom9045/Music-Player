package tj.ilhom.musicappplayer.service.model

data class MusicItem(
    var isPlay: Boolean = true,
    val id: Int,
    var musicPath: String,
    var title: String,
    var artist: String
) : java.io.Serializable