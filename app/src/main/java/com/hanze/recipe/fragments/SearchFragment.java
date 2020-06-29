package com.hanze.recipe.fragments;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hanze.recipe.MainActivity;
import com.hanze.recipe.R;
import com.hanze.recipe.ServerConnection;
import com.hanze.recipe.data.components.RecipeTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class SearchFragment extends Fragment {


    private View view;
    private EditText searchbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment, container, false);

        init();
        return view;
    }

    private void init() {

        this.searchbar = this.view.findViewById(R.id.search_bar);

        Button searchButton = this.view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchResults();
                MainActivity.hideKeyboardFrom(getContext(),view);
            }
        });

    }

    private void fetchResults() {
        try {
            ServerConnection sc = new ServerConnection(getContext());
            String queries = "?searchString=" + this.searchbar.getText();
            JSONObject response = sc.fetch(new URL(ServerConnection.URL_ROOT + "zoek/" + queries));
            System.out.println(response);

            try {
                JSONArray recipes = response.getJSONArray("recipes");

                for (int i = 0; i < recipes.length(); i++) {
                    LinearLayout recipeLayout = new LinearLayout(getContext());
                    recipeLayout.setOrientation(LinearLayout.HORIZONTAL);
                    JSONObject recipe = recipes.getJSONObject(i);

                    RecipeTextView recipeTextView = new RecipeTextView(getContext(), recipe.getString("id"), recipe.getString("name"));
                    recipeTextView.setText(recipe.getString("name"));
                    recipeTextView.setTextSize(30);
                    recipeTextView.setClickable(true);
                    recipeTextView.setFocusable(true);

                    TypedValue outValue = new TypedValue();
                    getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                    recipeTextView.setBackgroundResource(outValue.resourceId);

                    recipeTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RecipeTextView recipe = (RecipeTextView) v;
                            ((MainActivity) getActivity()).changeFragment(new RecipeFragment(recipe.getRecipeID()));
                        }
                    });

                    recipeLayout.addView(recipeTextView);

                    LinearLayout recipesLayout = view.findViewById(R.id.result_layout);
                    recipesLayout.addView(recipeLayout);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
