package com.example.searchwidget.Model;


public class SuggestionsModel {

    private String text;
    private String category;
    private String hits;
    private int searchIcon;
    private int trendingIcon;

    public SuggestionsModel(String text, String category, String hits, int searchIcon, int trendingIcon) {
        this.text = text;
        this.category = category;
        this.hits = hits;
        this.searchIcon = searchIcon;
        this.trendingIcon = trendingIcon;
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
}
