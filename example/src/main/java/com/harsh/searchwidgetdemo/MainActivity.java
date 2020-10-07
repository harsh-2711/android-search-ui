package com.harsh.searchwidgetdemo;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    JavaUsage javaUsage;
    KotlinUsage kotlinUsage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting Views
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Adding Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        //Creating instances of fragments
        javaUsage = new JavaUsage();
        kotlinUsage = new KotlinUsage();

        //Loading Java Fragment by Default
        loadFragment(javaUsage);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.java:
                loadFragment(javaUsage);
                return true;
            case R.id.kotlin:
                loadFragment(kotlinUsage);
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        // Function To load Fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
