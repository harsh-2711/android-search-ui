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

    /**
     * Initialises SearchProp builder
     *
     * @param componentId Unique identifier of the component
     * @param dataField Data field(s) on which the search query will be applied to
     */
    public SearchProp(String componentId, ArrayList<String> dataField) {
        this.componentId = componentId;
        this.dataField = dataField;
    }

    /**
     * Sets category field parameter
     *
     * @param categoryField Data field which has the category values mapped
     * @return
     */
    public SearchProp setCategoryField(String categoryField) {
        this.categoryField = categoryField;
        return this;
    }

    /**
     * Sets default value parameter
     *
     * @param defaultValue Sets the initial search query text on mount & the category
     * @return
     */
    public SearchProp setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /**
     * Sets weight array for data fields
     *
     * @param weights Sets the search weight for the database fields, useful when dataField is an Array of more than one field. A higher number implies a higher relevance weight for the corresponding field in the search results
     * @return
     */
    public SearchProp setWeights(ArrayList<Integer> weights) {
        this.weights = weights;
        return this;
    }

    /**
     * Sets state of auto suggest functionality
     *
     * @param autoSuggest Sets whether the autosuggest functionality should be enabled or disabled
     * @return
     */
    public SearchProp setAutoSuggest(Boolean autoSuggest) {
        this.autoSuggest = autoSuggest;
        return this;
    }

    /**
     * Sets default suggestions for search bar
     *
     * @param defaultSuggestions Preset search suggestions to be shown on focus when the search box does not have any search query text set
     * @return
     */
    public SearchProp setDefaultSuggestions(ArrayList<Pair<String, String>> defaultSuggestions) {
        this.defaultSuggestions = defaultSuggestions;
        return this;
    }

    /**
     * Sets state of highlight functionality
     *
     * @param highlight Whether highlighting should be enabled in the returned results
     * @return
     */
    public SearchProp setHighlight(Boolean highlight) {
        this.highlight = highlight;
        return this;
    }

    /**
     * Sets the fields to be returned when highlighting is on
     *
     * @param highlightField When highlighting is enabled, this prop allows specifying the fields which should be returned with the matching highlights. When not specified, it defaults to applying highlights on the field(s) specified in the dataField prop
     * @return
     */
    public SearchProp setHighlightField(ArrayList<String> highlightField) {
        this.highlightField = highlightField;
        return this;
    }

    /**
     * Sets category below the search results for given top entries
     *
     * @param topEntries Entries from top for which category is to be displayed
     * @return
     */
    public SearchProp setTopEntriesForHighlight(int topEntries) {
        this.topEntriesToHighlight = topEntries;
        return this;
    }

    /**
     * Sets the query format of the request
     *
     * @param queryFormat Sets the query format, can be "or" or "and". Defaults to "and"
     * @return
     */
    public SearchProp setQueryFormat(String queryFormat) {
        this.queryFormat = queryFormat;
        return this;
    }

    /**
     * Sets fuzziness for given query
     *
     * @param fuzziness Sets a maximum edit distance on the search parameters, can be "0", "1", "2" or “AUTO”. Useful for showing the correct results for an incorrect search parameter by taking the fuzziness into account
     * @return
     */
    public SearchProp setFuzziness(String fuzziness) {
        this.fuzziness = fuzziness;
        return this;
    }

    /**
     * Sets debounce for given query
     *
     * @param debounce sets the milliseconds to wait before executing the query. Defaults to 0, i.e. no debounce
     * @return
     */
    public SearchProp setDebounce(int debounce) {
        this.debounce = debounce;
        return this;
    }

    /**
     * Sets the state of aggregation functionality
     *
     * @param isAggregration Whether aggregation count should be done or not
     * @return
     */
    public SearchProp setAggregrationState(boolean isAggregration) {
        this.aggregation = isAggregration;
        return this;
    }

    /**
     * Sets the field on which aggregation needs to be run
     *
     * @param aggregationFields The fields on which aggregation query will be made
     * @return
     */
    public SearchProp setAggregationFields(ArrayList<String> aggregationFields) {
        this.aggregationFields = aggregationFields;
        return this;
    }

    /**
     * Sets specific name for the given aggregation query. Default name is "unique-terms"
     *
     * @param aggregationName Name of the aggregation query
     * @return
     */
    public SearchProp setAggregationName(String aggregationName) {
        this.aggregationName = aggregationName;
        return this;
    }

    /**
     * Sets state of hits functionality
     *
     * @param hitsEnabled Whether number of hits should be displayed for each search result
     * @return
     */
    public SearchProp setHitsEnabled(boolean hitsEnabled) {
        this.hits = hitsEnabled;
        return this;
    }

    /**
     * Compiles all the parameter into one SearchPropModel
     * 
     * @return Object of SearchPropModel
     */
    public SearchPropModel build() {
        return new SearchPropModel(componentId, dataField, categoryField, defaultValue, weights, autoSuggest,
                defaultSuggestions, highlight, highlightField, topEntriesToHighlight, queryFormat, fuzziness, debounce, aggregation, aggregationFields, aggregationName, hits);
    }
}
