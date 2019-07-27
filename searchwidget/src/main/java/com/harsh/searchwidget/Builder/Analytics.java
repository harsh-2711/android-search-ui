package com.harsh.searchwidget.Builder;

import com.harsh.searchwidget.Model.AnalyticsModel;

public class Analytics {

    private String XSearchId = null;
    private boolean XSearchClick = false;
    private String XSearchClickPosition = null;
    private boolean XSearchConversion = false;

    /**
     * Initialises Analytics Builder
     */
    public Analytics() {

    }

    /**
     * Sets X-Search-Id property
     *
     * @param XSearchId Search Id
     * @return
     */
    public Analytics setXSearchId(String XSearchId) {
        this.XSearchId = XSearchId;
        return this;
    }

    /**
     * Sets X-Search-Click property
     *
     * @param XSearchClick State of X-Search-Click to be considered
     * @return
     */
    public Analytics setXSearchClick(boolean XSearchClick) {
        this.XSearchClick = XSearchClick;
        return this;
    }

    /**
     * Sets X-Search-ClickPosition property
     *
     * @param XSearchClickPosition Click position of the search item to be considered
     * @return
     */
    public Analytics setXSearchClickPosition(String XSearchClickPosition) {
        this.XSearchClickPosition = XSearchClickPosition;
        return this;
    }

    /**
     * Sets X-Search-Conversion property
     *
     * @param XSearchConversion State of X-Search-Conversion to be considered
     * @return
     */
    public Analytics setXSearchConversion(boolean XSearchConversion) {
        this.XSearchConversion = XSearchConversion;
        return this;
    }

    /**
     * Compiles all parameter into an AnalyticsModel
     *
     * @return Object of AnalyticsModel
     */
    public AnalyticsModel build() {
        return new AnalyticsModel(XSearchId, XSearchClick, XSearchClickPosition, XSearchConversion);
    }
}
