package com.example.searchwidget.Model;

import android.util.Pair;

import java.util.ArrayList;

public class SearchPropModel {

    private String componentId;
    private ArrayList<String> dataField;
    private String categoryField;
    private String defaultValue;
    private ArrayList<Integer> weights;
    private boolean autoSuggest;
    private ArrayList<Pair<String, String>> defaultSuggestions;
    private boolean highlight;
    private ArrayList<String> highlightField;
    private String queryFormat;
    private String fuzziness;
    private int debounce;
    private boolean aggregation;
    private ArrayList<String> aggregationFields;
    private String aggregationName;
    private boolean hits;

    public SearchPropModel(String componentId, ArrayList<String> dataField, String categoryField, String defaultValue,
                           ArrayList<Integer> weights, boolean autoSuggest, ArrayList<Pair<String, String>> defaultSuggestions,
                           boolean highlight, ArrayList<String> highlightField, String queryFormat, String fuzziness, int debounce,
                           boolean aggregation, ArrayList<String> aggregationFields, String aggregationName, boolean hits) {

        this.componentId = componentId;
        this.dataField = dataField;
        this.categoryField = categoryField;
        this.defaultValue = defaultValue;
        this.weights = weights;
        this.autoSuggest = autoSuggest;
        this.defaultSuggestions = defaultSuggestions;
        this.highlight = highlight;
        this.highlightField = highlightField;
        this.queryFormat = queryFormat;
        this.fuzziness = fuzziness;
        this.debounce = debounce;
        this.aggregation = aggregation;
        this.aggregationFields = aggregationFields;
        this.aggregationName = aggregationName;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public ArrayList<String> getDataField() {
        return dataField;
    }

    public void setDataField(ArrayList<String> dataField) {
        this.dataField = dataField;
    }

    public String getCategoryField() {
        return categoryField;
    }

    public void setCategoryField(String categoryField) {
        this.categoryField = categoryField;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public ArrayList<Integer> getWeights() {
        return weights;
    }

    public void setWeights(ArrayList<Integer> weights) {
        this.weights = weights;
    }

    public boolean isAutoSuggest() {
        return autoSuggest;
    }

    public void setAutoSuggest(boolean autoSuggest) {
        this.autoSuggest = autoSuggest;
    }

    public ArrayList<Pair<String, String>> getDefaultSuggestions() {
        return defaultSuggestions;
    }

    public void setDefaultSuggestions(ArrayList<Pair<String, String>> defaultSuggestions) {
        this.defaultSuggestions = defaultSuggestions;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public ArrayList<String> getHighlightField() {
        return highlightField;
    }

    public void setHighlightField(ArrayList<String> highlightField) {
        this.highlightField = highlightField;
    }

    public String getQueryFormat() {
        return queryFormat;
    }

    public void setQueryFormat(String queryFormat) {
        this.queryFormat = queryFormat;
    }

    public String getFuzziness() {
        return fuzziness;
    }

    public void setFuzziness(String fuzziness) {
        this.fuzziness = fuzziness;
    }

    public int getDebounce() {
        return debounce;
    }

    public void setDebounce(int debounce) {
        this.debounce = debounce;
    }

    public boolean getAggregrationState() {
        return aggregation;
    }

    public void setAggregrationState(boolean aggregationState) {
        this.aggregation = aggregationState;
    }

    public ArrayList<String> getAggregationFields() {
        return aggregationFields;
    }

    public void setAggregationFields(ArrayList<String> aggregationFields) {
        this.aggregationFields = aggregationFields;
    }

    public String getAggregationName() {
        return aggregationName;
    }

    public void setAggregationName(String aggregationName) {
        this.aggregationName = aggregationName;
    }

    public boolean getHitsState() {
        return hits;
    }

    public void setHitsState(boolean hits) {
        this.hits = hits;
    }
}
