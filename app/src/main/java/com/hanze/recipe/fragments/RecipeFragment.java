package com.hanze.recipe.fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hanze.recipe.R;

import java.util.ArrayList;

public class RecipeFragment extends Fragment {

    private boolean running;
    private Chronometer chronometer;
    private long pauseOffset;
    static public String currentRecept;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inf = inflater.inflate(R.layout.recipe_fragment, container, false);
        try {
            currentRecept = getArguments().getString("recept");
        }
        catch(Exception e) {
            //currentRecept = "";
        }
        setReceptNaam(inf);
        setReceptText(inf);
        setIngriedents(inf);
        setTimers(inf,1);
        return inf;
    }

    //MOCK DATA
    public void setReceptNaam(View inf) {
        TextView tvReceptNaam = (TextView) inf.findViewById(R.id.receptNaam);
        tvReceptNaam.setText(currentRecept);

    }

    //MOCK DATA
    public void setReceptText(View inf) {
        TextView tvReceptText = (TextView) inf.findViewById(R.id.receptText);
        tvReceptText.setText("Verwarm de oven voor tot 180 °C. Maak een deeg door bloem, boter, suiker en eieren goed door elkaar te kneden. Laat het deeg even rusten. Schil de appels en haal de klokhuizen eruit. Snijd de appels in blokjes en meng die met de suiker en de kaneel. Rol het deeg uit en leg het in een beboterde taartvorm. Schep de vulling hierin en druk goed aan. Gebruik restjes deeg om de bovenkant van de taart te versieren. Bak de taart in de voorverwarmde oven ongeveer 50 minuten tot gaar en goudbruin.");
    }

    //MOCK DATA
    public void setIngriedents(View inf) {
        ArrayList<String> ingriedients = new ArrayList<>();
        ingriedients.add("Appel 500g");
        ingriedients.add("Bloem 600g");
        ingriedients.add("Boter 300g");
        ingriedients.add("Kaneel 5g");
        ingriedients.add("Eieren 6");

        String text = "";

        for (int i = 0; i < ingriedients.size(); i++) {
            text = text + "• " + ingriedients.get(i) + "\n";
        }

        TextView tvIngriedientsList = inf.findViewById(R.id.ingriedientslist);
        tvIngriedientsList.setText(text);
    }

    //MOCK DATA
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setTimers(View inf, int amount) {
        TableRow tableRow1 = inf.findViewById(R.id.TableRow_timer1);
        TableRow tableRow2 = inf.findViewById(R.id.TableRow_timer2);
        TableRow tableRow3 = inf.findViewById(R.id.TableRow_timer3);
        switch (amount) {
            case 1:
                tableRow2.setVisibility(View.GONE);
                tableRow3.setVisibility(View.GONE);
                break;

            case 2:
                tableRow3.setVisibility(View.GONE);
                break;
            case 3:
                break;
            default:
                tableRow1.setVisibility(View.GONE);
                tableRow2.setVisibility(View.GONE);
                tableRow3.setVisibility(View.GONE);
                break;
        }

        long nr_of_min = 5;
        long nr_of_sec = 0;

        final Chronometer chronometer1 = inf.findViewById(R.id.chronometer1);
        chronometer1.setCountDown(true);
        chronometer1.setFormat("Time: %s");
        chronometer1.setBase(SystemClock.elapsedRealtime() + (nr_of_min * 60000 + nr_of_sec * 1000));

        final Chronometer chronometer2 = inf.findViewById(R.id.chronometer2);
        chronometer2.setCountDown(true);
        chronometer2.setFormat("Time: %s");
        chronometer2.setBase(SystemClock.elapsedRealtime() + (nr_of_min * 60000 + nr_of_sec * 1000));

        final Chronometer chronometer3 = inf.findViewById(R.id.chronometer3);
        chronometer3.setCountDown(true);
        chronometer3.setFormat("Time: %s");
        chronometer3.setBase(SystemClock.elapsedRealtime() + (nr_of_min * 60000 + nr_of_sec * 1000));

        //start timer button
        Button startButton = inf.findViewById(R.id.startbutton1);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!running) {
                    chronometer1.setBase(chronometer1.getBase());
                    chronometer1.start();
                    running = true;
                }
            }
        });

        //pause timer button
        Button pauseButton = inf.findViewById(R.id.pausebutton1);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running) {
                    chronometer1.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer1.getBase();
                    running = false;
                }
            }
        });

        //start timer button
        Button startButton2 = inf.findViewById(R.id.startbutton2);
        startButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!running) {
                    chronometer2.setBase(chronometer2.getBase());
                    chronometer2.start();
                    running = true;
                }
            }
        });

        //pause timer button
        Button pauseButton2 = inf.findViewById(R.id.pausebutton2);
        pauseButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running) {
                    chronometer2.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer2.getBase();
                    running = false;
                }
            }
        });

        //start timer button
        Button startButton3 = inf.findViewById(R.id.startbutton3);
        startButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!running) {
                    chronometer3.setBase(chronometer3.getBase());
                    chronometer3.start();
                    running = true;
                }
            }
        });

        //pause timer button
        Button pauseButton3 = inf.findViewById(R.id.pausebutton3);
        pauseButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running) {
                    chronometer3.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer3.getBase();
                    running = false;
                }
            }
        });
    }
}
