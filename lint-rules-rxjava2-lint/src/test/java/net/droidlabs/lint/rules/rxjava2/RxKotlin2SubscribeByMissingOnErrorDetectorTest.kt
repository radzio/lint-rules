package net.droidlabs.lint.rules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles
import com.android.tools.lint.checks.infrastructure.TestLintTask
import org.junit.Test

class RxKotlin2SubscribeByMissingOnErrorDetectorTest {

    private val rxJava2 = TestFiles.bytes(
        "libs/rxjava-2.1.7.jar",
        javaClass.getResourceAsStream("/rxjava-2.1.7.jar").readBytes()
    )

    private val rxKotlin = TestFiles.bytes(
        "libs/rxkotlin-2.4.0.jar",
        javaClass.getResourceAsStream("/rxkotlin-2.4.0.jar").readBytes()
    )

    @Test
    fun callingObservableSubscribeBy() {
        TestLintTask.lint()
            .files(
                rxJava2, rxKotlin, TestFiles.kt(
                    """
          package foo
          import io.reactivex.rxkotlin.subscribeBy
          import io.reactivex.Observable

          class Example {
           fun foo() {
              Observable.just("test").subscribeBy()
            }
          }"""
                ).indented()
            )
            .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
            .run()
            .expect("""
              |src/foo/Example.kt:7: Error: Using a version subscribeBy() without onError defined. [RxKotlin2SubscribeByMissingOnError]
              |    Observable.just("test").subscribeBy()
              |                            ~~~~~~~~~~~
              |1 errors, 0 warnings""".trimMargin()
            )
    }

    @Test
    fun callingMaybeSubscribeBy() {
        TestLintTask.lint()
            .files(
                rxJava2, rxKotlin, TestFiles.kt(
                    """
          package foo
          import io.reactivex.rxkotlin.subscribeBy
          import io.reactivex.Maybe

          class Example {
           fun foo() {
              Maybe.just("test").subscribeBy()
            }
          }"""
                ).indented()
            )
            .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
            .run()
            .expect("""
              |src/foo/Example.kt:7: Error: Using a version subscribeBy() without onError defined. [RxKotlin2SubscribeByMissingOnError]
              |    Maybe.just("test").subscribeBy()
              |                       ~~~~~~~~~~~
              |1 errors, 0 warnings""".trimMargin()
            )
    }
}