package com.harsh.searchwidgetdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.harsh.searchwidget.Builder.DefaultClientSuggestions;
import com.harsh.searchwidget.Model.ClientSuggestionsModel;
import com.harsh.searchwidget.Model.SearchPropModel;
import com.harsh.searchwidget.SearchBar;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SearchBar searchBar;
    private ArrayList<String> dataFields;
    private ArrayList<Integer> weights;
    private ArrayList<ClientSuggestionsModel> defaultSuggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = (SearchBar) findViewById(R.id.searchBar);

        // Setting max suggestions count
        searchBar.setMaxSuggestionCount(5);

        // Setting Appbase Client - type is optional here
        searchBar.setAppbaseClient("https://scalr.api.appbase.io", "shopify-flipkart-test", "xJC6pHyMz", "54fabdda-4f7d-43c9-9960-66ff45d8d4cf", "products");

        // Setting basic search prop
        dataFields = new ArrayList<>();
        dataFields.add("title");
        dataFields.add("title.search");

        // Setting weights for dataFields
        weights = new ArrayList<>();
        weights.add(1);
        weights.add(3);

        // Making list of default suggestions
        ArrayList<String> suggestions = new ArrayList<>();
        suggestions.add("Puma T-Shirt");
        suggestions.add("Apple iPhone XS");
        suggestions.add("Nike Trousers");

        // Making list of default categories to be displayed
        ArrayList<String> categories = new ArrayList<>();
        categories.add("T-Shirt");
        categories.add("Mobiles");

        // Setting extra properties
        ArrayList<String> extraProperties = new ArrayList<>();
        extraProperties.add("images");

        // Setting default suggestions
        defaultSuggestions = new DefaultClientSuggestions(suggestions).setCategories(categories).build();

        SearchPropModel searchPropModel = searchBar.setSearchProp("Demo Widget", dataFields)
                .setQueryFormat("or")
                .setFuzziness("10")
                .setDebounce(100)
                .setHighlight(true)
                .setCategoryField("tags")
                .setTopEntries(2)
                .setRedirectIcon(false)
                .setDefaultSuggestions(defaultSuggestions)
                .setExtraFields(extraProperties)
                .build();

        // To log the queries made by Appbase client for debugging
         searchBar.setLoggingQuery(true);

        // Setting listener to handle callbacks
        searchBar.setOnTextChangeListner(new SearchBar.TextChangeListener() {
            @Override
            public void onTextChange(String response) {
                // Responses to the queries passed in the Search Bar are available here
                // Parse the response string and add the data in search list respectively
                Log.d("Results", response);
            }
        });

        // Setting click gestures on search results
        searchBar.setOnItemClickListener(new SearchBar.ItemClickListener() {
            @Override
            public void onClick(View view, int position, ClientSuggestionsModel result) {
                Log.d("Search Result", result.getText());
                HashMap<String, ArrayList<String>> hashMap = result.getExtraProperties();
                try {
                    Log.d("Extra property", hashMap.get("images").toString());
                } catch (NullPointerException e) {
                    //e.printStackTrace();
                }
            }

            @Override
            public void onLongClick(View view, int position, ClientSuggestionsModel result) {
                Log.d("Search Result", result.getText());
            }
        });

        // Start search
        searchBar.startSearch(searchPropModel);

        // Sets navigation bar icon inside the search bar
        searchBar.setNavButtonEnabled(true);
    }
}
