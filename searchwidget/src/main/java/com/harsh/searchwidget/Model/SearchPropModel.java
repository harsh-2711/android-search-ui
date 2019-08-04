package com.harsh.searchwidget.Model;

import java.util.ArrayList;

public class SearchPropModel {

    private String componentId;
    private ArrayList<String> dataField;
    private ArrayList<String> extraFields;
    private String categoryField;
    private boolean inPlaceCategory;
    private String defaultValue;
    private ArrayList<Integer> weights;
    private boolean autoSuggest;
    private ArrayList<ClientSuggestionsModel> defaultSuggestions;
    private boolean highlight;
    private ArrayList<String> highlightField;
    private int topEntries;
    private String queryFormat;
    private String fuzziness;
    private int debounce;
    private boolean aggregation;
    private ArrayList<String> aggregationFields;
    private String aggregationName;
    private boolean hits;
    private boolean searchResultImage;
    private boolean redirectIcon;

    public SearchPropModel(String componentId, ArrayList<String> dataField, ArrayList<String> extraFields, String categoryField, boolean inPlaceCategory,
                           String defaultValue, ArrayList<Integer> weights, boolean autoSuggest, ArrayList<ClientSuggestionsModel> defaultSuggestions,
                           boolean highlight, ArrayList<String> highlightField, int topEntries, String queryFormat, String fuzziness,
                           int debounce, boolean aggregation, ArrayList<String> aggregationFields, String aggregationName,
                           boolean hits, boolean searchResultImage, boolean redirectIcon) {

        this.componentId = componentId;
        this.dataField = dataField;
        this.extraFields = extraFields;
        this.categoryField = categoryField;
        this.inPlaceCategory = inPlaceCategory;
        this.defaultValue = defaultValue;
        this.weights = weights;
        this.autoSuggest = autoSuggest;
        this.defaultSuggestions = defaultSuggestions;
        this.highlight = highlight;
        this.highlightField = highlightField;
        this.topEntries = topEntries;
        this.queryFormat = queryFormat;
        this.fuzziness = fuzziness;
        this.debounce = debounce;
        this.aggregation = aggregation;
        this.aggregationFields = aggregationFields;
        this.aggregationName = aggregationName;
        this.hits = hits;
        this.searchResultImage = searchResultImage;
        this.redirectIcon = redirectIcon;
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

    public ArrayList<String> getExtraFields() {
        return extraFields;
    }

    public void setExtraFields(ArrayList<String> extraFields) {
        this.extraFields = extraFields;
    }

    public String getCategoryField() {
        return categoryField;
    }

    public void setCategoryField(String categoryField) {
        this.categoryField = categoryField;
    }

    public boolean isInPlaceCategory() {
        return inPlaceCategory;
    }

    public void setInPlaceCategory(boolean inPlaceCategory) {
        this.inPlaceCategory = inPlaceCategory;
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

    public ArrayList<ClientSuggestionsModel> getDefaultSuggestions() {
        return defaultSuggestions;
    }

    public void setDefaultSuggestions(ArrayList<ClientSuggestionsModel> defaultSuggestions) {
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

    public int getTopEntries() {
        return topEntries;
    }

    public void setTopEntries(int topEntries) {
        this.topEntries = topEntries;
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

    public boolean isSearchResultImage() {
        return searchResultImage;
    }

    public void setSearchResultImage(boolean searchResultImage) {
        this.searchResultImage = searchResultImage;
    }

    public boolean isRedirectIcon() {
        return redirectIcon;
    }

    public void setRedirectIcon(boolean redirectIcon) {
        this.redirectIcon = redirectIcon;
    }
}
