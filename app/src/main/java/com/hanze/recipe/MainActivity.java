package com.hanze.recipe;

import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;


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
        runServerTest();



    }

    private void runServerTest() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL("http://192.168.1.5:3000/test");

                    HttpURLConnection myConnection =
                            (HttpURLConnection) url.openConnection();


                    if (myConnection.getResponseCode() == 200) {
                        String server_response = readStream(myConnection.getInputStream());
                        JSONArray jObject = new JSONArray(server_response);
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
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
