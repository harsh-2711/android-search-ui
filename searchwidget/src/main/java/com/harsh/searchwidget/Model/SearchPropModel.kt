package com.harsh.searchwidget.Model

import java.util.*

/**
 * A Kotlin Data class that holds properties of Searches
 */

data class SearchPropModel(
        var componentId: String,
        var dataField: ArrayList<String>,
        var extraFields: ArrayList<String>?,
        var categoryField: String?,
        var isInPlaceCategory: Boolean,
        var defaultValue: String?,
        var weights: ArrayList<Int>?,
        var isAutoSuggest: Boolean,
        var defaultSuggestions: ArrayList<ClientSuggestionsModel>?,
        var isHighlight: Boolean,
        var highlightField: ArrayList<String>?,
        var topEntries: Int,
        var queryFormat: String,
        var fuzziness: String?,
        var debounce: Int,
        var aggregationState: Boolean,
        var aggregationFields: ArrayList<String>,
        var aggregationName: String,
        var hitsState: Boolean,
        var isSearchResultImage: Boolean,
        var isRedirectIcon: Boolean
)
