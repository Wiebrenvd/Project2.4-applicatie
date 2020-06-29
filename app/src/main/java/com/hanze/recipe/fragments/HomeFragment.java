package com.hanze.recipe.fragments;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

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

            ViewGroup popular_recipes = inf.findViewById(R.id.recipes_layout);


            for (int i = 0; i < recipes.length(); i++) {


                JSONObject recipe = recipes.getJSONObject(i);
                RecipeImageComponent popularRecipe = new RecipeImageComponent(getContext(), recipe.getString("id"), recipe.getString("name"), recipe.getString("picture"));
                popularRecipe.setText(String.valueOf(i+1) + ". " + recipe.getString("name"));
                popularRecipe.setClickable(true);
                popularRecipe.setFocusable(true);

                TypedValue outValue = new TypedValue();
                getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                popularRecipe.setBackgroundResource(outValue.resourceId);

                popularRecipe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecipeImageComponent recipe = (RecipeImageComponent) v;
                        ((MainActivity) getActivity()).changeFragment(new RecipeFragment(recipe.getRecipeID()));
                    }
                });

                TableRow row;
                switch (i) {
                    case 0:
                    case 1:
                        row = inf.findViewById(R.id.row1);
                        row.addView(popularRecipe);
                        break;
                    case 2:
                    case 3:
                        row = inf.findViewById(R.id.row2);
                        row.addView(popularRecipe);
                        break;
                    case 4:
                    case 5:
                        row = inf.findViewById(R.id.row3);
                        row.addView(popularRecipe);
                        break;


                }



            }

        } catch (MalformedURLException | JSONException e) {
            e.printStackTrace();
        }

    }
}
