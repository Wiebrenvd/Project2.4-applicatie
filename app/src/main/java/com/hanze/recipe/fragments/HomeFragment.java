package com.hanze.recipe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hanze.recipe.MainActivity;
import com.hanze.recipe.R;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inf = inflater.inflate(R.layout.home_fragment, container, false);
        setPopulairReceptenText(inf);
        return inf;
    }

    //MOCK DATA
    public void setPopulairReceptenText(View inf) {
        final Bundle bundle = new Bundle();

        TextView tv1 = (TextView) inf.findViewById(R.id.pop_receptText1);
        tv1.setText("• Rijst");
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("recept", "Rijst");
                Fragment recipeFragment = new RecipeFragment();
                recipeFragment.setArguments(bundle);
                MainActivity.getInstance().changeFragment(recipeFragment);
            }
        });
        TextView tv2 = (TextView) inf.findViewById(R.id.pop_receptText2);
        tv2.setText("• Appeltaart");
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("recept", "Appeltaart");
                Fragment recipeFragment = new RecipeFragment();
                recipeFragment.setArguments(bundle);
                MainActivity.getInstance().changeFragment(recipeFragment);
            }
        });
        TextView tv3 = (TextView) inf.findViewById(R.id.pop_receptText3);
        tv3.setText("• Kip");
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("recept", "Kip");
                Fragment recipeFragment = new RecipeFragment();
                recipeFragment.setArguments(bundle);
                MainActivity.getInstance().changeFragment(recipeFragment);
            }
        });
        TextView tv4 = (TextView) inf.findViewById(R.id.pop_receptText4);
        tv4.setText("• Spaghetti");
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("recept", "Spaghetti");
                Fragment recipeFragment = new RecipeFragment();
                recipeFragment.setArguments(bundle);
                MainActivity.getInstance().changeFragment(recipeFragment);
            }
        });
        TextView tv5 = (TextView) inf.findViewById(R.id.pop_receptText5);
        tv5.setText("• Tosti");
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("recept", "Tosti");
                Fragment recipeFragment = new RecipeFragment();
                recipeFragment.setArguments(bundle);
                MainActivity.getInstance().changeFragment(recipeFragment);
            }
        });

    }
}
