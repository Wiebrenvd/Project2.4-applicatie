package com.hanze.recipe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.hanze.recipe.fragments.BoodschappenFragment;
import com.hanze.recipe.fragments.HomeFragment;
import com.hanze.recipe.fragments.RecipeFragment;
import com.hanze.recipe.fragments.SearchFragment;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupDrawer();
        setupNavigation();
        changeFragment(new HomeFragment());
        instance = this;

    }



    public static MainActivity getInstance() {
        return instance;
    }

    private void setupNavigation() {
        nv = findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        changeFragment(new HomeFragment());
                        dl.closeDrawers();
                        break;
                    case R.id.recipe:
                        changeFragment(new RecipeFragment());
                        dl.closeDrawers();
                        break;
                    case R.id.boodschappen:
                        changeFragment(new BoodschappenFragment());
                        dl.closeDrawers();
                        break;
                    case R.id.search:
                        changeFragment(new SearchFragment());
                        dl.closeDrawers();
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    private void setupDrawer() {
        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close); // Deze 2 strings zijn voor accessibility
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}
