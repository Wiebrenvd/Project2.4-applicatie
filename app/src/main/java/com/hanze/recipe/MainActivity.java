package com.hanze.recipe;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.hanze.recipe.fragments.BoodschappenFragment;
import com.hanze.recipe.fragments.HomeFragment;
import com.hanze.recipe.fragments.LoginFragment;
import com.hanze.recipe.fragments.RegisterFragment;
import com.hanze.recipe.fragments.SearchFragment;

import org.json.JSONObject;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    public NavigationView nv;
    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupDrawer();
        setupNavigation();

        changeFragment(new HomeFragment());
        instance = this;
        try {
            verifyUser();
        } catch (MalformedURLException | ConnectException e) {
            e.printStackTrace();
        }

    }

    public void verifyUser() throws MalformedURLException, ConnectException {
        ServerConnection sc = new ServerConnection(getBaseContext());
        JSONObject res = sc.fetch(new URL(ServerConnection.URL_ROOT + "verify"));
        if(res == null){
            Log.d("JWT", "Failed");
            LoginFragment.loggin = false;
            LoginFragment.loginStatusChanged(false);
        }else{
            Log.d("JWT", "Success");
            LoginFragment.loggin = true;
            LoginFragment.loginStatusChanged(true);
        }
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                    case R.id.search:
                        changeFragment(new SearchFragment());
                        dl.closeDrawers();
                        break;
                    case R.id.boodschappen:
                        if (LoginFragment.loggin == true) {
                            changeFragment(new BoodschappenFragment(true));
                        } else {
                            changeFragment(new BoodschappenFragment(false));
                        }
                        dl.closeDrawers();
                        break;
                    case R.id.loginMenu:
                        if (LoginFragment.loggin == true) {
                            LoginFragment.loggin = false;
                            changeFragment(new LoginFragment());
                        } else {
                            changeFragment(new LoginFragment());
                        }
                        dl.closeDrawers();
                        break;
                    case R.id.registerMenu:
                        changeFragment(new RegisterFragment());
                        dl.closeDrawers();
                        break;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }


}
