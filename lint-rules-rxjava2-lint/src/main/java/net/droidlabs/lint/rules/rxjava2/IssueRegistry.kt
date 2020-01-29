package net.droidlabs.lint.rules.rxjava2

import com.android.tools.lint.detector.api.CURRENT_API

internal const val PRIORITY = 10

class IssueRegistry : com.android.tools.lint.client.api.IssueRegistry() {
    override val api = CURRENT_API

    override val issues get() = listOf(
        ISSUE_SUBSCRIBE_MISSING_ON_ERROR
    )
}
