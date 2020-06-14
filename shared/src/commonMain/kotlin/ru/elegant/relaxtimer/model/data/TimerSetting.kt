package ru.elegant.relaxtimer.model.data

data class TimerSetting(
    val duration: Long,
    val tickTime: Long,
    val mode: TimerMode
)