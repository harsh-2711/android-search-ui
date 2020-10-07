package com.harsh.searchwidget.Model

/**
 * A Kotin Data class that holds data pertaining to Analytics
 * Removes verbosity introduces due to getters and setters in Java
 */

data class AnalyticsModel(var xSearchId: String?,
                     var isXSearchClick: Boolean,
                     var xSearchClickPosition: String?,
                     var isXSearchConversion: Boolean)