package com.example.searchwidget.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.searchwidget.R;

public class Suggestions_View_Holder extends RecyclerView.ViewHolder {

    public TextView text;
    public TextView category;
    public TextView hits;
    public ImageView searchIcon;
    public ImageView trendingIcon;

    public Suggestions_View_Holder(View itemView) {
        super(itemView);
        text = (TextView) itemView.findViewById(R.id.text);
        category = (TextView) itemView.findViewById(R.id.categoryText);
        hits = (TextView) itemView.findViewById(R.id.hits);
        searchIcon = (ImageView) itemView.findViewById(R.id.searchIcon);
        trendingIcon = (ImageView) itemView.findViewById(R.id.trending_icon);
    }
}
