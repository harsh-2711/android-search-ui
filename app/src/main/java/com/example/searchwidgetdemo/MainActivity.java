package com.example.searchwidgetdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.searchwidget.SearchBar;

public class MainActivity extends AppCompatActivity {

    SearchBar searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = (SearchBar) findViewById(R.id.searchBar);
        searchBar.setAppbaseClient("https://scalr.api.appbase.io", "shopify-flipkart-test", "xJC6pHyMz", "54fabdda-4f7d-43c9-9960-66ff45d8d4cf");
        searchBar.startSearch();
    }
}
