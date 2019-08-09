package com.harsh.searchwidget.Model;


import java.util.ArrayList;
import java.util.HashMap;

public class ClientSuggestionsModel {

    private String text;
    private String category;
    private boolean isCategoricalSearch;
    private String hits;
    private int searchIcon;
    private int trendingIcon;
    private HashMap<String, ArrayList<String>> extraProperties;

    public ClientSuggestionsModel(String text, String category, boolean isCategoricalSearch, String hits, int searchIcon, int trendingIcon, HashMap<String, ArrayList<String>> extraProperties) {
        this.text = text;
        this.category = category;
        this.isCategoricalSearch = isCategoricalSearch;
        this.hits = hits;
        this.searchIcon = searchIcon;
        this.trendingIcon = trendingIcon;
        this.extraProperties = extraProperties;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isCategoricalSearch() {
        return isCategoricalSearch;
    }

    public void setCategoricalSearch(boolean categoricalSearch) {
        isCategoricalSearch = categoricalSearch;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public int getSearchIcon() {
        return searchIcon;
    }

    public void setSearchIcon(int searchIcon) {
        this.searchIcon = searchIcon;
    }

    public int getTrendingIcon() {
        return trendingIcon;
    }

    public void setTrendingIcon(int trendingIcon) {
        this.trendingIcon = trendingIcon;
    }

    public HashMap<String, ArrayList<String>> getExtraProperties() {
        return extraProperties;
    }

    public void setExtraProperties(HashMap<String, ArrayList<String>> extraProperties) {
        this.extraProperties = extraProperties;
    }
}
