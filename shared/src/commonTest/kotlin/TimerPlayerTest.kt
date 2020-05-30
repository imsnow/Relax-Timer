import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import ru.elegant.relaxtimer.model.TimerPlayer
import ru.elegant.relaxtimer.model.TimerPlayerImpl
import kotlin.test.Test
import kotlin.test.assertTrue

open class TimerPlayerTest {

    private val testScope = CoroutineScope(Job())
    private val timerPlayer: TimerPlayer = TimerPlayerImpl(testScope)

    @Test
    fun testStart() {
        timerPlayer.start {
            println("tick $it")
        }
    }
}