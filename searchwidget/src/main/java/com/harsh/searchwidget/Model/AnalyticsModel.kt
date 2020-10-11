package com.harsh.searchwidget.Model

/**
 * A Kotlin Data class that holds properties of Searches
 */

data class AnalyticsModel(
        var xSearchId: String?,
        var isXSearchClick: Boolean,
        var xSearchClickPosition: String?,
        var isXSearchConversion: Boolean
)
