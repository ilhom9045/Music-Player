package tj.ilhom.musicappplayer.service.model

data class NotificationActionModel(
    var isPlay: Boolean = false,
    var isNext: Boolean = false,
    var isPrevious: Boolean = false,
    var isFavorite: Boolean = false,
    var isReplay: Boolean = false,
    var path: String
) : java.io.Serializable