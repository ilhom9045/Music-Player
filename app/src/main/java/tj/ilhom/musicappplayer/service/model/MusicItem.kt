package tj.ilhom.musicappplayer.service.model

data class MusicItem(
    var isPlay: Boolean = true,
    val id: Int,
    var musicPath: String,
    var title: String,
    var artist: String,
    var duration: String
) : java.io.Serializable