package com.example.searchwidget.Builder;

import android.util.Pair;

import com.example.searchwidget.Model.SearchPropModel;

import java.util.ArrayList;

public class SearchProp {

    public String componentId;
    public ArrayList<String> dataField;
    public String categoryField = null;
    public String defaultValue = null;
    public ArrayList<Integer> weights = null;
    public boolean autoSuggest = true;
    public ArrayList<Pair<String, String>> defaultSuggestions = null;
    public boolean highlight = false;
    public ArrayList<String> highlightField = null;
    public String queryFormat = "or";
    public String fuzziness = "0";
    public int debounce = 0;
    public boolean isAggregation = false;
    public ArrayList<String> aggregationFields = null;
    public String aggregationName = "unique-terms";

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
        this.isAggregation = isAggregration;
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

    public SearchPropModel build() {
        return new SearchPropModel(componentId, dataField, categoryField, defaultValue, weights, autoSuggest,
                defaultSuggestions, highlight, highlightField, queryFormat, fuzziness, debounce, isAggregation, aggregationFields, aggregationName);
    }
}
