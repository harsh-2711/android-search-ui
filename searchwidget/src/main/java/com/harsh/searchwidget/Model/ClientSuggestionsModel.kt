package com.harsh.searchwidget.Model

import java.util.*

/**
 * A Kotin Data class that holds data pertaining to ClientSuggestions
 * Removes verbosity introduces due to getters and setters in Java
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