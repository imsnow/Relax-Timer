package ru.elegant.relaxtimer.model.data

sealed class TimerStatus {

    object Start : TimerStatus()

    object Stop : TimerStatus()

    object DurationEnd : TimerStatus()

    object Pause : TimerStatus()

    data class Tick(val value: Long): TimerStatus()
}