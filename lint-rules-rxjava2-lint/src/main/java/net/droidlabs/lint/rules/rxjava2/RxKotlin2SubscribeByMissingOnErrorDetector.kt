package net.droidlabs.lint.rules.rxjava2

import com.android.tools.lint.client.api.JavaEvaluator
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Severity.ERROR
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.util.*

val ISSUE_SUBSCRIBE_MISSING_ON_ERROR = Issue.create(
    "RxJava2SubscribeMissingOnError",
    "Flags a version of the subscribeBy() method without onError defined.",
    "When calling the subscribeBy() method an onError should be defined. Otherwise errors might be thrown and may crash the application or get forwarded to the Plugin Error handler.",
    CORRECTNESS, PRIORITY, ERROR,
    Implementation(RxKotlin2SubscribeByMissingOnErrorDetector::class.java, EnumSet.of(JAVA_FILE))
)

class RxKotlin2SubscribeByMissingOnErrorDetector : Detector(), Detector.UastScanner {
    override fun getApplicableMethodNames() = listOf("subscribeBy")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if ("subscribeBy" == method.name && isReactiveType(context.evaluator, method)) {
            context.report(
                ISSUE_SUBSCRIBE_MISSING_ON_ERROR,
                node,
                context.getNameLocation(node),
                "Using a version of subscribe() without an error Consumer."
            )
        }
    }

    private fun isReactiveType(evaluator: JavaEvaluator, method: PsiMethod): Boolean {
        return REACTIVE_TYPES.any { evaluator.isMemberInClass(method, it) }
    }

    companion object {
        private val OBSERVABLE = "io.reactivex.Observable"
        private val FLOWABLE = "io.reactivex.Flowable"
        private val SINGLE = "io.reactivex.Single"
        private val COMPLETABLE = "io.reactivex.Completable"
        private val MAYBE = "io.reactivex.Maybe"

        private val REACTIVE_TYPES = setOf(OBSERVABLE, FLOWABLE, SINGLE, MAYBE, COMPLETABLE)
    }
}