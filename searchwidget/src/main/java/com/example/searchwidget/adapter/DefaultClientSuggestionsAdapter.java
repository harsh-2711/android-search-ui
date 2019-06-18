package com.example.searchwidget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.searchwidget.Model.SuggestionsModel;
import com.example.searchwidget.R;
import com.example.searchwidget.View.Suggestions_View_Holder;

import java.util.ArrayList;

public class DefaultClientSuggestionsAdapter extends RecyclerView.Adapter<Suggestions_View_Holder> {
    private ArrayList<SuggestionsModel> suggestions;
    private Context context;
    private int topEntries;

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
     * @param suggestions List of suggestions to be added
     * @param context Context of the activity
     */
    public DefaultClientSuggestionsAdapter(ArrayList<SuggestionsModel> suggestions, Context context) {
        this.suggestions = suggestions;
        this.context = context;
        this.topEntries = -1;
    }

    /**
     * Initiates DefaultClientSuggestionsAdapter
     *
     * @param suggestions List of suggestions to be added
     * @param context Context of the activity
     * @param topEntries Number of entries for which categories needs to be shown under search result
     */
    public DefaultClientSuggestionsAdapter(ArrayList<SuggestionsModel> suggestions, Context context, int topEntries) {
        this.suggestions = suggestions;
        this.context = context;
        this.topEntries = topEntries;
    }

    @Override
    public Suggestions_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.default_suggestion_item, parent, false);
        Suggestions_View_Holder holder = new Suggestions_View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Suggestions_View_Holder holder, int position) {
        holder.text.setText(suggestions.get(position).getText());
        holder.hits.setText(suggestions.get(position).getHits());
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

