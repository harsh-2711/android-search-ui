package com.harsh.searchwidget.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harsh.searchwidget.Model.ClientSuggestionsModel;
import com.harsh.searchwidget.R;
import com.harsh.searchwidget.View.ClientSuggestionsViewHolder;

import java.util.ArrayList;

public class DefaultClientSuggestionsAdapter extends RecyclerView.Adapter<ClientSuggestionsViewHolder> {
    private ArrayList<ClientSuggestionsModel> suggestions;
    private int topEntries;
    private boolean shouldHighlight;
    private String queryText;
    private boolean showHits;
    private boolean searchResultImage;
    private boolean redirectIcon;
    private RedirectClickListener redirectClickListener;
    private RecyclerItemClickListener recyclerItemClickListener;
    private final int endTextLimit = 15;
    private int customCategoriesCount;

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
     * Interface for handling click events on redirect icon
     */
    public interface RedirectClickListener {

        /**
         * Invoked when redirect icon is clicked
         *
         * @param position Position of the result selected from given search results
         * @param responseText Text of the result selected
         */
        void onRedirectIconClicked(int position, String responseText);
    }

    /**
     * Interface for handling click events on search results
     */
    public interface RecyclerItemClickListener {

        /**
         * Invoked when search result is clicked once
         *
         * @param v Context view
         * @param position Position of the result clicked from given search results
         */
        void onItemClick(View v, int position);

        /**
         * Invoked when search result is long pressed
         * @param v Context view
         * @param position Position of the result clicked from given search results
         */
        void onItemClickLong(View v, int position);
    }

    /**
     * Initiates DefaultClientSuggestionsAdapter with default topEntries parameter
     * Using this constructor, no categories for any search results will be shown
     *
     * @param suggestions List of suggestions to be added
     * @param queryText The text which is queried/written in the search bar
     * @param shouldHighlight Should highlight the queried text or not
     * @param showHits Should show number of hits or not
     * @param searchResultImage Whether to show search image/icon before search results/suggestions
     * @param redirectIcon Whether to show redirect icon for every search result entry
     */
    public DefaultClientSuggestionsAdapter(ArrayList<ClientSuggestionsModel> suggestions, String queryText, boolean shouldHighlight, boolean showHits, boolean searchResultImage, boolean redirectIcon) {
        this.suggestions = suggestions;
        this.queryText = queryText;
        this.shouldHighlight = shouldHighlight;
        this.showHits = showHits;
        this.searchResultImage = searchResultImage;
        this.redirectIcon = redirectIcon;
        this.topEntries = -1;
        this.customCategoriesCount = -1;
    }

    /**
     * Initiates DefaultClientSuggestionsAdapter
     *
     * @param suggestions List of suggestions to be added
     * @param queryText The text which is queried/written in the search bar
     * @param shouldHighlight Should highlight the queried text or not
     * @param showHits Should show number of hits or not
     * @param searchResultImage Whether to show search image/icon before search results/suggestions
     * @param redirectIcon Whether to show redirect icon for every search result entry
     * @param topEntries  Number of entries for which categories needs to be shown under search result
     */
    public DefaultClientSuggestionsAdapter(ArrayList<ClientSuggestionsModel> suggestions, String queryText, boolean shouldHighlight, boolean showHits, boolean searchResultImage, boolean redirectIcon, int topEntries) {
        this.suggestions = suggestions;
        this.queryText = queryText;
        this.shouldHighlight = shouldHighlight;
        this.showHits = showHits;
        this.searchResultImage = searchResultImage;
        this.redirectIcon = redirectIcon;
        this.topEntries = topEntries;
        this.customCategoriesCount = -1;
    }

    /**
     * Initiates DefaultClientSuggestionsAdapter with separate results for categories
     *
     * @param suggestions List of suggestions to be added
     * @param customCategoriesCount Number of extra search results to be added on top for categorical search
     * @param queryText The text which is queried/written in the search bar
     * @param shouldHighlight Should highlight the queried text or not
     * @param showHits Should show number of hits or not
     * @param searchResultImage Whether to show search image/icon before search results/suggestions
     * @param redirectIcon Whether to show redirect icon for every search result entry
     */
    public DefaultClientSuggestionsAdapter(ArrayList<ClientSuggestionsModel> suggestions, int customCategoriesCount, String queryText, boolean shouldHighlight, boolean showHits, boolean searchResultImage, boolean redirectIcon) {
        this.suggestions = suggestions;
        this.queryText = queryText;
        this.shouldHighlight = shouldHighlight;
        this.showHits = showHits;
        this.searchResultImage = searchResultImage;
        this.redirectIcon = redirectIcon;
        this.topEntries = -1;
        this.customCategoriesCount = customCategoriesCount;
    }

    @Override
    public ClientSuggestionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.default_suggestion_item, parent, false);
        ClientSuggestionsViewHolder holder = new ClientSuggestionsViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientSuggestionsViewHolder holder, final int position) {

        boolean shouldRedirect = false;

        if(customCategoriesCount != -1 && position < customCategoriesCount) {
            String firstHalf = suggestions.get(position).getText().substring(0, queryText.length());
            String secondHalf = suggestions.get(position).getText().substring(queryText.length());

            holder.text.setText(Html.fromHtml("<font color=#00a1ff> <b>" + firstHalf + "</b>" + secondHalf + "</font>"));
        }
        else if(shouldHighlight) {
            if(suggestions.get(position).getText().toLowerCase().contains(queryText.toLowerCase())) {
                int start = suggestions.get(position).getText().toLowerCase().indexOf(queryText.toLowerCase());
                int end = start + queryText.length();

                String firstHalf = suggestions.get(position).getText().substring(0, start);
                String secondHalf = suggestions.get(position).getText().substring(end);
                String highlight = suggestions.get(position).getText().substring(start, end);

                holder.text.setText(Html.fromHtml(firstHalf + "<font color=#000000>" + highlight + "</font>" + secondHalf));

                if(firstHalf.length() > secondHalf.length() + highlight.length())
                    holder.text.setEllipsize(TextUtils.TruncateAt.START);
                else if(secondHalf.length() < endTextLimit)
                    holder.text.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                else
                    holder.text.setEllipsize(TextUtils.TruncateAt.END);

                if(secondHalf.length() >= 1)
                    shouldRedirect = true;

            } else {
                holder.text.setText(suggestions.get(position).getText());
                holder.text.setEllipsize(TextUtils.TruncateAt.END);
            }
        }
        else {
            holder.text.setText(suggestions.get(position).getText());
            holder.text.setEllipsize(TextUtils.TruncateAt.END);
        }

        if(showHits) {
            holder.hits.setVisibility(View.VISIBLE);
            holder.hits.setText(suggestions.get(position).getHits());
        } else
            holder.hits.setVisibility(View.GONE);

        if(searchResultImage) {
            holder.searchIcon.setVisibility(View.VISIBLE);
            holder.searchIcon.setImageResource(suggestions.get(position).getSearchIcon());
        }
        else
            holder.searchIcon.setVisibility(View.GONE);

        if(redirectIcon && shouldRedirect && (customCategoriesCount == -1 || position >= customCategoriesCount)) {
            holder.trendingIcon.setVisibility(View.VISIBLE);
            holder.trendingIcon.setImageResource(suggestions.get(position).getTrendingIcon());
        } else
            holder.trendingIcon.setVisibility(View.GONE);

        holder.trendingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectClickListener.onRedirectIconClicked(position, suggestions.get(position).getText());
            }
        });

        if (topEntries == -1)
            holder.category.setVisibility(View.GONE);
        else
            if (position < topEntries) {
                if(suggestions.get(position).getCategory() != null && !suggestions.get(position).getCategory().equals(""))
                    holder.category.setText("in " + suggestions.get(position).getCategory());
                else
                    holder.category.setText(suggestions.get(position).getCategory());
            }
            else
                holder.category.setVisibility(View.GONE);

        holder.touchListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerItemClickListener.onItemClick(v, position);
            }
        });

        holder.touchListener.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                recyclerItemClickListener.onItemClickLong(v, position);
                return true;
            }
        });
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
    public void insert(int position, ClientSuggestionsModel data) {
        suggestions.add(position, data);
        notifyItemInserted(position);
    }

    /**
     * Deletes an item from DefaultClientSuggestionsAdapter
     * @param data Item that needs to be deleted
     */
    public void remove(ClientSuggestionsModel data) {
        int position = suggestions.indexOf(data);
        suggestions.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Clears all items in adapter
     */
    public void clear() {
        int size = getItemCount();
        for(int i = size - 1; i >= 0; i--) {
            suggestions.remove(i);
            notifyItemRemoved(i);
        }
    }

    /**
     * Get search result at given position
     *
     * @param position Position of the search item to be returned
     * @return Search item in form ClientSuggestionsModel
     */
    public ClientSuggestionsModel getItem(int position) {
        return suggestions.get(position);
    }

    /**
     * Registers listener for redirect icon click callbacks
     * @param redirectClickListener Redirect icon click callbacks
     */
    public void setOnRedirectClickListener(RedirectClickListener redirectClickListener) {
        this.redirectClickListener = redirectClickListener;
    }

    public void setRecyclerItemClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }
}

