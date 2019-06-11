package com.example.searchwidget.Model;

import android.util.Pair;

import java.util.ArrayList;

public class SearchPropModel {

    private String componentId;
    private ArrayList<String> dataField;
    private String categoryField;
    private String title;
    private String defaultValue;
    private ArrayList<Integer> weights;
    private String placeholder;
    private boolean autoSuggest;
    private ArrayList<Pair<String, String>> defaultSuggestions;
    private boolean highlight;
    private String highlightField;
    private String queryFormat;
    private String fuzziness;
    private int debounce;
    private boolean isAggregration;
    private ArrayList<String> aggregrationFields;
    private String aggregrationName;

    public SearchPropModel(String componentId, ArrayList<String> dataField, String categoryField, String title,
                           String defaultValue, ArrayList<Integer> weights, String placeholder,
                           boolean autoSuggest, ArrayList<Pair<String, String>> defaultSuggestions, boolean highlight,
                           String highlightField, String queryFormat, String fuzziness, int debounce, boolean isAggregration,
                           ArrayList<String> aggregrationFields, String aggregrationName) {

        this.componentId = componentId;
        this.dataField = dataField;
        this.categoryField = categoryField;
        this.title = title;
        this.defaultValue = defaultValue;
        this.weights = weights;
        this.placeholder = placeholder;
        this.autoSuggest = autoSuggest;
        this.defaultSuggestions = defaultSuggestions;
        this.highlight = highlight;
        this.highlightField = highlightField;
        this.queryFormat = queryFormat;
        this.fuzziness = fuzziness;
        this.debounce = debounce;
        this.isAggregration = isAggregration;
        this.aggregrationFields = aggregrationFields;
        this.aggregrationName = aggregrationName;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getPlaceholer() {
        return placeholder;
    }

    public void setPlaceholer(String placeholer) {
        this.placeholder = placeholer;
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

    public String getHighlightField() {
        return highlightField;
    }

    public void setHighlightField(String highlightField) {
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
        return isAggregration;
    }

    public void setAggregrationState(boolean aggregrationState) {
        this.isAggregration = aggregrationState;
    }

    public ArrayList<String> getAggregrationFields() {
        return aggregrationFields;
    }

    public void setAggregrationFields(ArrayList<String> aggregrationFields) {
        this.aggregrationFields = aggregrationFields;
    }

    public String getAggregrationName() {
        return aggregrationName;
    }

    public void setAggregrationName(String aggregrationName) {
        this.aggregrationName = aggregrationName;
    }
}
