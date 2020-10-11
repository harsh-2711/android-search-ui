package com.harsh.searchwidget.Model

/**
 * A Kotlin Data class that holds data pertaining to Analytics
 */

data class AnalyticsModel(
        var xSearchId: String?,
        var isXSearchClick: Boolean,
        var xSearchClickPosition: String?,
        var isXSearchConversion: Boolean
)
