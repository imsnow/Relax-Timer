package ru.elegant.relaxtimer.model

interface TimerPlayer {

    fun start(onTick: (Long) -> Unit)

    fun pause()

    fun stop()
}