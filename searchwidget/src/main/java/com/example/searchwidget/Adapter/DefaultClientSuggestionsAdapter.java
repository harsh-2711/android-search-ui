package com.example.searchwidget.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.searchwidget.Model.SuggestionsModel;
import com.example.searchwidget.R;
import com.example.searchwidget.View.Suggestions_View_Holder;

import java.util.ArrayList;

public class DefaultClientSuggestionsAdapter extends RecyclerView.Adapter<Suggestions_View_Holder> {
    private ArrayList<SuggestionsModel> suggestions;
    private Context context;
    private int topEntries;
    private boolean shouldHighlight;
    private String queryText;
    private boolean showHits;

    /**
     * Listener for click on recycler view's items
     */
    public interface OnItemViewClickListener {

        /**
         * Registers item click listener
         * @param position Position of the item clicked
         * @param v Context view
         */
        void OnItemClickListener(int position, View v);
    }

    /**
     * Initiates DefaultClientSuggestionsAdapter with default topEntries parameter
     * Using this constructor, no categories for any search results will be shown
     *
     * @param suggestions List of suggestions to be added
     * @param context Context of the activity
     * @param context Context of the activity
     * @param queryText The text which is queried/written in the search bar
     * @param shouldHighlight Should highlight the queried text or not
     * @param showHits Should show number of hits or not
     */
    public DefaultClientSuggestionsAdapter(ArrayList<SuggestionsModel> suggestions, Context context, String queryText, boolean shouldHighlight, boolean showHits) {
        this.suggestions = suggestions;
        this.context = context;
        this.queryText = queryText;
        this.shouldHighlight = shouldHighlight;
        this.topEntries = -1;
        this.showHits = showHits;
    }

    /**
     * Initiates DefaultClientSuggestionsAdapter
     *
     * @param suggestions List of suggestions to be added
     * @param context Context of the activity
     * @param queryText The text which is queried/written in the search bar
     * @param shouldHighlight Should highlight the queried text or not
     * @param showHits Should show number of hits or not
     * @param topEntries  Number of entries for which categories needs to be shown under search result
     */
    public DefaultClientSuggestionsAdapter(ArrayList<SuggestionsModel> suggestions, Context context, String queryText, boolean shouldHighlight, boolean showHits, int topEntries) {
        this.suggestions = suggestions;
        this.context = context;
        this.queryText = queryText;
        this.shouldHighlight = shouldHighlight;
        this.topEntries = topEntries;
        this.showHits = showHits;
    }

    @Override
    public Suggestions_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.default_suggestion_item, parent, false);
        Suggestions_View_Holder holder = new Suggestions_View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Suggestions_View_Holder holder, int position) {

        if(shouldHighlight) {
            if(suggestions.get(position).getText().toLowerCase().contains(queryText.toLowerCase())) {
                int start = suggestions.get(position).getText().toLowerCase().indexOf(queryText.toLowerCase());
                int end = start + queryText.length();

                String firstHalf = suggestions.get(position).getText().substring(0, start);
                String secondHalf = suggestions.get(position).getText().substring(end);
                String highlight = suggestions.get(position).getText().substring(start, end);

                holder.text.setText(Html.fromHtml(firstHalf + "<font color=#000000>" + highlight + "</font>" + secondHalf));
            } else
                holder.text.setText(suggestions.get(position).getText());
        }
        else {
            holder.text.setText(suggestions.get(position).getText());
        }

        if(showHits) {
            holder.hits.setVisibility(View.VISIBLE);
            holder.hits.setText(suggestions.get(position).getHits());
        } else
            holder.hits.setVisibility(View.GONE);

        holder.searchIcon.setImageResource(suggestions.get(position).getSearchIcon());
        holder.trendingIcon.setImageResource(suggestions.get(position).getTrendingIcon());
        if (topEntries == -1)
            holder.category.setVisibility(View.GONE);
        else
            if (position < topEntries)
                holder.category.setText(suggestions.get(position).getCategory());
            else
                holder.category.setVisibility(View.GONE);
    }

    @Override
    /**
     * Returns the number of elements the RecyclerView will display
     */
    public int getItemCount() {
        return suggestions.size();
    }

    @Override
    /**
     * Attaches recycler view to parent view
     */
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Inserts an item in DefaultClientSuggestionsAdapter
     * @param position Position where the item needs to be added
     * @param data Item that needs to be added
     */
    public void insert(int position, SuggestionsModel data) {
        suggestions.add(position, data);
        notifyItemInserted(position);
    }

    /**
     * Deletes an item from DefaultClientSuggestionsAdapter
     * @param data Item that needs to be deleted
     */
    public void remove(SuggestionsModel data) {
        int position = suggestions.indexOf(data);
        suggestions.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        int size = getItemCount();
        for(int i = size - 1; i >= 0; i--) {
            suggestions.remove(i);
            notifyItemRemoved(i);
        }
    }
}

