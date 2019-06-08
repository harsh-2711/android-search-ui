package com.example.searchwidget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SearchBar extends RelativeLayout {

    public SearchBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public SearchBar(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init(attributeSet);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchBar(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        super(context, attributeSet, defStyleAttr, defStyleRes);
        init(attributeSet);
    }

    private void init(AttributeSet attributeSet) {
        inflate(getContext(), R.layout.search_bar, this);
    }
}
