package com.example.searchwidget.Builder;

import android.util.Pair;

import com.example.searchwidget.Model.SearchPropModel;

import java.util.ArrayList;

public class SearchProp {

    private String componentId;
    private ArrayList<String> dataField;
    private String categoryField = null;
    private String defaultValue = null;
    private ArrayList<Integer> weights = null;
    private boolean autoSuggest = true;
    private ArrayList<Pair<String, String>> defaultSuggestions = null;
    private boolean highlight = false;
    private ArrayList<String> highlightField = null;
    private int topEntriesToHighlight = 1;
    private String queryFormat = "or";
    private String fuzziness = "0";
    private int debounce = 0;
    private boolean aggregation = false;
    private ArrayList<String> aggregationFields = null;
    private String aggregationName = "unique-terms";
    private boolean hits = false;

    public SearchProp(String componentId, ArrayList<String> dataField) {
        this.componentId = componentId;
        this.dataField = dataField;
    }

    public SearchProp setCategoryField(String categoryField) {
        this.categoryField = categoryField;
        return this;
    }

    public SearchProp setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public SearchProp setWeights(ArrayList<Integer> weights) {
        this.weights = weights;
        return this;
    }

    public SearchProp setAutoSuggest(Boolean autoSuggest) {
        this.autoSuggest = autoSuggest;
        return this;
    }

    public SearchProp setDefaultSuggestions(ArrayList<Pair<String, String>> defaultSuggestions) {
        this.defaultSuggestions = defaultSuggestions;
        return this;
    }

    public SearchProp setHighlight(Boolean highlight) {
        this.highlight = highlight;
        return this;
    }

    public SearchProp setHighlightField(ArrayList<String> highlightField) {
        this.highlightField = highlightField;
        return this;
    }

    public SearchProp setTopEntriesForHighlight(int topEntries) {
        this.topEntriesToHighlight = topEntries;
        return this;
    }

    public SearchProp setQueryFormat(String queryFormat) {
        this.queryFormat = queryFormat;
        return this;
    }

    public SearchProp setFuzziness(String fuzziness) {
        this.fuzziness = fuzziness;
        return this;
    }

    public SearchProp setDebounce(int debounce) {
        this.debounce = debounce;
        return this;
    }

    public SearchProp setAggregrationState(boolean isAggregration) {
        this.aggregation = isAggregration;
        return this;
    }

    public SearchProp setAggregationFields(ArrayList<String> aggregationFields) {
        this.aggregationFields = aggregationFields;
        return this;
    }

    public SearchProp setAggregationName(String aggregationName) {
        this.aggregationName = aggregationName;
        return this;
    }

    public SearchProp setHitsEnabled(boolean hitsEnabled) {
        this.hits = hitsEnabled;
        return this;
    }

    public SearchPropModel build() {
        return new SearchPropModel(componentId, dataField, categoryField, defaultValue, weights, autoSuggest,
                defaultSuggestions, highlight, highlightField, topEntriesToHighlight, queryFormat, fuzziness, debounce, aggregation, aggregationFields, aggregationName, hits);
    }
}
