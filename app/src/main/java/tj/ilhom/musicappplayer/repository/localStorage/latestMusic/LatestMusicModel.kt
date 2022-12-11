package tj.ilhom.musicappplayer.repository.localStorage.latestMusic

data class LatestMusicModel(

    var isPlay: Boolean = true,
    val id: Int,
    var musicPath: String,
    var title: String,
    var artist: String,
    var duration: String,
    var stopedPosition: Int
):java.io.Serializable
