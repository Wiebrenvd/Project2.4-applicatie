package com.hanze.recipe.data.components;

import android.content.Context;

public class RecipeTextView extends androidx.appcompat.widget.AppCompatTextView {


    private String name;
    private String recipeID;

    public RecipeTextView(Context context, String recipeID, String name) {
        super(context);
        this.recipeID = recipeID;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getRecipeID() {
        return recipeID;
    }
}
