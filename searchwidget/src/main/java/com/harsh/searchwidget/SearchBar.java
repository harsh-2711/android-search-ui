package com.harsh.searchwidget;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harsh.searchwidget.Adapter.DefaultLocalSuggestionsAdapter;
import com.harsh.searchwidget.Builder.Analytics;
import com.harsh.searchwidget.Builder.DefaultClientSuggestions;
import com.harsh.searchwidget.Builder.SearchProp;
import com.harsh.searchwidget.Model.AnalyticsModel;
import com.harsh.searchwidget.Model.ClientSuggestionsModel;
import com.harsh.searchwidget.Adapter.DefaultClientSuggestionsAdapter;
import com.harsh.searchwidget.Adapter.SuggestionsAdapter;
import com.harsh.searchwidget.Model.SearchPropModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


import io.appbase.client.AppbaseClient;

import static android.content.ContentValues.TAG;

public class SearchBar extends RelativeLayout implements View.OnClickListener,
        Animation.AnimationListener, SuggestionsAdapter.OnItemViewClickListener,
        View.OnFocusChangeListener, TextView.OnEditorActionListener {

    public static final int BUTTON_SPEECH = 1;
    public static final int BUTTON_NAVIGATION = 2;
    public static final int BUTTON_BACK = 3;
    public static final int VIEW_VISIBLE = 1;
    public static final int VIEW_INVISIBLE = 0;
    private static final String listening = "Listening...";

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
    private boolean isSearchIconVisible = true;
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

    private boolean navIconShown = true;

    private boolean isPropSet = false;
    private static SearchPropModel defaultSearchPropModel;
    private String defaultQuery;

    private AppbaseClient client;
    private boolean isAppbaseClientSet = false;
    private String clientType;

    private TextChangeListener textChangeListener;
    private ItemClickListener itemClickListener;

    private boolean shouldLogQuery = false;
    private boolean speechPermissionGranted = false;

    private TextWatcher textWatcher;

    private DefaultClientSuggestionsAdapter defaultClientSuggestionsAdapter;
    private RecyclerView recyclerView;

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

        destiny = getResources().getDisplayMetrics().density;
        if (adapter == null) {
            adapter = new DefaultLocalSuggestionsAdapter(LayoutInflater.from(getContext()));
        }
        if (adapter instanceof DefaultLocalSuggestionsAdapter)
            ((DefaultLocalSuggestionsAdapter) adapter).setListener(this);
        adapter.setMaxSuggestionsCount(maxSuggestionCount);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setVisibility(GONE);

        array.recycle();

        // View References
        searchBarCardView = findViewById(R.id.container);
        suggestionDivider = findViewById(R.id.divider);
        menuDivider = findViewById(R.id.menu_divider);
        menuIcon = findViewById(R.id.menu);
        clearIcon = findViewById(R.id.clear);
        searchIcon = findViewById(R.id.search);
        arrowIcon = findViewById(R.id.arrow);
        searchEdit = findViewById(R.id.editText);
        placeHolder = findViewById(R.id.placeholder);
        inputContainer = findViewById(R.id.inputContainer);
        navIcon = findViewById(R.id.nav);
        findViewById(R.id.clear).setOnClickListener(this);

        // Listeners
        setOnClickListener(this);
        arrowIcon.setOnClickListener(this);
        searchIcon.setOnClickListener(this);
        searchEdit.setOnFocusChangeListener(this);
        searchEdit.setOnEditorActionListener(this);
        navIcon.setOnClickListener(this);

        // Placeholder
        placeholderText = "Search Widget";

        postSetup();
    }


    /**
     * Inflate menu for searchBar
     *
     * @param menuResource - menu resource
     */
    public void inflateMenu(int menuResource) {
        inflateMenuRequest(menuResource, -1);
    }

    /**
     * Inflate menu for searchBar with custom Icon
     *
     * @param menuResource - menu resource
     * @param icon         - icon resource id
     */
    public void inflateMenu(int menuResource, int icon) {
        inflateMenuRequest(menuResource, icon);
    }

    private void inflateMenuRequest(int menuResource, int iconResId) {
        if (menuResource > 0) {
            ImageView menuIcon = findViewById(R.id.menu);
            if (iconResId != -1) {
                menuIconRes = iconResId;
                menuIcon.setImageResource(menuIconRes);
            }
            RelativeLayout.LayoutParams params = (LayoutParams) searchIcon.getLayoutParams();
            params.rightMargin = (int) (48 * destiny);
            searchIcon.setLayoutParams(params);
            menuIcon.setVisibility(VISIBLE);
            menuIcon.setOnClickListener(this);
            popupMenu = new PopupMenu(getContext(), menuIcon);
            popupMenu.inflate(menuResource);
            popupMenu.setGravity(Gravity.RIGHT);
        }
    }

    /**
     * Get popup menu
     *
     * @return PopupMenu
     */
    public PopupMenu getMenu() {
        return this.popupMenu;
    }

    private void postSetup() {
        setupTextColors();
        setupRoundedSearchBarEnabled();
        setupSearchBarColor();
        setupIcons();
        setupMenuDividerEnabled();
        setupSearchEditText();
    }

    /**
     * Capsule shaped searchbar enabled
     * Only works on SDK V21+ due to odd behavior on lower
     */
    private void setupRoundedSearchBarEnabled() {
        if (roundedSearchBarEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            searchBarCardView.setRadius(getResources().getDimension(R.dimen.corner_radius_rounded));
        } else {
            searchBarCardView.setRadius(getResources().getDimension(R.dimen.corner_radius_default));
        }
    }

    private void setupSearchBarColor() {
        searchBarCardView.setCardBackgroundColor(searchBarColor);
        setupDividerColor();
    }

    private void setupDividerColor() {
        suggestionDivider.setBackgroundColor(dividerColor);
        menuDivider.setBackgroundColor(dividerColor);
    }

    private void setupTextColors() {
        searchEdit.setHintTextColor(hintColor);
        searchEdit.setTextColor(textColor);
        placeHolder.setTextColor(placeholderColor);
    }

    /**
     * Setup editText coloring and drawables
     */
    private void setupSearchEditText() {
        setupCursorColor();
        searchEdit.setHighlightColor(highlightedTextColor);

        if (hintText != null)
            searchEdit.setHint(hintText);
        if (placeholderText != null) {
            arrowIcon.setBackground(null);
            placeHolder.setText(placeholderText);
        }
    }

    private void setupCursorColor() {
        try {
            Field field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(searchEdit);

            field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int cursorDrawableRes = field.getInt(searchEdit);
            Drawable cursorDrawable = ContextCompat.getDrawable(getContext(), cursorDrawableRes).mutate();
            cursorDrawable.setColorFilter(textCursorColor, PorterDuff.Mode.SRC_IN);
            Drawable[] drawables = {cursorDrawable, cursorDrawable};
            field = editor.getClass().getDeclaredField("mCursorDrawable");
            field.setAccessible(true);
            field.set(editor, drawables);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //Setup Icon Colors And Drawables
    private void setupIcons() {

        //Drawables
        navIconResId = R.drawable.ic_menu_animated;
        this.navIcon.setImageResource(navIconResId);
        setNavButtonEnabled(navButtonEnabled);

        //Menu
        if (popupMenu == null) {
            findViewById(R.id.menu).setVisibility(GONE);
        }

        //Search
        setSpeechMode(speechMode);

        //Arrow
        this.arrowIcon.setImageResource(arrowIconRes);

        //Clear
        this.clearIcon.setImageResource(clearIconRes);

        //Colors
        setupNavIconTint();
        setupMenuIconTint();
        setupSearchIconTint();
        setupArrowIconTint();
        setupClearIconTint();
        setupIconRippleStyle();
    }

    private void setupNavIconTint() {
        if (navIconTintEnabled) {
            navIcon.setColorFilter(navIconTint, PorterDuff.Mode.SRC_IN);
        } else {
            navIcon.clearColorFilter();
        }
    }

    private void setupMenuIconTint() {
        if (menuIconTintEnabled) {
            menuIcon.setColorFilter(menuIconTint, PorterDuff.Mode.SRC_IN);
        } else {
            menuIcon.clearColorFilter();
        }
    }

    private void setupSearchIconTint() {
        if (searchIconTintEnabled) {
            searchIcon.setColorFilter(searchIconTint, PorterDuff.Mode.SRC_IN);
        } else {
            searchIcon.clearColorFilter();
        }
    }

    private void setupArrowIconTint() {
        if (arrowIconTintEnabled) {
            arrowIcon.setColorFilter(arrowIconTint, PorterDuff.Mode.SRC_IN);
        } else {
            arrowIcon.clearColorFilter();
        }
    }

    private void setupClearIconTint() {
        if (clearIconTintEnabled) {
            clearIcon.setColorFilter(clearIconTint, PorterDuff.Mode.SRC_IN);
        } else {
            clearIcon.clearColorFilter();
        }
    }

    private void setupMenuDividerEnabled() {
        if (menuDividerEnabled) {
            menuDivider.setVisibility(VISIBLE);
        } else {
            menuDivider.setVisibility(GONE);
        }
    }

    private void setupIconRippleStyle() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            TypedValue rippleStyle = new TypedValue();
            if (borderlessRippleEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, rippleStyle, true);
            } else {
                getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, rippleStyle, true);
            }
            navIcon.setBackgroundResource(rippleStyle.resourceId);
            searchIcon.setBackgroundResource(rippleStyle.resourceId);
            menuIcon.setBackgroundResource(rippleStyle.resourceId);
            arrowIcon.setBackgroundResource(rippleStyle.resourceId);
            clearIcon.setBackgroundResource(rippleStyle.resourceId);
        } else {
            Log.w(TAG, "setupIconRippleStyle() Only Available On SDK Versions Higher Than 16!");
        }
    }

    /**
     * Register listener for search bar callbacks.
     *
     * @param onSearchActionListener the callback listener
     */
    public void setOnSearchActionListener(OnSearchActionListener onSearchActionListener) {
        this.onSearchActionListener = onSearchActionListener;
    }

    /**
     * Hides search input and close arrow
     */
    public void disableSearch() {
        recyclerView.setVisibility(GONE);
        animateNavIcon();
        searchEnabled = false;
        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_right);
        out.setAnimationListener(this);

        if(isSearchIconVisible)
            searchIcon.setVisibility(VISIBLE);

        inputContainer.startAnimation(out);
        searchIcon.startAnimation(in);

        if (placeholderText != null) {
            placeHolder.setVisibility(VISIBLE);
            placeHolder.startAnimation(in);
        }
        if (listenerExists())
            onSearchActionListener.onSearchStateChanged(false);
        if (suggestionsVisible) animateSuggestions(getListHeight(false), 0);
    }

    /**
     * Shows search input and close arrow
     */
    public void enableSearch() {
        if (isSearchEnabled()) {
            onSearchActionListener.onSearchStateChanged(true);
            searchEdit.requestFocus();
            return;
        }
        animateNavIcon();
        adapter.notifyDataSetChanged();
        searchEnabled = true;
        Animation left_in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_left);
        Animation left_out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_left);
        left_in.setAnimationListener(this);
        placeHolder.setVisibility(GONE);
        inputContainer.setVisibility(VISIBLE);
        inputContainer.startAnimation(left_in);
        if (listenerExists()) {
            onSearchActionListener.onSearchStateChanged(true);
        }
        searchIcon.startAnimation(left_out);
    }

    private void animateNavIcon() {
        if (navIconShown) {
            this.navIcon.setImageResource(R.drawable.ic_menu_animated);
        } else {
            this.navIcon.setImageResource(R.drawable.ic_back_animated);
        }
        Drawable mDrawable = navIcon.getDrawable();
        if (mDrawable instanceof Animatable) {
            ((Animatable) mDrawable).start();
        }
        navIconShown = !navIconShown;
    }

    private void animateSuggestions(int from, int to) {
        suggestionsVisible = to > 0;
        final RelativeLayout last = findViewById(R.id.last);
        final ViewGroup.LayoutParams lp = last.getLayoutParams();
        if (to == 0 && lp.height == 0)
            return;
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.height = (int) animation.getAnimatedValue();
                last.setLayoutParams(lp);
            }
        });
        if (adapter.getItemCount() > 0)
            animator.start();
    }

    public void showSuggestionsList() {
        animateSuggestions(0, getListHeight(false));
    }

    public void hideSuggestionsList() {
        animateSuggestions(getListHeight(false), 0);
    }

    /**
     * Clears the suggestions list
     */
    public void clearSuggestions() {
        if (suggestionsVisible)
            animateSuggestions(getListHeight(false), 0);
        adapter.clearSuggestions();

        if(defaultClientSuggestionsAdapter != null) {
            defaultClientSuggestionsAdapter.clear();
            animateSuggestions(getListHeight(false), 0);
        }

        recyclerView.setVisibility(GONE);
    }

    /**
     * Check if suggestions are shown
     *
     * @return return result
     */
    public boolean areSuggestionsVisible() {
        return suggestionsVisible;
    }

    /**
     * Check if suggestions are enabled
     */
    public boolean areSuggestionsEnabled() {
        return isSuggestionsEnabled;
    }

    /**
     * Set suggestions enabled
     */
    public void setSuggestionsEnabled(boolean suggestionsEnabled) {
        isSuggestionsEnabled = suggestionsEnabled;
    }

    /**
     * Set Menu Icon Drawable
     *
     * @param menuIconResId icon resource id
     */
    public void setMenuIcon(int menuIconResId) {
        this.menuIconRes = menuIconResId;
        this.menuIcon.setImageResource(this.menuIconRes);
    }

    /**
     * Set search icon drawable
     *
     * @param searchIconResId icon resource id
     */
    public void setSearchIcon(int searchIconResId) {
        this.searchIconRes = searchIconResId;
        this.searchIcon.setImageResource(searchIconResId);
    }

    /**
     * Sets visibility of search icon in search bar
     *
     * @param state Boolean state visibility for search icon
     */
    public void setSearchIconVisibility(boolean state) {
        if(state) {
            this.searchIcon.setVisibility(VISIBLE);
            isSearchIconVisible = true;
        }
        else {
            this.searchIcon.setVisibility(GONE);
            isSearchIconVisible = false;
        }
    }

    /**
     * Set back arrow icon drawable
     *
     * @param arrowIconResId icon resource id
     */
    public void setArrowIcon(int arrowIconResId) {
        this.arrowIconRes = arrowIconResId;
        this.arrowIcon.setImageResource(arrowIconRes);
    }

    /**
     * Set clear icon drawable
     *
     * @param clearIconResId icon resource id
     */
    public void setClearIcon(int clearIconResId) {
        this.clearIconRes = clearIconResId;
        this.clearIcon.setImageResource(clearIconRes);
    }

    /**
     * Sets visibility of clear icon in search bar
     *
     * @param state Boolean state visibility for clear icon
     */
    public void setClearIconVisibility(boolean state) {
        if(state)
            this.clearIcon.setVisibility(VISIBLE);
        else
            this.clearIcon.setVisibility(GONE);
    }

    /**
     * Set the tint color of the navigation icon
     *
     * @param navIconTint nav icon color
     */
    public void setNavIconTint(int navIconTint) {
        this.navIconTint = navIconTint;
        setupNavIconTint();
    }

    /**
     * Set the tint color of the menu icon
     *
     * @param menuIconTint menu icon color
     */
    public void setMenuIconTint(int menuIconTint) {
        this.menuIconTint = menuIconTint;
        setupMenuIconTint();
    }

    /**
     * Set the tint color of the search/speech icon
     *
     * @param searchIconTint search icon color
     */
    public void setSearchIconTint(int searchIconTint) {
        this.searchIconTint = searchIconTint;
        setupSearchIconTint();
    }

    /**
     * Set the tint color of the back arrow icon
     *
     * @param arrowIconTint arrow icon color
     */
    public void setArrowIconTint(int arrowIconTint) {
        this.arrowIconTint = arrowIconTint;
        setupArrowIconTint();
    }

    /**
     * Set the tint color of the clear icon
     *
     * @param clearIconTint clear icon tint
     */
    public void setClearIconTint(int clearIconTint) {
        this.clearIconTint = clearIconTint;
        setupClearIconTint();
    }

    /**
     * Show a borderless ripple(circular) when icon is pressed
     * Borderless only available on SDK V21+
     *
     * @param borderlessRippleEnabled true for borderless, false for default
     */
    public void setIconRippleStyle(boolean borderlessRippleEnabled) {
        this.borderlessRippleEnabled = borderlessRippleEnabled;
        setupIconRippleStyle();
    }

    /**
     * Sets search bar hintText
     *
     * @param hintText hintText text
     */
    public void setHint(CharSequence hintText) {
        this.hintText = hintText;
        searchEdit.setHint(hintText);
    }

    /**
     * Sets search bar placeholder text
     *
     * @param placeHolderText Text to be shown while search bar is disabled
     */
    public void setPlaceHolderText(CharSequence placeHolderText) {
        this.placeholderText = placeHolderText;
    }

    /**
     * Returns the place holder text
     *
     * @return placeholder text
     */
    public CharSequence getPlaceHolderText() {
        return placeHolder.getText();
    }

    public void setMenuDividerEnabled(boolean menuDividerEnabled) {
        this.menuDividerEnabled = menuDividerEnabled;
        setupMenuDividerEnabled();
    }

    /**
     * sets the speechMode for the search bar.
     * If set to true, microphone icon will display instead of the search icon.
     * Also clicking on this icon will trigger the callback method onButtonClicked()
     *
     * @param speechMode enable speech
     * @see #BUTTON_SPEECH
     * @see OnSearchActionListener#onButtonClicked(int)
     */
    public void setSpeechMode(boolean speechMode) {
        this.speechMode = speechMode;
        if (speechMode) {
            searchIcon.setImageResource(speechIconRes);
            searchIcon.setClickable(true);
        } else {
            searchIcon.setImageResource(searchIconRes);
            searchIcon.setClickable(false);
        }
    }

    /**
     * Checks if the voice recording permission is granted for the device
     * Also sets the state of speechPermissionGranted variable
     *
     * @return
     */
    public boolean isVoicePermissionGranted() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            speechPermissionGranted = false;
        else
            speechPermissionGranted = true;

        return speechPermissionGranted;
    }

    /**
     * Sets the state of speechPermissionGranted variable
     *
     * @param state Whether voice recording permission is granted or not
     */
    public void setVoicePermissionGranted(boolean state) {
        this.speechPermissionGranted = state;
    }

    /**
     * True if MaterialSearchBar is in speech mode
     *
     * @return speech mode
     */
    public boolean isSpeechModeEnabled() {
        return speechMode;
    }

    /**
     * Check if search bar is in edit mode
     *
     * @return true if search bar is in edit mode
     */
    public boolean isSearchEnabled() {
        return searchEnabled;
    }

    /**
     * Specifies the maximum number of search queries stored until the activity is destroyed
     *
     * @param maxSuggestionsCount maximum queries
     */
    public void setMaxSuggestionCount(int maxSuggestionsCount) {
        this.maxSuggestionCount = maxSuggestionsCount;
        adapter.setMaxSuggestionsCount(maxSuggestionsCount);
    }

    /**
     * Sets a custom adapter for suggestions list view.
     *
     * @param suggestionAdapter customized adapter
     */
    public void setCustomSuggestionAdapter(SuggestionsAdapter suggestionAdapter) {
        this.adapter = suggestionAdapter;
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Returns the last search queries.
     * The queries are stored only for the duration of one activity session.
     * When the activity is destroyed, the queries will be deleted.
     * To save queries, use the method getLastSuggestions().
     * To recover the queries use the method setLastSuggestions().
     * <p><b color="red">List< String >  will be returned if You don't use custom adapter.</b></p>
     *
     * @return array with the latest search queries
     * @see #setLastSuggestions(List)
     * @see #setMaxSuggestionCount(int)
     */
    public List getLastSuggestions() {
        return adapter.getSuggestions();
    }

    /**
     * Sets the array of recent search queries.
     * It is advisable to save the queries when the activity is destroyed
     * and call this method when creating the activity.
     * <p><b color="red">Pass a List< String > if You don't use custom adapter.</b></p>
     *
     * @param suggestions an array of queries
     * @see #getLastSuggestions()
     * @see #setMaxSuggestionCount(int)
     */
    public void setLastSuggestions(List suggestions) {
        adapter.setSuggestions(suggestions);
    }

    /**
     * Changes the array of recent search queries with animation.
     * <p><b color="red">Pass a List< String >  if You don't use custom adapter.</b></p>
     *
     * @param suggestions an array of queries
     */
    public void updateLastSuggestions(List suggestions) {
        int startHeight = getListHeight(false);
        if (suggestions.size() > 0) {
            List newSuggestions = new ArrayList<>(suggestions);
            adapter.setSuggestions(newSuggestions);
            animateSuggestions(startHeight, getListHeight(false));
        } else {
            animateSuggestions(startHeight, 0);
        }
    }

    /**
     * Allows you to intercept the suggestions click event
     * <p><b color="red">This method will not work with custom Suggestion Adapter</b></p>
     *
     * @param listener click listener
     */
    public void setSuggestionsClickListener(SuggestionsAdapter.OnItemViewClickListener listener) {
        if (adapter instanceof DefaultLocalSuggestionsAdapter)
            ((DefaultLocalSuggestionsAdapter) adapter).setListener(listener);
    }

    /**
     * Set search input text color
     *
     * @param textColor text color
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        setupTextColors();
    }

    /**
     * Set text input hintText color
     *
     * @param hintColor text hintText color
     */
    public void setTextHintColor(int hintColor) {
        this.hintColor = hintColor;
        setupTextColors();
    }

    /**
     * Set placeholder text color
     *
     * @param placeholderColor placeholder color
     */
    public void setPlaceHolderColor(int placeholderColor) {
        this.placeholderColor = placeholderColor;
        setupTextColors();
    }

    /**
     * Set the color of the highlight when text is selected
     *
     * @param highlightedTextColor selected text highlight color
     */
    public void setTextHighlightColor(int highlightedTextColor) {
        this.highlightedTextColor = highlightedTextColor;
        searchEdit.setHighlightColor(highlightedTextColor);
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        setupDividerColor();
    }

    /**
     * Set navigation drawer menu icon enabled
     *
     * @param navButtonEnabled icon enabled
     */
    public void setNavButtonEnabled(boolean navButtonEnabled) {
        this.navButtonEnabled = navButtonEnabled;
        if (navButtonEnabled) {
            navIcon.setVisibility(VISIBLE);
            navIcon.setClickable(true);
            navIcon.getLayoutParams().width = (int) (50 * destiny);

            ((LayoutParams) inputContainer.getLayoutParams()).leftMargin = (int) (50 * destiny);
            arrowIcon.setVisibility(GONE);
        } else {
            navIcon.getLayoutParams().width = 1;
            navIcon.setVisibility(INVISIBLE);
            navIcon.setClickable(false);

            ((LayoutParams) inputContainer.getLayoutParams()).leftMargin = (int) (0 * destiny);
            arrowIcon.setVisibility(VISIBLE);
        }
        navIcon.requestLayout();
        placeHolder.requestLayout();
        arrowIcon.requestLayout();
    }

    /**
     * Enable capsule shaped SearchBar (API 21+)
     *
     * @param roundedSearchBarEnabled capsule shape enabled
     * @
     */
    public void setRoundedSearchBarEnabled(boolean roundedSearchBarEnabled) {
        this.roundedSearchBarEnabled = roundedSearchBarEnabled;
        setupRoundedSearchBarEnabled();
    }

    /**
     * Set CardView elevation
     *
     * @param elevation desired elevation
     */
    public void setCardViewElevation(int elevation) {
        CardView cardView = findViewById(R.id.container);
        cardView.setCardElevation(elevation);
    }

    /**
     * Get search text
     *
     * @return text
     */
    public String getText() {
        return searchEdit.getText().toString();
    }

    /**
     * Set search text
     *
     * @param text Text value
     */
    public void setText(String text) {
        searchEdit.setText(text);
    }

    /**
     * Sets text in search bar without activating onTextChangeListener
     *
     * @param text Text value
     */
    public void setTextWithoutActivatingTextWatcher(String text) {
        searchEdit.removeTextChangedListener(this.textWatcher);
        searchEdit.setText(text);
        searchEdit.addTextChangedListener(this.textWatcher);
    }

    /**
     * Add text watcher to searchbar's EditText
     *
     * @param textWatcher textWatcher to add
     */
    public void addTextChangeListener(TextWatcher textWatcher) {
        searchEdit.addTextChangedListener(textWatcher);
        this.textWatcher = textWatcher;
    }

    public EditText getSearchEditText() {
        return searchEdit;
    }

    public TextView getPlaceHolderView() {
        return placeHolder;
    }


    private boolean listenerExists() {
        return onSearchActionListener != null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == getId()) {
            if (!searchEnabled) {
                enableSearch();
                if(defaultSearchPropModel != null) {
                    if(defaultSearchPropModel.getDefaultSuggestions() != null) {

                        // Searching for categories count
                        int categoriesCount = 0;
                        for(int i = 0; i < defaultSearchPropModel.getDefaultSuggestions().size(); i++) {
                            ClientSuggestionsModel clientSuggestionsModel = defaultSearchPropModel.getDefaultSuggestions().get(i);
                            if(clientSuggestionsModel.getCategory() != null)
                                categoriesCount += 1;
                            else
                                break;
                        }

                        defaultClientSuggestionsAdapter = new DefaultClientSuggestionsAdapter(defaultSearchPropModel.getDefaultSuggestions(), "", false, false, true, true, categoriesCount);
                        recyclerView.setAdapter(defaultClientSuggestionsAdapter);
                        if(defaultClientSuggestionsAdapter.getItemCount() > 0)
                            recyclerView.setVisibility(VISIBLE);
                        else
                            recyclerView.setVisibility(GONE);
                    }
                }
            }
        } else if (id == R.id.clear) {
            recyclerView.setVisibility(GONE);
            searchEdit.setText("");

            if(defaultClientSuggestionsAdapter != null) {
                defaultClientSuggestionsAdapter.clear();
                recyclerView.setAdapter(defaultClientSuggestionsAdapter);
            }

            if(defaultClientSuggestionsAdapter != null) {
                defaultClientSuggestionsAdapter.clear();
                recyclerView.setAdapter(defaultClientSuggestionsAdapter);
            }
        }
        else if (id == R.id.arrow || !navIconShown) {
            disableSearch();

            if(defaultClientSuggestionsAdapter != null) {
                defaultClientSuggestionsAdapter.clear();
                recyclerView.setAdapter(defaultClientSuggestionsAdapter);
            }

            if(defaultClientSuggestionsAdapter != null) {
                defaultClientSuggestionsAdapter.clear();
                recyclerView.setAdapter(defaultClientSuggestionsAdapter);
            }

        } else if (id == R.id.search) {
            if (listenerExists())
                onSearchActionListener.onButtonClicked(BUTTON_SPEECH);
        } else if (id == R.id.menu) {
            popupMenu.show();
        } else if (id == R.id.nav)
            if (listenerExists()) {
                if (navIconShown) {
                    onSearchActionListener.onButtonClicked(BUTTON_NAVIGATION);
                } else {
                    onSearchActionListener.onButtonClicked(BUTTON_BACK);
                }
            }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (!searchEnabled) {
            inputContainer.setVisibility(GONE);
            searchEdit.setText("");
        } else {
            searchIcon.setVisibility(GONE);
            searchEdit.requestFocus();
            if (!suggestionsVisible && isSuggestionsEnabled)
                showSuggestionsList();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (hasFocus) {
            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (listenerExists())
            onSearchActionListener.onSearchConfirmed(searchEdit.getText());
        if (suggestionsVisible)
            hideSuggestionsList();
        if (adapter instanceof DefaultLocalSuggestionsAdapter)
            adapter.addSuggestion(searchEdit.getText().toString());
        return true;
    }

    /**
     * For calculate the height change when item delete or add animation
     * false is return the full height of item,
     * true is return the height of position subtraction one
     *
     * @param isSubtraction is subtraction enabled
     */
    private int getListHeight(boolean isSubtraction) {
        if (!isSubtraction)
            return (int) (adapter.getListHeight() * destiny);
        return (int) (((adapter.getItemCount() - 1) * adapter.getSingleViewHeight()) * destiny);
    }

    @Override
    public void OnItemClickListener(int position, View v) {
        if (v.getTag() instanceof String) {
            searchEdit.setText((String) v.getTag());
        }
    }

    @Override
    public void OnItemDeleteListener(int position, View v) {
        if (v.getTag() instanceof String) {
            /*Order of two line should't be change,
            because should calculate the height of item first*/
            animateSuggestions(getListHeight(false), getListHeight(true));
            adapter.deleteSuggestion(position, v.getTag());
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isSearchBarVisible = searchEnabled ? VIEW_VISIBLE : VIEW_INVISIBLE;
        savedState.suggestionsVisible = suggestionsVisible ? VIEW_VISIBLE : VIEW_INVISIBLE;
        savedState.speechMode = speechMode ? VIEW_VISIBLE : VIEW_INVISIBLE;
        savedState.navIconResId = navIconResId;
        savedState.searchIconRes = searchIconRes;
        savedState.suggestions = getLastSuggestions();
        savedState.maxSuggestions = maxSuggestionCount;
        if (hintText != null) savedState.hint = hintText.toString();
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        searchEnabled = savedState.isSearchBarVisible == VIEW_VISIBLE;
        suggestionsVisible = savedState.suggestionsVisible == VIEW_VISIBLE;
        setLastSuggestions(savedState.suggestions);
        if (suggestionsVisible)
            animateSuggestions(0, getListHeight(false));
        if (searchEnabled) {
            inputContainer.setVisibility(VISIBLE);
            placeHolder.setVisibility(GONE);
            searchIcon.setVisibility(GONE);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && searchEnabled) {
            animateSuggestions(getListHeight(false), 0);
            disableSearch();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * Initiates the Appbase client for giving additional functionality to search bar
     *
     * @param url URL of the ElasticSearch host server (If application is hosted on appbase.io, url should be https://scalr.api.appbase.io)
     * @param appName Name of the app (aka search index)
     * @param username Username for basic auth (String before ':' in credentials string)
     * @param password Password for given username (String after ':' in credentials string)
     * @param type Type to be queried
     */
    public void setAppbaseClient(String url, String appName, String username, String password, String type) {
        this.client = new AppbaseClient(url, appName, username, password);
        isAppbaseClientSet = true;
        this.clientType = type;
    }

    /**
     * Initiates the Appbase client for giving additional functionality to search bar - with default type
     *
     * @param url URL of the ElasticSearch host server (If application is hosted on appbase.io, url should be https://scalr.api.appbase.io)
     * @param appName Name of the app (aka search index)
     * @param username Username for basic auth (String before ':' in credentials string)
     * @param password Password for given username (String after ':' in credentials string)
     */
    public void setAppbaseClient(String url, String appName, String username, String password) {
        this.client = new AppbaseClient(url, appName, username, password);
        isAppbaseClientSet = true;
        this.clientType = "_type";
    }

    /**
     * Gives response for the requested query using Appbase client
     *
     * @param searchPropModel Model which is returned on building the search prop
     * @param query Query Text
     * @return Response received for the requested query
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public String search(SearchPropModel searchPropModel, String query) throws ExecutionException, InterruptedException {

        searchPropModel.setDefaultValue(query);

        RequestParams requestParams = new RequestParams(query, getRequestedQuery(searchPropModel));
        SingleResponseSearch singleResponseSearch = new SingleResponseSearch();
        return singleResponseSearch.execute(requestParams).get();
    }

    /**
     * Gives response for the requested query using Appbase client
     *
     * @param searchPropModel Model which is returned on building the search prop
     * @param query Query Text
     * @param category Category to search in
     * @param isCategoricalSearch If the search query should be categorical
     * @return Response received for the requested query
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public String search(SearchPropModel searchPropModel, String query, String category, boolean isCategoricalSearch) throws ExecutionException, InterruptedException {

        searchPropModel.setDefaultValue(query);

        if(isCategoricalSearch) {
            Log.d("Query", getCategoricalSearchQuery(searchPropModel, category));
            RequestParams requestParams = new RequestParams(query, getCategoricalSearchQuery(searchPropModel, category));
            SingleResponseSearch singleResponseSearch = new SingleResponseSearch();
            return singleResponseSearch.execute(requestParams).get();
        }
        else {
            RequestParams requestParams = new RequestParams(query, getRequestedQuery(searchPropModel));
            SingleResponseSearch singleResponseSearch = new SingleResponseSearch();
            return singleResponseSearch.execute(requestParams).get();
        }
    }

    /**
     * Initiates Search prop
     *
     * @param componentId Unique identifier of the component
     * @param dataFields Data field(s) on which search query is to be applied to
     */
    public SearchProp setSearchProp(String componentId, ArrayList<String> dataFields) {
        isPropSet = true;
        return new SearchProp(componentId, dataFields);
    }

    private String getAggsQuery(SearchPropModel searchPropModel) {

        String fields = "";

        for(int i = 0; i < searchPropModel.getAggregationFields().size(); i++) {
            if(i == searchPropModel.getAggregationFields().size()-1)
                fields = fields + "\"" + searchPropModel.getAggregationFields().get(i) + "\"";
            else
                fields = fields + "\"" + searchPropModel.getAggregationFields().get(i) + "\",";
        }

        return  "\"aggs\": { \"" + searchPropModel.getAggregationName() + "\": { \"terms\": { \"field\": \"" + fields + "\", } } }";
    }

    private String getShouldQuery(SearchPropModel searchPropModel) {

        String value = searchPropModel.getDefaultValue() != null ? searchPropModel.getDefaultValue() : "";
        String fuzziness = searchPropModel.getFuzziness() != null ? searchPropModel.getFuzziness() : "0";
        String fields = "";

        if(searchPropModel.getWeights() != null) {
            if(searchPropModel.getWeights().size() == searchPropModel.getDataField().size()) {
                for(int i = 0; i < searchPropModel.getDataField().size(); i++) {
                    if(i == searchPropModel.getDataField().size()-1)
                        fields = fields + "\"" + searchPropModel.getDataField().get(i) + "^" + searchPropModel.getWeights().get(i) + "\"";
                    else
                        fields = fields + "\"" + searchPropModel.getDataField().get(i) + "^" + searchPropModel.getWeights().get(i) + "\",";
                }
            } else {
                Log.d("Size Error", "Size of weights array doesn't match size of dataFields array");
                for(int i = 0; i < searchPropModel.getDataField().size(); i++) {
                    if(i == searchPropModel.getDataField().size()-1)
                        fields = fields + "\"" + searchPropModel.getDataField().get(i) + "\"";
                    else
                        fields = fields + "\"" + searchPropModel.getDataField().get(i) + "\",";
                }
            }
        } else {
            for(int i = 0; i < searchPropModel.getDataField().size(); i++) {
                if(i == searchPropModel.getDataField().size()-1)
                    fields = fields + "\"" + searchPropModel.getDataField().get(i) + "\"";
                else
                    fields = fields + "\"" + searchPropModel.getDataField().get(i) + "\",";
            }
        }

        if(searchPropModel.getQueryFormat().toLowerCase().equals("and")) {
            return "[ { \"multi_match\": { \"query\": \"" + value + "\", \"fields\": [" + fields + "], " +
                    "\"type\": \"cross_fields\", \"operator\": \"and\" } }, { \"multi_match\": { \"query\": \"" +
                    value + "\", \"fields\": [" + fields + "], \"type\": \"phrase_prefix\", \"operator\": \"and\" } } ]";
        } else {
            return "[ { \"multi_match\": { \"query\": \"" + value + "\", \"fields\": [" + fields + "], " +
                    "\"operator\": \"or\", \"fuzziness\": \"" + fuzziness + "\" } }, " +
                    "{ \"multi_match\": { \"query\": \"" + value + "\", \"fields\": [" + fields +
                    "], \"type\": \"phrase_prefix\", \"operator\": \"or\" } } ]";
        }
    }

    private String getDefaultQuery(SearchPropModel searchPropModel) {

        String finalQuery = null;
        String value = searchPropModel.getDefaultValue() != null ? searchPropModel.getDefaultValue() : "";

        if(!value.equals("")) {
            finalQuery = "{ \"bool\": { \"should\": " + getShouldQuery(searchPropModel) + ", \"minimum_should_match\": \"1\" } }";
        }
        else {
            finalQuery =  "{ \"match_all\": {}, }";
        }

        return finalQuery;
    }

    private String getHighlightQuery(SearchPropModel searchPropModel) {

        if(!searchPropModel.isHighlight())
            return null;

        String fields = "";
        if(searchPropModel.getHighlightField() != null) {
            for(int i = 0; i < searchPropModel.getHighlightField().size(); i++) {
                if(i == searchPropModel.getHighlightField().size()-1)
                    fields = fields + "\"" + searchPropModel.getHighlightField().get(i) + "\": {}";
                else
                    fields = fields + "\"" + searchPropModel.getHighlightField().get(i) + "\": {},";
            }
        } else {
            for(int i = 0; i < searchPropModel.getDataField().size(); i++) {
                if(i == searchPropModel.getDataField().size()-1)
                    fields = fields + "\"" + searchPropModel.getDataField().get(i) + "\"";
                else
                    fields = fields + "\"" + searchPropModel.getDataField().get(i) + "\",";
            }
        }

        return "{ \"highlight\": { \"pre_tags\": [\"<mark>\"], \"post_tags\": [\"</mark>\"], \"fields\": {" + fields + "}, " +
                "\"number_of_fragments\": \"0\" } }";

    }

    private String getWrappedQuery(String query) {
        return "{ \"size\": \"" + maxSuggestionCount + "\", \"query\":" + query + " }";
    }

    private String getCategoryAggregrationQuery(String categoryField, String aggregrationName) {

        return  "\"aggs\": { \"" + aggregrationName + "\": { \"terms\": { \"field\": \"" + categoryField + "\" } } }";
    }

    /**
     * The query built using search prop parameters and which can be directly passed into Appbase search client
     *
     * @param searchPropModel Model which is returned on building the search prop
     * @return Returns the query built by search prop parameters
     */
    public String getRequestedQuery(SearchPropModel searchPropModel) {

        if(isPropSet) {
            defaultQuery = getDefaultQuery(searchPropModel);
            defaultQuery = getWrappedQuery(defaultQuery);

            if(searchPropModel.getAggregrationState()) {
                defaultQuery = defaultQuery.substring(0, defaultQuery.length() - 1);
                defaultQuery = defaultQuery + ", " + getAggsQuery(searchPropModel) + " }";
            }

            if(searchPropModel.getCategoryField() != null && !searchPropModel.isInPlaceCategory()) {
                defaultQuery = defaultQuery.substring(0, defaultQuery.length() - 1);
                defaultQuery = defaultQuery + ", " + getCategoryAggregrationQuery(searchPropModel.getCategoryField(), searchPropModel.getAggregationName()) + " }";
            }

            return defaultQuery;

        } else {
            Log.e("Error", "Please set search prop properly");
            return "";
        }
    }

    private String getCategoricalQuery(SearchPropModel searchPropModel) {

        String value = searchPropModel.getDefaultValue() != null ? searchPropModel.getDefaultValue() : "";
        String fuzziness = searchPropModel.getFuzziness() != null ? searchPropModel.getFuzziness() : "0";
        String fields = "";

        if(searchPropModel.getWeights() != null) {
            if(searchPropModel.getWeights().size() == searchPropModel.getDataField().size()) {
                for(int i = 0; i < searchPropModel.getDataField().size(); i++) {
                    if(i == searchPropModel.getDataField().size()-1)
                        fields = fields + "\"" + searchPropModel.getDataField().get(i) + "^" + searchPropModel.getWeights().get(i) + "\"";
                    else
                        fields = fields + "\"" + searchPropModel.getDataField().get(i) + "^" + searchPropModel.getWeights().get(i) + "\",";
                }
            } else {
                Log.d("Size Error", "Size of weights array doesn't match size of dataFields array");
                for(int i = 0; i < searchPropModel.getDataField().size(); i++) {
                    if(i == searchPropModel.getDataField().size()-1)
                        fields = fields + "\"" + searchPropModel.getDataField().get(i) + "\"";
                    else
                        fields = fields + "\"" + searchPropModel.getDataField().get(i) + "\",";
                }
            }
        } else {
            for(int i = 0; i < searchPropModel.getDataField().size(); i++) {
                if(i == searchPropModel.getDataField().size()-1)
                    fields = fields + "\"" + searchPropModel.getDataField().get(i) + "\"";
                else
                    fields = fields + "\"" + searchPropModel.getDataField().get(i) + "\",";
            }
        }

        return "{ \"bool\": { \"must\": [ { \"multi_match\": { \"query\": \"" + value +  "\", " +
                "\"fields\": [" + fields + "], \"type\": \"best_fields\", \"operator\": \"or\", " +
                "\"fuzziness\": \"" + fuzziness + "\" } }, { \"multi_match\": { \"query\": \"" + value + "\", " +
                "\"fields\": [ " + fields + " ], \"type\": \"phrase_prefix\", \"operator\": \"or\" } } ], " +
                "\"minimum_should_match\": \"1\" } }";
    }

    private String getTermsQuery(SearchPropModel searchPropModel, String category) {

        String term =searchPropModel.getCategoryField();

        return "{ \"term\": { \"" + term + "\": \"" + category + "\" } }";
    }

    private String getCustomWrapper(SearchPropModel searchPropModel, String category) {

        return "{ \"bool\": { \"must\": [ { \"bool\": { \"should\": [ [ " + getCategoricalQuery(searchPropModel) + "" +
                ", " + getTermsQuery(searchPropModel, category) + " ] ], \"minimum_should_match\": \"1\" } } ] } }";
    }

    /**
     * Search query for making a categorical search
     *
     * @param searchPropModel Model which is returned on building the search prop
     * @return Returns the query built by search prop parameters
     */
    public String getCategoricalSearchQuery(SearchPropModel searchPropModel, String category) {

        if(isPropSet) {
            defaultQuery = " { \"bool\": { \"must\": [ " + getCustomWrapper(searchPropModel, category) + " ] } }";
            defaultQuery = getWrappedQuery(defaultQuery);
            return defaultQuery;

        } else {
            Log.e("Error", "Please set search prop properly");
            return "";
        }
    }

    /**
     * Sets the value of parameter used for checking whether to log requested query for debugging
     * @param state Boolean state of the logging parameter
     */
    public void setLoggingQuery(boolean state) {
        this.shouldLogQuery = state;
    }

    /**
     * Provides recycler view for attaching custom adapter
     *
     * @return Search results recycler view
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * Registers listener for text change callbacks
     * @param textChangeListener Text change callbacks
     */
    public void setOnTextChangeListener(TextChangeListener textChangeListener) {
        this.textChangeListener = textChangeListener;
    }

    private boolean textChangeListenerExists() {
        return textChangeListener != null;
    }

    private boolean itemClickListenerExists() {
        return this.itemClickListener != null;
    }

    /**
     * Interface definition for MaterialSearchBar callbacks.
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

    /**
     * Interface for getting continuous response on text change in search widget
     */
    public interface TextChangeListener {

        /**
         * Invoked when text is changed in search bar
         * @param response Response for the query made from search prop parameters using Appbase client
         */
        void onTextChange(String response);
    }

    /**
     * Interface for listening continuously to click events on search results
     */
    public interface ItemClickListener {

        /**
         * Invoked when a click is made on search result
         *
         * @param view View on which click event is registered
         * @param position Position of the item clicked
         */
        void onClick(View view, int position, ClientSuggestionsModel result);

        /**
         * Invoked when long click gesture is made on search result
         *
         * @param view View on which long click event is registered
         * @param position Position of the item on which long click is done
         */
        void onLongClick(View view, int position, ClientSuggestionsModel result);
    }

    private static class RequestParams {
        String queryText;
        String requestedQuery;

        RequestParams(String queryText, String requestedQuery) {
            this.queryText = queryText;
            this.requestedQuery = requestedQuery;
        }
    }

    private static class SearchParams {
        String queryText;
        String requestedQuery;
        ItemClickListener itemClickListener;

        SearchParams(String queryText, String requestedQuery, ItemClickListener itemClickListener) {
            this.queryText = queryText;
            this.requestedQuery = requestedQuery;
            this.itemClickListener = itemClickListener;
        }
    }

    /**
     * Sets new SearchPropModel which is used by startSearch method
     *
     * @param searchPropModel SearchPropModel object
     */
    public void setSearchPropModel(SearchPropModel searchPropModel) {
        defaultSearchPropModel = searchPropModel;
    }

    /**
     * Starts on text change callbacks to be handled by the listener.
     * Call this method after setting TextChangeListener to start its functionality
     *
     * @param searchPropModel Model which is returned on building the search prop
     * @param itemClickListener Listener to handle callbacks from click and long click events
     */
    public void startSearch(final SearchPropModel searchPropModel, final ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

        defaultSearchPropModel = searchPropModel;

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!String.valueOf(s).equals("")) {
                    if(isPropSet && isAppbaseClientSet) {
                        defaultSearchPropModel.setDefaultValue(String.valueOf(s));
                        StartSearching startSearching = new StartSearching();
                        SearchParams searchParams = new SearchParams(String.valueOf(s), getRequestedQuery(defaultSearchPropModel), itemClickListener);
                        startSearching.execute(searchParams);
                        if(shouldLogQuery)
                            Log.d("QUERY", getRequestedQuery(defaultSearchPropModel));
                    } else {
                        Log.e("Error", "Please check if Appbase client, Search props and Text change listeners are set properly");
                    }
                } else {
                    if(defaultClientSuggestionsAdapter != null)
                        defaultClientSuggestionsAdapter.clear();
                    recyclerView.setVisibility(GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        searchEdit.addTextChangedListener(textWatcher);
        this.textWatcher = textWatcher;
    }

    /**
     * Starts voice search functionality
     * The method first checks whether voice recording permission is given for the device
     * 
     * @param searchPropModel Model which is returned on building the search prop
     * @param itemClickListener Listener to handle callbacks from click and long click events
     */
    public void startVoiceSearch(final SearchPropModel searchPropModel, final ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        defaultSearchPropModel = searchPropModel;

        if(speechPermissionGranted) {

            placeHolder.setText(listening);
            SpeechRecognizer sr = SpeechRecognizer.createSpeechRecognizer(getContext());
            sr.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {
                    placeHolder.setText(placeholderText);
                }

                @Override
                public void onResults(Bundle results) {

                    String resultString;

                    ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    assert data != null;
                    resultString = data.get(0).toString();

                    enableSearch();
                    searchEdit.setText(resultString);

                    if(!resultString.equals("")) {
                        if(isPropSet && isAppbaseClientSet) {
                            defaultSearchPropModel.setDefaultValue(resultString);
                            StartSearching startSearching = new StartSearching();
                            SearchParams searchParams = new SearchParams(resultString, getRequestedQuery(defaultSearchPropModel), itemClickListener);
                            startSearching.execute(searchParams);
                            if(shouldLogQuery)
                                Log.d("QUERY", getRequestedQuery(defaultSearchPropModel));
                        } else {
                            Log.e("Error", "Please check if Appbase client, Search props and Text change listeners are set properly");
                        }
                    } else {
                        // TO DO: Make this feature available for custom adapters
                        if(defaultClientSuggestionsAdapter != null)
                            defaultClientSuggestionsAdapter.clear();
                        recyclerView.setVisibility(GONE);
                    }

                    placeHolder.setText(placeholderText);
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                    enableSearch();
                    String partialResult;

                    ArrayList data = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    assert data != null;
                    partialResult = data.get(0).toString();

                    searchEdit.setText(partialResult);

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"com.harsh.searchwidget");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);

            sr.startListening(intent);

        } else {
            Log.e("Permission Error", "Voice Recording Permission not granted");
        }
    }

    private class StartSearching extends AsyncTask<SearchParams, Void, Void> {

        String result = "Query wasn't initiated";
        ArrayList<String> entries;
        ArrayList<String> duplicateCheck;
        String query;
        ArrayList<String> categories;
        ArrayList<Boolean> categoricalSearch;
        ArrayList<HashMap<String, ArrayList<String>>> extraProperties;
        ItemClickListener itemClickListener;
        int categoriesCount = 0;

        @Override
        protected Void doInBackground(SearchParams... params) {
            query = params[0].queryText;
            itemClickListener = params[0].itemClickListener;

            try {
                result = client.prepareSearch(clientType, params[0].requestedQuery).execute().body().string();
            } catch (IOException e) {
                e.printStackTrace();
                result = e.toString();
            } catch (NullPointerException err) {
                err.printStackTrace();
                result = err.toString();
            }
            if(defaultSearchPropModel.isAutoSuggest()) {

                JSONObject resultJSON = null;
                entries = new ArrayList<>();
                categoricalSearch = new ArrayList<>();

                if(defaultSearchPropModel.getCategoryField() != null && !defaultSearchPropModel.isInPlaceCategory()) {

                    /* Aggregation for categories */
                    try {
                        resultJSON = new JSONObject(result);
                        JSONObject categoryObject = resultJSON.getJSONObject("aggregations");
                        JSONObject aggregrationObject = categoryObject.getJSONObject(defaultSearchPropModel.getAggregationName());
                        JSONArray buckets = aggregrationObject.getJSONArray("buckets");

                        for(int i = 0; i < buckets.length(); i++) {

                            JSONObject obj = buckets.getJSONObject(i);
                            String key = obj.getString("key");

                            if(i < defaultSearchPropModel.getTopEntries() && query.length() >= 3) {
                                entries.add(query + " in " + key);
                                categoricalSearch.add(true);
                                categoriesCount++;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                /* Search query */
                try {
                    resultJSON = new JSONObject(result);
                    JSONObject hits = resultJSON.getJSONObject("hits");
                    JSONArray finalHits = hits.getJSONArray("hits");

                    categories = new ArrayList<>();
                    extraProperties = new ArrayList<>();
                    duplicateCheck = new ArrayList<>();
                    for (int i = 0; i < finalHits.length(); i++) {

                        JSONObject obj = finalHits.getJSONObject(i);
                        JSONObject source = obj.getJSONObject("_source");

                        for(int j = 0; j < defaultSearchPropModel.getDataField().size(); j++) {

                            try {
                                String entry = source.getString(defaultSearchPropModel.getDataField().get(j));
                                if(!duplicateCheck.contains(entry.toLowerCase())) {
                                    entries.add(entry);
                                    categoricalSearch.add(false);

                                    duplicateCheck.add(entry.toLowerCase());
                                }
                            } catch (JSONException e) {
                                // Error finding data field
                            }

                        }

                        if(defaultSearchPropModel.getCategoryField() != null && defaultSearchPropModel.isInPlaceCategory()) {
                            try {
                                JSONArray categoryArray = source.getJSONArray(defaultSearchPropModel.getCategoryField());
                                categories.add(categoryArray.get(0).toString());
                            } catch (JSONException e) {

                                try {
                                    String category = source.getString(defaultSearchPropModel.getCategoryField());
                                    categories.add(category);
                                } catch (JSONException err) {
                                    //err.printStackTrace();
                                }
                            }

                        }

                        if(defaultSearchPropModel.getExtraFields() != null) {

                            for(int j = 0; j < defaultSearchPropModel.getExtraFields().size(); j++) {

                                try {
                                    JSONArray extraFieldsArray = source.getJSONArray(defaultSearchPropModel.getExtraFields().get(j));

                                    ArrayList<String> tempProperty = new ArrayList<>();
                                    for(int k = 0; k < extraFieldsArray.length(); k++) {
                                        JSONObject category = extraFieldsArray.getJSONObject(i);
                                        tempProperty.add(category.toString());
                                    }

                                    HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
                                    hashMap.put(defaultSearchPropModel.getExtraFields().get(j), tempProperty);
                                    extraProperties.add(hashMap);

                                } catch (JSONException e) {

                                    try {
                                        String extraField = source.getString(defaultSearchPropModel.getExtraFields().get(j));

                                        ArrayList<String> tempProperty = new ArrayList<>();
                                        tempProperty.add(extraField);
                                        HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
                                        hashMap.put(defaultSearchPropModel.getExtraFields().get(j), tempProperty);
                                        extraProperties.add(hashMap);

                                    } catch (JSONException err) {
                                        //err.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(textChangeListenerExists())
                textChangeListener.onTextChange(result);

            if(recyclerView.getAdapter() instanceof DefaultClientSuggestionsAdapter) {

                if(defaultSearchPropModel.isAutoSuggest()) {
                    ArrayList<ClientSuggestionsModel> adapterEntries;
                    if(defaultSearchPropModel.getCategoryField() != null && categories != null && defaultSearchPropModel.isInPlaceCategory()) {
                        adapterEntries = new DefaultClientSuggestions(entries).setCategories(categories).setCategoricalSearch(categoricalSearch).setExtraProperties(extraProperties).build();
                        defaultClientSuggestionsAdapter = new DefaultClientSuggestionsAdapter(adapterEntries, query, defaultSearchPropModel.isHighlight(), defaultSearchPropModel.getHitsState(), defaultSearchPropModel.isSearchResultImage(), defaultSearchPropModel.isRedirectIcon(), defaultSearchPropModel.getTopEntries());
                    }
                    else if(defaultSearchPropModel.getCategoryField() != null && categories != null && !defaultSearchPropModel.isInPlaceCategory()) {
                        adapterEntries = new DefaultClientSuggestions(entries).setCategoricalSearch(categoricalSearch).build();
                        defaultClientSuggestionsAdapter = new DefaultClientSuggestionsAdapter(adapterEntries, categoriesCount, query, defaultSearchPropModel.isHighlight(), defaultSearchPropModel.getHitsState(), defaultSearchPropModel.isSearchResultImage(), defaultSearchPropModel.isRedirectIcon());
                    }
                    else {
                        adapterEntries = new DefaultClientSuggestions(entries).setCategoricalSearch(categoricalSearch).build();
                        defaultClientSuggestionsAdapter = new DefaultClientSuggestionsAdapter(adapterEntries, query, defaultSearchPropModel.isHighlight(), defaultSearchPropModel.getHitsState(), defaultSearchPropModel.isSearchResultImage(), defaultSearchPropModel.isRedirectIcon());
                    }

                    defaultClientSuggestionsAdapter.setOnRedirectClickListener(new DefaultClientSuggestionsAdapter.RedirectClickListener() {
                        @Override
                        public void onRedirectIconClicked(int position, String responseText) {
                            searchEdit.setText(responseText);
                            searchEdit.setSelection(responseText.length());
                        }
                    });

                    defaultClientSuggestionsAdapter.setRecyclerItemClickListener(new DefaultClientSuggestionsAdapter.RecyclerItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            itemClickListener.onClick(v, position, defaultClientSuggestionsAdapter.getItem(position));
                        }

                        @Override
                        public void onItemClickLong(View v, int position) {
                            itemClickListener.onLongClick(v, position, defaultClientSuggestionsAdapter.getItem(position));
                        }
                    });

                    recyclerView.setAdapter(defaultClientSuggestionsAdapter);
                    if(defaultClientSuggestionsAdapter.getItemCount() > 0)
                        recyclerView.setVisibility(VISIBLE);
                    else
                        recyclerView.setVisibility(GONE);
                }
            }
        }
    }

    private class SingleResponseSearch extends AsyncTask<RequestParams,Void,String> {

        String defaultResult = "Internal server error";

        @Override
        protected String doInBackground(RequestParams... params) {

            if(isAppbaseClientSet) {
                try {
                    return client.prepareSearch(clientType, params[0].requestedQuery).execute().body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                return "Please set Appbase client";
            }
            return defaultResult;
        }
    }

    /**
     * Returns object of DefaultClientSuggestions model to modify the search results as required before setting the DefaultClientSuggestionsAdapter
     *
     * @param searchPropModel Model which is returned on building the search prop
     * @param suggestions List of suggestions
     * @return Object of DefaultClientSuggestions Model
     */
    public DefaultClientSuggestions buildCustomSuggestions (SearchPropModel searchPropModel, ArrayList<String> suggestions) {
        searchPropModel.setAutoSuggest(true);
        defaultSearchPropModel = searchPropModel;
        return new DefaultClientSuggestions(suggestions);
    }

    /**
     * Initiates Analytics Prop
     *
     * @return Analytics builder to set available properties
     */
    public Analytics setAnalyticsProps() {
        return new Analytics();
    }

    /**
     * Provides analytics for the given search id (previous search id by default)
     *
     * @param analyticsModel Analytics Model with all the necessary properties
     * @return Analytics received for the given search id
     */
    public String getAnalytics(AnalyticsModel analyticsModel) {

        if(isAppbaseClientSet) {
            String XSearchId = analyticsModel.getXSearchId();
            boolean XSearchClick = analyticsModel.isXSearchClick();
            boolean XSearchConversion = analyticsModel.isXSearchConversion();
            String XSearchClickPosition = analyticsModel.getXSearchClickPosition();

            String query = "\"X-Search-Click\": \"" + XSearchClick + "\", \"X-Search-Conversion\": \"" + XSearchConversion + "\"";

            if(XSearchClickPosition != null)
                query = query + ", \"X-Search-ClickPosition\": " + XSearchClickPosition + "\"";

            if(XSearchId != null)
                query = query + ", \"X-Search-Id\": " + XSearchId + "\"";

            try {
                return new GetAnalytics().execute(query).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("Error", "Please set Appbase Client");
        }

        return null;
    }

    private class GetAnalytics extends AsyncTask<String,Void,String> {

        String analytics = null;

        @Override
        protected String doInBackground(String... strings) {
            try {
                analytics = client.prepareAnalytics(strings[0]).execute().body().string();
                return analytics;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        private int isSearchBarVisible;
        private int suggestionsVisible;
        private int speechMode;
        private int searchIconRes;
        private int navIconResId;
        private String hint;
        private List suggestions;
        private int maxSuggestions;

        public SavedState(Parcel source) {
            super(source);
            isSearchBarVisible = source.readInt();
            suggestionsVisible = source.readInt();
            speechMode = source.readInt();

            navIconResId = source.readInt();
            searchIconRes = source.readInt();
            hint = source.readString();
            suggestions = source.readArrayList(null);
            maxSuggestions = source.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(isSearchBarVisible);
            out.writeInt(suggestionsVisible);
            out.writeInt(speechMode);

            out.writeInt(searchIconRes);
            out.writeInt(navIconResId);
            out.writeString(hint);
            out.writeList(suggestions);
            out.writeInt(maxSuggestions);
        }
    }
}
