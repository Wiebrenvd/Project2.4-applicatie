package com.hanze.recipe.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanze.recipe.R;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

class RecipeImageComponent extends LinearLayout {


    private ImageView imageView;
    private String name;
    private String recipeID;
    private TextView textView;

    public RecipeImageComponent(Context context, String recipeID, String name, String image) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        this.recipeID = recipeID;
        this.name = name;

        this.imageView = new ImageView(context);

        imageView.setImageResource(R.mipmap.missing_picture);


            try {
                URL url = new URL(image);

                Bitmap bmp = new AsyncTask<URL, Void, Bitmap>() {
                    @SuppressLint("StaticFieldLeak")
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
                imageView.setImageBitmap(bmp);
            } catch (IOException | NullPointerException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }


        imageView.setAdjustViewBounds(true);
        imageView.setMaxHeight(100);
        imageView.setMaxWidth(100);
        imageView.setLayoutParams(new LayoutParams(500, 500));


        addView(imageView);
        this.textView = new TextView(context);
        addView(textView);

    }


    public String getName() {
        return name;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public void setText(String name) {
        this.textView.setGravity(Gravity.CENTER);
        this.textView.setTextSize(18);
        this.textView.setText(name);

    }

    public void setTextSize(int i) {
        this.textView.setTextSize(i);
    }
}
