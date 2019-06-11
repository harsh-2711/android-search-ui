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
    public String highlightField = null;
    public String queryFormat = "or";
    public String fuzziness = "0";
    public int debounce = 0;
    public boolean isAggregration = false;
    public ArrayList<String> aggregrationFields = null;
    public String aggregrationName = "unique-terms";

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

    public SearchProp setHighlightField(String highlightField) {
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
        this.isAggregration = isAggregration;
        return this;
    }

    public SearchProp setAggregrationFields(ArrayList<String> aggregrationFields) {
        this.aggregrationFields = aggregrationFields;
        return this;
    }

    public SearchProp setAggregrationName(String aggregrationName) {
        this.aggregrationName = aggregrationName;
        return this;
    }

    public SearchPropModel build() {
        return new SearchPropModel(componentId, dataField, categoryField, defaultValue, weights, autoSuggest,
                defaultSuggestions, highlight, highlightField, queryFormat, fuzziness, debounce, isAggregration, aggregrationFields, aggregrationName);
    }
}
