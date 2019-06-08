package com.example.searchwidget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.searchwidget.adapter.SuggestionsAdapter;

public class SearchBar extends RelativeLayout {

    public static final int BUTTON_SPEECH = 1;
    public static final int BUTTON_NAVIGATION = 2;
    public static final int BUTTON_BACK = 3;
    public static final int VIEW_VISIBLE = 1;
    public static final int VIEW_INVISIBLE = 0;
    private CardView searchBarCardView;
    private LinearLayout inputContainer;
    private ImageView navIcon;
    private ImageView menuIcon;
    private ImageView searchIcon;
    private ImageView arrowIcon;
    private ImageView clearIcon;
    private EditText searchEdit;
    private TextView placeHolder;
    private View suggestionDivider;
    private View menuDivider;
    private OnSearchActionListener onSearchActionListener;
    private boolean searchEnabled;
    private boolean suggestionsVisible;
    private boolean isSuggestionsEnabled = true;
    private SuggestionsAdapter adapter;
    private float destiny;

    private PopupMenu popupMenu;

    private int navIconResId;
    private int menuIconRes;
    private int searchIconRes;
    private int speechIconRes;
    private int arrowIconRes;
    private int clearIconRes;

    private boolean speechMode;
    private int maxSuggestionCount;
    private boolean navButtonEnabled;
    private boolean roundedSearchBarEnabled;
    private boolean menuDividerEnabled;
    private int dividerColor;
    private int searchBarColor;

    private CharSequence hintText;
    private CharSequence placeholderText;
    private int textColor;
    private int hintColor;
    private int placeholderColor;
    private int navIconTint;
    private int menuIconTint;
    private int searchIconTint;
    private int arrowIconTint;
    private int clearIconTint;

    private boolean navIconTintEnabled;
    private boolean menuIconTintEnabled;
    private boolean searchIconTintEnabled;
    private boolean arrowIconTintEnabled;
    private boolean clearIconTintEnabled;
    private boolean borderlessRippleEnabled = false;

    private int textCursorColor;
    private int highlightedTextColor;

    //Nav/Back Arrow Flag
    private boolean navIconShown = true;

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

        TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.SearchBar);

        //Base Attributes
        speechMode = array.getBoolean(R.styleable.SearchBar_speechMode, false);
        maxSuggestionCount = array.getInt(R.styleable.SearchBar_maxSuggestionsCount, 3);
        navButtonEnabled = array.getBoolean(R.styleable.SearchBar_navIconEnabled, false);
        roundedSearchBarEnabled = array.getBoolean(R.styleable.SearchBar_roundedSearchBarEnabled, false);
        menuDividerEnabled = array.getBoolean(R.styleable.SearchBar_menuDividerEnabled, false);
        dividerColor = array.getColor(R.styleable.SearchBar_dividerColor, ContextCompat.getColor(getContext(), R.color.searchBarDividerColor));
        searchBarColor = array.getColor(R.styleable.SearchBar_searchBarColor, ContextCompat.getColor(getContext(), R.color.searchBarPrimaryColor));

        //Icon Related Attributes
        menuIconRes = array.getResourceId(R.styleable.SearchBar_menuIconDrawable, R.drawable.ic_dots_vertical_black_48dp);
        searchIconRes = array.getResourceId(R.styleable.SearchBar_searchIconDrawable, R.drawable.ic_magnify_black_48dp);
        speechIconRes = array.getResourceId(R.styleable.SearchBar_speechIconDrawable, R.drawable.ic_microphone_black_48dp);
        arrowIconRes = array.getResourceId(R.styleable.SearchBar_backIconDrawable, R.drawable.ic_arrow_left_black_48dp);
        clearIconRes = array.getResourceId(R.styleable.SearchBar_clearIconDrawable, R.drawable.ic_close_black_48dp);
        navIconTint = array.getColor(R.styleable.SearchBar_navIconTint, ContextCompat.getColor(getContext(), R.color.searchBarNavIconTintColor));
        menuIconTint = array.getColor(R.styleable.SearchBar_menuIconTint, ContextCompat.getColor(getContext(), R.color.searchBarMenuIconTintColor));
        searchIconTint = array.getColor(R.styleable.SearchBar_searchIconTint, ContextCompat.getColor(getContext(), R.color.searchBarSearchIconTintColor));
        arrowIconTint = array.getColor(R.styleable.SearchBar_backIconTint, ContextCompat.getColor(getContext(), R.color.searchBarBackIconTintColor));
        clearIconTint = array.getColor(R.styleable.SearchBar_clearIconTint, ContextCompat.getColor(getContext(), R.color.searchBarClearIconTintColor));
        navIconTintEnabled = array.getBoolean(R.styleable.SearchBar_navIconUseTint, true);
        menuIconTintEnabled = array.getBoolean(R.styleable.SearchBar_menuIconUseTint, true);
        searchIconTintEnabled = array.getBoolean(R.styleable.SearchBar_searchIconUseTint, true);
        arrowIconTintEnabled = array.getBoolean(R.styleable.SearchBar_backIconUseTint, true);
        clearIconTintEnabled = array.getBoolean(R.styleable.SearchBar_clearIconUseTint, true);
        borderlessRippleEnabled = array.getBoolean(R.styleable.SearchBar_borderlessRippleEnabled, false);

        //Text Related Attributes
        hintText = array.getString(R.styleable.SearchBar_hint);
        placeholderText = array.getString(R.styleable.SearchBar_placeholder);
        textColor = array.getColor(R.styleable.SearchBar_textColor, ContextCompat.getColor(getContext(), R.color.searchBarTextColor));
        hintColor = array.getColor(R.styleable.SearchBar_hintColor, ContextCompat.getColor(getContext(), R.color.searchBarHintColor));
        placeholderColor = array.getColor(R.styleable.SearchBar_placeholderColor, ContextCompat.getColor(getContext(), R.color.searchBarPlaceholderColor));
        textCursorColor = array.getColor(R.styleable.SearchBar_textCursorTint, ContextCompat.getColor(getContext(), R.color.searchBarCursorColor));
        highlightedTextColor = array.getColor(R.styleable.SearchBar_highlightedTextColor, ContextCompat.getColor(getContext(), R.color.searchBarTextHighlightColor));

        array.recycle();
    }

    /**
     * Interface definition for SearchBar callbacks.
     */
    public interface OnSearchActionListener {
        /**
         * Invoked when SearchBar opened or closed
         *
         * @param enabled state
         */
        void onSearchStateChanged(boolean enabled);

        /**
         * Invoked when search confirmed and "search" button is clicked on the soft keyboard
         *
         * @param text search input
         */
        void onSearchConfirmed(CharSequence text);

        /**
         * Invoked when "speech" or "navigation" buttons clicked.
         *
         * @param buttonCode {@link #BUTTON_NAVIGATION}, {@link #BUTTON_SPEECH} or {@link #BUTTON_BACK} will be passed
         */
        void onButtonClicked(int buttonCode);
    }
}
