package com.hanze.recipe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hanze.recipe.R;

import java.util.ArrayList;

public class RecipeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inf = inflater.inflate(R.layout.recipe_fragment , container, false);
        setReceptNaam(inf);
        setReceptText(inf);
        setIngriedents(inf);
        return inf;
    }

    //MOCK DATA
    public void setReceptNaam(View inf){
        TextView tvReceptNaam = (TextView) inf.findViewById(R.id.receptNaam);
        tvReceptNaam.setText("AppelTaart");

    }
    //MOCK DATA
    public void setReceptText(View inf){
        TextView tvReceptText = (TextView) inf.findViewById(R.id.receptText);
        tvReceptText.setText("Verwarm de oven voor tot 180 °C. Maak een deeg door bloem, boter, suiker en eieren goed door elkaar te kneden. Laat het deeg even rusten. Schil de appels en haal de klokhuizen eruit. Snijd de appels in blokjes en meng die met de suiker en de kaneel. Rol het deeg uit en leg het in een beboterde taartvorm. Schep de vulling hierin en druk goed aan. Gebruik restjes deeg om de bovenkant van de taart te versieren. Bak de taart in de voorverwarmde oven ongeveer 50 minuten tot gaar en goudbruin.");

    }

    //MOCK DATA
    public void setIngriedents(View inf){
        ArrayList<String> ingriedients = new ArrayList<>();
        ingriedients.add("Appel 500g");
        ingriedients.add("Bloem 600g");
        ingriedients.add("Boter 300g");
        ingriedients.add("Kaneel 5g");
        ingriedients.add("Eieren 6");

        String text = "";

        for(int i = 0; i<ingriedients.size(); i++){
            text = text + "• " + ingriedients.get(i) + "\n";
        }

        TextView tvIngriedientsList = inf.findViewById(R.id.ingriedientslist);
        tvIngriedientsList.setText(text);
    }
}
