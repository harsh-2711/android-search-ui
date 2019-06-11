package com.example.searchwidget.Builder;

import android.util.Pair;

import com.example.searchwidget.Model.SearchPropModel;

import java.util.ArrayList;

public class SearchProp {

    private String componentId;
    private ArrayList<String> dataField;
    private String categoryField = null;
    private String title = null;
    private ArrayList<Pair<String, String>> defaultValue = null;
    private ArrayList<Integer> weights = null;
    private String placeholder = "Search";
    private boolean autoSuggest = true;
    private ArrayList<Pair<String, String>> defaultSuggestions = null;
    private boolean highlight = false;
    private String highlightField = null;
    private String queryFormat = "or";
    private String fuzziness = "0";
    private int debounce = 0;

    public SearchProp(String componentId, ArrayList<String> dataField) {
        this.componentId = componentId;
        this.dataField = dataField;
    }

    SearchProp setCategoryField(String categoryField) {
        this.categoryField = categoryField;
        return this;
    }

    SearchProp setTitle(String title) {
        this.title = title;
        return this;
    }

    SearchProp setDefaultValue(ArrayList<Pair<String, String>> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    SearchProp setWeights(ArrayList<Integer> weights) {
        this.weights = weights;
        return this;
    }

    SearchProp setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    SearchProp setAutoSuggest(Boolean autoSuggest) {
        this.autoSuggest = autoSuggest;
        return this;
    }

    SearchProp setDefaultSuggestions(ArrayList<Pair<String, String>> defaultSuggestions) {
        this.defaultSuggestions = defaultSuggestions;
        return this;
    }

    SearchProp setHighlight(Boolean highlight) {
        this.highlight = highlight;
        return this;
    }

    SearchProp setHighlightField(String highlightField) {
        this.highlightField = highlightField;
        return this;
    }

    SearchProp setQueryFormat(String queryFormat) {
        this.queryFormat = queryFormat;
        return this;
    }

    SearchProp setFuzziness(String fuzziness) {
        this.fuzziness = fuzziness;
        return this;
    }

    SearchProp setDebounce(int debounce) {
        this.debounce = debounce;
        return this;
    }

    SearchPropModel build() {
        return new SearchPropModel(componentId, dataField, categoryField, title, defaultValue, weights, placeholder, autoSuggest,
                defaultSuggestions, highlight, highlightField, queryFormat, fuzziness, debounce);
    }
}
