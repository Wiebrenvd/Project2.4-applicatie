package com.hanze.recipe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hanze.recipe.MainActivity;
import com.hanze.recipe.R;
import com.hanze.recipe.ServerConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inf = inflater.inflate(R.layout.home_fragment, container, false);
        setPopulairReceptenText(inf);
        return inf;
    }

    public void setPopulairReceptenText(View inf) {

        try {
            ServerConnection sc = new ServerConnection(getContext());
            JSONObject response = sc.fetch(new URL(ServerConnection.URL_ROOT + "popular"));
            System.out.println(response);
            JSONArray recipes = response.getJSONArray("recipes");

            for (int i = 0; i < recipes.length() ; i++) {
                JSONObject recipe = recipes.getJSONObject(i);
                LinearLayout popular_recipes = inf.findViewById(R.id.popular_recipes);
                TextView popularRecipe = new TextView(getContext());
                popularRecipe.setText(recipe.getString("name"));
                popularRecipe.setTextSize(30);
                popular_recipes.addView(popularRecipe);
                
            }

        } catch (MalformedURLException | JSONException e) {
            e.printStackTrace();
        }

    }
}
