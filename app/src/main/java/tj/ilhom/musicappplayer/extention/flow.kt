package tj.ilhom.musicappplayer.extention

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

suspend fun <T> Flow<T>.await(): T = first()