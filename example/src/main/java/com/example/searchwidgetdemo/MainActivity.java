package com.example.searchwidgetdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.searchwidget.SearchBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SearchBar searchBar;
    private ArrayList<String> dataFields;
    private ArrayList<Integer> weights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = (SearchBar) findViewById(R.id.searchBar);

        // Setting Appbase Client - type is optional here
        searchBar.setAppbaseClient("https://scalr.api.appbase.io", "shopify-flipkart-test", "xJC6pHyMz", "54fabdda-4f7d-43c9-9960-66ff45d8d4cf", "products");

        // Setting basic search prop
        dataFields = new ArrayList<>();
        dataFields.add("tags");
        dataFields.add("tags.search");

        weights = new ArrayList<>();
        weights.add(1);
        weights.add(3);

        searchBar.setSearchProp("Demo Widget", dataFields)
                .setWeights(weights)
                .setQueryFormat("and")
                .setFuzziness("10")
                .setDebounce(100)
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

        // Start search
        searchBar.startSearch();
    }
}
