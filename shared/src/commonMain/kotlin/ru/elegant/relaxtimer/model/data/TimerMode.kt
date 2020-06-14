package ru.elegant.relaxtimer.model.data

enum class TimerMode {
    STRICT, // строгий режим - таймер останавливается, когда законичлось время тика
    INFINITY // бесконечный режим - таймер не останавливается, когда законичлось время тика. Только через стоп
}