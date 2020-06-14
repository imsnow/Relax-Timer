import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest

actual class TestUtils {
    actual class TestRunner {

        private val testCoroutineScope = TestCoroutineScope()

        actual val coroutineScope: CoroutineScope = testCoroutineScope

        actual fun <T> runTest(
            block: suspend () -> T
        ) = testCoroutineScope.runBlockingTest { block.invoke() }

        actual fun clear() = testCoroutineScope.cleanupTestCoroutines()

        actual fun advanceTimeBy(delayTimeMillis: Long) {
            testCoroutineScope.advanceTimeBy(delayTimeMillis)
        }

    }

}