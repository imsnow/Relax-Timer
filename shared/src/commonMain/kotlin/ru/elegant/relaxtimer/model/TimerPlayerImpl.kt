package ru.elegant.relaxtimer.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerPlayerImpl(
    private val scope: CoroutineScope
) : TimerPlayer, CoroutineScope by scope {

    override fun start(onTick: (Long) -> Unit) {
        val startValue = 1000L
        val tick = 100L
        println("hello start $scope")
        val job = launch {
            println("start time")
            repeat((startValue/tick).toInt()) {
                delay(tick)
                onTick(it.toLong())
            }
            println("end time")
        }
        println("hello job $job")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}