package tj.ilhom.musicappplayer.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

interface TimerRepository {

    fun downTimer(
        coroutineScope: CoroutineScope,
        time: Long,
        countDownInterval: Long,
        onTick: (it: Long) -> Unit,
        onTimerEnd: () -> Unit
    ): Job

    fun upTimer(
        coroutineScope: CoroutineScope,
        time: Long,
        countUpInterval: Long,
        onTick: (it: Long) -> Unit,
        onTimerEnd: () -> Unit
    ): Job

    fun durationToTime(string: String): String

    fun timeToDuration(string: String): Long

    @Singleton
    class Base @Inject constructor() : TimerRepository {

        override fun downTimer(
            coroutineScope: CoroutineScope,
            time: Long,
            countDownInterval: Long,
            onTick: (it: Long) -> Unit,
            onTimerEnd: () -> Unit
        ): Job {
            return (time downTo 0).asSequence().asFlow().onEach {
                delay(countDownInterval)
                if (it == 0L) {
                    onTimerEnd.invoke()
                    return@onEach
                }
                onTick.invoke(it)
            }.launchIn(coroutineScope)
        }

        override fun upTimer(
            coroutineScope: CoroutineScope,
            time: Long,
            countUpInterval: Long,
            onTick: (it: Long) -> Unit,
            onTimerEnd: () -> Unit
        ): Job {
            return (0..time).asSequence().asFlow().onEach {
                delay(countUpInterval)
                if (it == time) {
                    onTimerEnd.invoke()
                    return@onEach
                }
                onTick.invoke(it)
            }.launchIn(coroutineScope)
        }

        override fun durationToTime(string: String): String {
            val dur = string.toLong()
            val second = ((dur.rem(TIME)).div(DIVIDE)).toString()
            val minute = (dur.div(TIME)).toString()

            return if (second.length == 1) {
                "0$minute:0$second"
            } else {
                "0$minute:$second"
            }
        }

        override fun timeToDuration(string: String): Long {
            val times = string.split(":")
            val second = times[1].toLong() * DIVIDE
            val minute = times[0].toLong() * TIME
            return minute + second
        }

        companion object {
            const val TIME = 60000
            const val DIVIDE = 1000
        }
    }

}