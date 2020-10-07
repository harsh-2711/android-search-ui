package com.harsh.searchwidgetdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harsh.searchwidget.Builder.DefaultClientSuggestions;
import com.harsh.searchwidget.Fragments.VoicePermissionDialogFragment;
import com.harsh.searchwidget.Model.AnalyticsModel;
import com.harsh.searchwidget.Model.ClientSuggestionsModel;
import com.harsh.searchwidget.Model.SearchPropModel;
import com.harsh.searchwidget.SearchBar;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class JavaUsage extends Fragment {
    SearchBar<?,?> searchBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout,container,false);

        searchBar = view.findViewById(R.id.searchBar);

        // Setting max suggestions count
        searchBar.setMaxSuggestionCount(5);

        // Setting Appbase Client - type is optional here
        searchBar.setAppbaseClient("https://scalr.api.appbase.io", "shopify-flipkart-test", "xJC6pHyMz", "54fabdda-4f7d-43c9-9960-66ff45d8d4cf", "products");

        // Setting basic search prop
        ArrayList<String> dataFields = new ArrayList<>();
        dataFields.add("title");
        dataFields.add("title.search");

        // Setting weights for dataFields
        ArrayList<Integer> weights = new ArrayList<>();
        weights.add(1);
        weights.add(3);

        // Making list of default suggestions
        ArrayList<String> suggestions = new ArrayList<>();
        suggestions.add(getString(R.string.first_default_suggestion));
        suggestions.add(getString(R.string.second_default_suggestion));
        suggestions.add(getString(R.string.third_default_suggestion));

        // Making list of default categories to be displayed
        ArrayList<String> categories = new ArrayList<>();
        categories.add(getString(R.string.first_default_category));
        categories.add(getString(R.string.second_default_category));

        // Setting extra properties
        ArrayList<String> extraProperties = new ArrayList<>();
        extraProperties.add("image");

        // Setting default suggestions
        ArrayList<ClientSuggestionsModel> defaultSuggestions = new DefaultClientSuggestions(suggestions).setCategories(categories).build();

        final SearchPropModel searchPropModel = searchBar.setSearchProp("Demo Widget", dataFields)
                .setQueryFormat("or")
                .setHighlight(true)
                .setCategoryField("tags.keyword")
                .setInPlaceCategory(false)
                .setTopEntries(2)
                .setDefaultSuggestions(defaultSuggestions)
                .setExtraFields(extraProperties)
                .build();

        // To log the queries made by Appbase client for debugging
        searchBar.setLoggingQuery(true);

        // Setting listener to handle callbacks
        searchBar.setOnTextChangeListener(new SearchBar.TextChangeListener() {
            @Override
            public void onTextChange(String response) {
                // Responses to the queries passed in the Search Bar are available here
                // Parse the response string and add the data in search list respectively
                Log.d("Results", response);
            }
        });

        // Start search
        searchBar.startSearch(searchPropModel, new SearchBar.ItemClickListener() {
            @Override
            public void onClick(View view, int position, ClientSuggestionsModel result) {
                Log.d("Click Listener", "CLICKED");

                // Checking if it should be categorical search
                if(result.isCategoricalSearch()) {
                    String category = result.getText().substring(searchBar.getText().length() + 4);
                    try {
                        String response = searchBar.search(searchPropModel, searchBar.getText(), category, true);
                        Log.d("Categorical Search", response);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position, ClientSuggestionsModel result) {
                Log.d("Click Listener", "LONG CLICKED");

                // Checking if it should be categorical search
                Log.d("Categorical Search", String.valueOf(result.isCategoricalSearch()));
            }
        });

        // Sets navigation bar icon inside the search bar
        searchBar.setNavButtonEnabled(true);

        // Sets speech mode to true
        searchBar.setSpeechMode(true);

        // Managing voice recording permissions on record button click
        searchBar.setOnSearchActionListener(new SearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                // On search pressed from soft keyboard
                try {
                    String response = searchBar.search(searchPropModel, String.valueOf(text));
                    Log.d("RESPONSE", response);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if(buttonCode == SearchBar.BUTTON_SPEECH) {
                    if(searchBar.isVoicePermissionGranted()) {
                        searchBar.startVoiceSearch(searchPropModel, new SearchBar.ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, ClientSuggestionsModel result) {
                                // Handle item click events
                            }

                            @Override
                            public void onLongClick(View view, int position, ClientSuggestionsModel result) {
                                // Handle long click events
                            }
                        });
                    } else {
                        getChildFragmentManager().beginTransaction().add(new VoicePermissionDialogFragment(), "Recording Permission").commit();
                    }
                }
            }
        });

        // Getting Analytics
        AnalyticsModel analyticsModel = searchBar.setAnalyticsProps().setXSearchClick(true).build();
        String analyticsResponse = searchBar.getAnalytics(analyticsModel);
        Log.d("Analytics", analyticsResponse);

    return view;}
}
