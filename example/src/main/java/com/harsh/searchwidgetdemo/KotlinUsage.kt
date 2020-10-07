package com.harsh.searchwidgetdemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.harsh.searchwidget.Builder.DefaultClientSuggestions
import com.harsh.searchwidget.Fragments.VoicePermissionDialogFragment
import com.harsh.searchwidget.Model.ClientSuggestionsModel
import com.harsh.searchwidget.SearchBar
import com.harsh.searchwidget.SearchBar.*
import java.util.*
import java.util.concurrent.ExecutionException


class KotlinUsage : Fragment() {
    private lateinit var searchBar: SearchBar<*, *>
    private lateinit var dataFields: ArrayList<String>
    private lateinit var weights: ArrayList<Int>
    private lateinit var defaultSuggestions: ArrayList<ClientSuggestionsModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_layout, container, false)

        with(view) {
            searchBar = findViewById(R.id.searchBar)
        }

        // Setting basic search prop

        dataFields = ArrayList()
        with(dataFields) {
            add("title")
            add("title.search")
        }


        // Setting weights for dataFields
        weights = ArrayList()
        with(weights) {
            add(1)
            add(3)
        }


        // Making list of default suggestions
        val suggestions = ArrayList<String>()
        with(suggestions) {
            add(getString(R.string.first_default_suggestion))
            add(getString(R.string.second_default_suggestion))
            add(getString(R.string.third_default_suggestion))
        }


        // Making list of default categories to be displayed
        val categories = ArrayList<String>()
        categories.add(getString(R.string.first_default_category))
        categories.add(getString(R.string.second_default_category))

        // Setting extra properties
        val extraProperties = ArrayList<String>()
        extraProperties.add("image")

        // Setting default suggestions
        defaultSuggestions = DefaultClientSuggestions(suggestions).setCategories(categories).build()

        with(searchBar) {
            val searchPropModel = setSearchProp("Demo Widget", dataFields)
                    .setQueryFormat("or")
                    .setHighlight(true)
                    .setCategoryField("tags.keyword")
                    .setInPlaceCategory(false)
                    .setTopEntries(2)
                    .setDefaultSuggestions(defaultSuggestions)
                    .setExtraFields(extraProperties)
                    .build()

            // To log the queries made by Appbase client for debugging
            setMaxSuggestionCount(5)

            // Setting Appbase Client - type is optional here
            setAppbaseClient("https://scalr.api.appbase.io", "shopify-flipkart-test", "xJC6pHyMz", "54fabdda-4f7d-43c9-9960-66ff45d8d4cf", "products")
            setLoggingQuery(true)

            // Setting listener to handle callbacks
            setOnTextChangeListener { response -> // Responses to the queries passed in the Search Bar are available here
                // Parse the response string and add the data in search list respectively
                Log.d("Results", response)
            }

            // Start search
            startSearch(searchPropModel, object : ItemClickListener {
                override fun onClick(view: View, position: Int, result: ClientSuggestionsModel) {
                    Log.d("Click Listener", "CLICKED")

                    // Checking if it should be categorical search
                    if (result.isCategoricalSearch) {
                        val category = result.text.substring(text.length + 4)
                        try {
                            val response = search(searchPropModel, text, category, true)
                            Log.d("Categorical Search", response)
                        } catch (e: ExecutionException) {
                            e.printStackTrace()
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onLongClick(view: View, position: Int, result: ClientSuggestionsModel) {
                    Log.d("Click Listener", "LONG CLICKED")

                    // Checking if it should be categorical search
                    Log.d("Categorical Search", result.isCategoricalSearch.toString())
                }
            })

            // Sets navigation bar icon inside the search bar
            setNavButtonEnabled(true)

            // Sets speech mode to true

            setSpeechMode(true)

            // Managing voice recording permissions on record button click

            setOnSearchActionListener(object : OnSearchActionListener {
                override fun onSearchStateChanged(enabled: Boolean) {}
                override fun onSearchConfirmed(text: CharSequence) {
                    // On search pressed from soft keyboard
                    try {
                        val response = search(searchPropModel, text.toString())
                        Log.d("RESPONSE", response)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } catch (e: ExecutionException) {
                        e.printStackTrace()
                    }
                }

                override fun onButtonClicked(buttonCode: Int) {
                    if (buttonCode == BUTTON_SPEECH) {
                        if (isVoicePermissionGranted) {
                            startVoiceSearch(searchPropModel, object : ItemClickListener {
                                override fun onClick(view: View, position: Int, result: ClientSuggestionsModel) {
                                    // Handle item click events
                                }

                                override fun onLongClick(view: View, position: Int, result: ClientSuggestionsModel) {
                                    // Handle long click events
                                }
                            })
                        } else {
                            childFragmentManager.beginTransaction().add(VoicePermissionDialogFragment(), "Recording Permission").commit()
                        }
                    }
                }
            })
        }

        return view
    }


}