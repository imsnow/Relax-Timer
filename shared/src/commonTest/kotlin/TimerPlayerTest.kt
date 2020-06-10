import ru.elegant.relaxtimer.model.TimerPlayer
import ru.elegant.relaxtimer.model.TimerPlayerImpl
import kotlin.test.Test

open class TimerPlayerTest(private val testRunner: TestUtils.TestRunner) {

    private val timerPlayer: TimerPlayer = TimerPlayerImpl(testRunner.coroutineScope)

    @Test
    fun testStart() {
        println("hello test")
        timerPlayer.start {
            println("tick $it")
        }
        testRunner.advanceTimeBy(4000)
    }
}