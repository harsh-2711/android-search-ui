# appbase-search-widget

### Installation

* Add the Jitpack.io dependency in project level gradle file

```
allprojects {
  repositories {
    ...
    	maven { url 'https://jitpack.io' }
  }
}
```

* Add `appbase-search-widget` dependency in app level gradle file

```
dependencies {
	implementation 'com.github.harsh-2711:appbase-search-widget:tag'
}
```

NOTE: Latest tag is [v0.1.6](https://github.com/harsh-2711/appbase-search-widget/releases/tag/v0.1.6)

### Adding Search Bar into Android project

* Add SearchBar in the XML layout

```
<com.example.searchwidget.SearchBar
        android:id="@+id/searchBar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:maxSuggestionsCount="10"
        app:hint="Search Books"
        app:placeholder="Search" />
```

* Find the SearchBar view in Java file and a basic search bar is ready for implementation

```java
public class MainActivity extends AppCompatActivity {

    SearchBar searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = (SearchBar) findViewById(R.id.searchBar);
    }
}
```

### Example

#### Classic Search Bar

```java
public class MainActivity extends AppCompatActivity {

    SearchBar searchBar;
    private List<String> lastSearches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = (SearchBar) findViewById(R.id.searchBar);

        //restore last queries from disk
        lastSearches = loadSearchSuggestionFromDisk();
        searchBar.setLastSuggestions(list);

        searchBar.setOnSearchActionListener(new SearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                String state = enabled ? "enabled" : "disabled";
                Toast.makeText(getApplicationContext(), "Search bar is " + state, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Toast.makeText(getApplicationContext(), "Search query is: " + text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                switch (buttonCode) {
                    case SearchBar.BUTTON_BACK:
                        Toast.makeText(getApplicationContext(), "Back button pressed", Toast.LENGTH_SHORT).show();
                        break;
                    case SearchBar.BUTTON_NAVIGATION:
                        Toast.makeText(getApplicationContext(), "Open Navigation Drawer", Toast.LENGTH_SHORT).show();
                        break;
                    case SearchBar.BUTTON_SPEECH:
                        // Yet to come
                        Toast.makeText(getApplicationContext(), "Start voice recognition module", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Handle the real time search queries here
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //save last queries to disk
        saveSearchSuggestionToDisk(searchBar.getLastSuggestions());
    }
}

```

#### Appbase Client Search Bar

```java
public class MainActivity extends AppCompatActivity {

    SearchBar searchBar;
    private ArrayList<String> dataFields;

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
        SearchPropModel searchPropModel = searchBar.setSearchProp("Demo Widget", dataFields)
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
        searchBar.startSearch(searchPropModel);
    }
}
```

For more detailed examples, checkout [MainActivity.java](https://github.com/harsh-2711/appbase-search-widget/blob/master/example/src/main/java/com/example/searchwidgetdemo/MainActivity.java) file in [example](https://github.com/harsh-2711/appbase-search-widget/tree/master/example) app

#### Quick Fixes

If you are facing this error

```
error: cannot access RecyclerView
class file for android.support.v7.widget.RecyclerView not found
```

and/or this one

```
error: method does not override or implement a method from a supertype
```

and/or this one
```
error: cannot find symbol constructor ()
```

there should be a problem with the recycler view version in the project. A quick fix would be to add 

```
implementation "com.android.support:recyclerview-v7:28.0.0"
```

in the app level dependencies

### Documentation

The documentation for appbase-search-widget library is hosted on [GitHub pages](https://harsh-2711.github.io/appbase-search-widget/)
