package com.harsh.searchwidget.Model

import java.util.*

/**
 * A Kotlin Data class that holds data pertaining to ClientSuggestions
 */

data class ClientSuggestionsModel(
        var text: String,
        var category: String?,
        var isCategoricalSearch: Boolean,
        var hits: String?,
        var searchIcon: Int,
        var trendingIcon: Int,
        var extraProperties: HashMap<String, ArrayList<String>>?
)
