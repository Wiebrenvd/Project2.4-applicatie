package com.hanze.recipe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hanze.recipe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BoodschappenFragment extends Fragment {


    private LinearLayout boodschappenlayout;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.boodschappen_fragment, container, false);

        boodschappenlayout = view.findViewById(R.id.boodschappenlijst);
        updateListView();
        addDeleteButtonListener(view);
        addAddButtonListener(view);
        return view;
    }

    private void addAddButtonListener(View view) {

        Button button = view.findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {

                EditText ingredientInput = getFragmentView().findViewById(R.id.ingredientInput);
                addToFile(new File(getContext().getFilesDir(), "list.json"), String.valueOf(ingredientInput.getText()));
                updateListView();
            }
        });
    }

    private View getFragmentView() {
        return view;
    }


    private void addDeleteButtonListener(View view) {
        Button button = view.findViewById(R.id.deleteButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < boodschappenlayout.getChildCount(); i++) {
                    View vw = boodschappenlayout.getChildAt(i);
                    if (vw instanceof CheckBox) {
                        if (((CheckBox) vw).isChecked()) {
                            removeFromFile(((CheckBox) vw).getId());
                        }
                    }
                }

                updateListView();
            }
        });
    }

    private void removeFromFile(int id) {
        File file = new File(getContext().getFilesDir(), "list.json");
        ArrayList<HashMap<String, String>> list = readFile(file);

        Iterator<HashMap<String, String>> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().get("id").equals(String.valueOf(id))) {
                it.remove();
            }
        }

        writeFile(file, String.valueOf(new JSONArray(list)));


    }


    private void updateListView() {
        ArrayList<HashMap<String, String>> map = null;
        if (boodschappenlayout.getChildCount() > 0) {
            boodschappenlayout.removeAllViews();
        }
        try {
            map = fetchList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (HashMap<String, String> ingredient : map) {
            CheckBox checkbox = createCheckbox(ingredient);
            boodschappenlayout.addView(checkbox);

        }

    }

    private CheckBox createCheckbox(HashMap<String, String> ingredient) {
        CheckBox checkbox = new CheckBox(getContext());
        checkbox.setText(ingredient.get("name"));
        checkbox.setId(Integer.parseInt(ingredient.get("id")));
        checkbox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        checkbox.setTextSize(24);
        return checkbox;
    }

    private ArrayList<HashMap<String, String>> fetchList() throws IOException {
        File file = new File(getContext().getFilesDir(), "list.json");
        if (file.createNewFile() || file.length() < 1) {
            writeFile(file, "[]"); // JSONarray begin
        }
        return readFile(file);
    }

    private void writeFile(File file, String content) {

        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<HashMap<String, String>> readFile(File file) {

        ArrayList<HashMap<String, String>> mapArray = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;

            line = bufferedReader.readLine();


            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            String response = stringBuilder.toString();
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));
                HashMap<String, String> map = new HashMap<>();


                try {
                    map.put("id", obj.getString("id"));
                    map.put("name", obj.getString("name"));
                } catch (JSONException e) {

                }


                mapArray.add(map);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        return mapArray;

    }

    public void addToFile(File file, String ingredient) {
        ArrayList<HashMap<String, String>> list = readFile(file);

        ArrayList<Integer> ids = new ArrayList<>();

        for (HashMap<String,String> map : list) {
            ids.add(Integer.valueOf(map.get("id")));
        }

        int id = generateID(ids, 0);



        HashMap<String, String> map = new HashMap<>();

        map.put("id", String.valueOf(id));
        map.put("name", ingredient);
        list.add(map);
        writeFile(file, String.valueOf(new JSONArray(list)));
    }

    private int generateID(ArrayList<Integer> numbers, int id) {
        if (numbers.contains(id)) {
            id += 1;
            return generateID(numbers,id);
        } else {
            return id;
        }

    }



    /*
     * Verkrijgt ingredienten van server. In de vorm van arraylist<map<string,string>>
     * DEZE METHODE IS ALLEEN ALS VOORBEELD VOOR SERVERCONNECTION KLASSE.
     * gebruik andere fetchboodschappenlijstje()!
     */
//    private ArrayList<HashMap<String, String>> fetchBoodschappenLijstje() {
//        ArrayList<HashMap<String, String>> response = null;
//        try {
//            ServerConnection sc = new ServerConnection(new URL("http://192.168.2.8:3000/test")); // Geef url mee
//            response = sc.fetch("country", "id"); // Welke keys wil je uit de response hebben.
//            System.out.println(response);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//
//        return response;
//    }

}
