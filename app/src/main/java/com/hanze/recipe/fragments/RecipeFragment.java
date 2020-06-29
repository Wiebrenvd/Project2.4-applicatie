package com.hanze.recipe.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.hanze.recipe.R;
import com.hanze.recipe.ServerConnection;
import com.hanze.recipe.data.components.TimerComponent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RecipeFragment extends Fragment {

    private String recipeID;
    private View inf;
    private ArrayList<Chronometer> timerArray = new ArrayList<>();


    public RecipeFragment() {
    }

    public RecipeFragment(String recipeID) {
        this.recipeID = recipeID;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inf = inflater.inflate(R.layout.recipe_fragment, container, false);
        this.inf = inf;


        if (recipeID != null) {
            fetchRecipe(Integer.parseInt(recipeID));
        }


        return inf;
    }

    private void fetchRecipe(int i) {
        try {
            ServerConnection sc = new ServerConnection(getContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                addRecipeData(sc.fetch(new URL(ServerConnection.URL_ROOT + "recept/" + i)));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("StaticFieldLeak")
    private void addRecipeData(JSONObject response) {
        System.out.println(response);
        String image = null;

        try {
            image = response.getString("image");
            if (image == null) {


                URL url = new URL(image);

                Bitmap bmp = new AsyncTask<URL, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(URL... url) {

                        Bitmap bmp = null;
                        try {
                            bmp = BitmapFactory.decodeStream(url[0].openConnection().getInputStream());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return bmp;
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url).get();

                ImageView imageView = inf.findViewById(R.id.recipe_image);
                imageView.setImageBitmap(bmp);
            }

        } catch (JSONException | IOException | NullPointerException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        TextView name = inf.findViewById(R.id.recipe_name);
        try {
            name.setText(response.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView desc = inf.findViewById(R.id.recipe_desc);
        try {
            desc.setText(response.getString("desc"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray timers = response.getJSONArray("timers");
            LinearLayout timersLayout = inf.findViewById(R.id.timers_layout);
            for (int i = 0; i < timers.length(); i++) {
                TimerComponent timer = new TimerComponent(getContext(), timers.getInt(i));
                timersLayout.addView(timer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray ingredients = response.getJSONArray("ingredients");
            LinearLayout ingredientLayout = inf.findViewById(R.id.ingredient_layout);
            for (int i = 0; i < ingredients.length(); i++) {
                TextView ingredient = new TextView(getContext());
                ingredient.setText(String.format("%s %s", ingredients.getJSONObject(i).getString("name"), ingredients.getJSONObject(i).getString("amount")));
                ingredientLayout.addView(ingredient);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}

