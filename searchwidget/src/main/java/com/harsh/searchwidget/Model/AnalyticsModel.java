package com.harsh.searchwidget.Model;

public class AnalyticsModel {

    private String XSearchId;
    private boolean XSearchClick;
    private String XSearchClickPosition;
    private boolean XSearchConversion;

    public AnalyticsModel(String XSearchId, boolean XSearchClick, String XSearchClickPosition, boolean XSearchConversion) {
        this.XSearchId = XSearchId;
        this.XSearchClick = XSearchClick;
        this.XSearchClickPosition = XSearchClickPosition;
        this.XSearchConversion = XSearchConversion;
    }

    public String getXSearchId() {
        return XSearchId;
    }

    public void setXSearchId(String XSearchId) {
        this.XSearchId = XSearchId;
    }

    public boolean isXSearchClick() {
        return XSearchClick;
    }

    public void setXSearchClick(boolean XSearchClick) {
        this.XSearchClick = XSearchClick;
    }

    public String getXSearchClickPosition() {
        return XSearchClickPosition;
    }

    public void setXSearchClickPosition(String XSearchClickPosition) {
        this.XSearchClickPosition = XSearchClickPosition;
    }

    public boolean isXSearchConversion() {
        return XSearchConversion;
    }

    public void setXSearchConversion(boolean XSearchConversion) {
        this.XSearchConversion = XSearchConversion;
    }
}
