package com.harsh.searchwidget.Builder;

import com.harsh.searchwidget.Model.ClientSuggestionsModel;
import com.harsh.searchwidget.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DefaultClientSuggestions {

    ArrayList<String> suggestions;
    ArrayList<HashMap<String, ArrayList<String>>> extraProperties = null;
    ArrayList<String> categories = null;
    ArrayList<Boolean> categoricalSearch = null;
    ArrayList<Integer> searchIcon = null;
    ArrayList<Integer> trendingIcon = null;
    ArrayList<String> hits = null;

    /**
     * Builder for single suggestion entry
     *
     * @param suggestions List of suggestions
     */
    public DefaultClientSuggestions(ArrayList<String> suggestions) {
        this.suggestions = suggestions;
    }

    /**
     * Sets values for extra fields passed in key-value format
     *
     * @param extraProperties Values for given extra fields in key-value format using Hashmap data structure
     * @return
     */
    public DefaultClientSuggestions setExtraProperties(ArrayList<HashMap<String, ArrayList<String>>> extraProperties) {
        this.extraProperties = extraProperties;
        return this;
    }

    /**
     * Sets categories to be shown below search results
     *
     * @param categories List of categories
     * @return
     */
    public DefaultClientSuggestions setCategories(ArrayList<String> categories) {
        this.categories = categories;
        return this;
    }

    public DefaultClientSuggestions setCategoricalSearch(ArrayList<Boolean> categoricalSearch) {
        this.categoricalSearch = categoricalSearch;
        return this;
    }

    /**
     * Sets images or icons for suggestions
     *
     * @param searchIcon List of resource ids for each suggestion
     * @return
     */
    public DefaultClientSuggestions setSearchImages(ArrayList<Integer> searchIcon) {
        this.searchIcon = searchIcon;
        return this;
    }

    /**
     * Sets extra trending images or icons for suggestions - The rightmost icon
     *
     * @param trendingIcon List of resource ids for each suggestion
     * @return
     */
    public DefaultClientSuggestions setIcons(ArrayList<Integer> trendingIcon) {
        this.trendingIcon = trendingIcon;
        return this;
    }

    /**
     * Sets the hit count for each suggestion
     *
     * @param hits List of hits
     * @return
     */
    public DefaultClientSuggestions hits(ArrayList<String> hits) {
        this.hits = hits;
        return this;
    }

    /**
     * Binds all the parameter of Suggestion Builder and makes a list of SuggestionModel items
     *
     * @return List of items of SuggestionModel class
     */
    public ArrayList<ClientSuggestionsModel> build() {

        ArrayList<ClientSuggestionsModel> suggestions = new ArrayList<>();
        String category;
        boolean isCategoricalSearch;
        String hit;
        int searchicon;
        int trendingicon;
        HashMap<String, ArrayList<String>> extraProperty;

        for(int i = 0; i < this.suggestions.size(); i++) {

            if(this.categories == null)
                category = null;
            else {
                if(this.categories.size() > i) {
                    category = this.categories.get(i);
                }
                else
                    category = null;
            }

            if(this.categoricalSearch == null)
                isCategoricalSearch = false;
            else {
                if(this.categoricalSearch.size() > i)
                    isCategoricalSearch = this.categoricalSearch.get(i);
                else
                    isCategoricalSearch = false;
            }

            if(this.hits == null)
                hit = null;
            else {
                if(this.hits.size() > i) {
                    hit = this.hits.get(i);
                }
                else
                    hit = null;
            }

            if(this.searchIcon == null)
                searchicon = R.drawable.ic_search_icon;
            else {
                if(this.searchIcon.size() > i) {
                    searchicon = this.searchIcon.get(i);
                }
                else
                    searchicon = R.drawable.ic_search_icon;
            }

            if(this.trendingIcon == null)
                trendingicon = R.drawable.top_left_arrow;
            else {
                if(this.trendingIcon.size() > i) {
                    trendingicon = this.trendingIcon.get(i);
                }
                else
                    trendingicon = R.drawable.top_left_arrow;
            }

            if(this.extraProperties == null)
                extraProperty = null;
            else {
                if(this.extraProperties.size() > i) {
                    extraProperty = this.extraProperties.get(i);
                }
                else
                    extraProperty = null;
            }

            suggestions.add(new ClientSuggestionsModel(this.suggestions.get(i), category, isCategoricalSearch, hit, searchicon, trendingicon, extraProperty));
        }

        return suggestions;
    }
}
