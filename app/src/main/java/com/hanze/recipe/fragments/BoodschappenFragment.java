package com.hanze.recipe.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.hanze.recipe.ServerConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BoodschappenFragment extends Fragment {


    private LinearLayout boodschappenlayout;

    private View view;

    private File file;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.boodschappen_fragment, container, false);
        setHasOptionsMenu(true);
        boodschappenlayout = view.findViewById(R.id.boodschappenlijst);
        updateListView();
        addDeleteButtonListener(view);
        addAddButtonListener(view);

        this.file = new File(getContext().getFilesDir(), "list.json");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.boodschappen_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload:
                upload();
                break;
            case R.id.download:
                download();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void upload() {

        ArrayList<HashMap<String, String>> list = readFile(file);

        // TODO


    }

    private void download() {


        try {
            ServerConnection sc = new ServerConnection(getContext());
            JSONObject response = sc.fetch(new URL(ServerConnection.URL_ROOT + "boodschappenlijstje"));
            JSONArray ingredients = response.getJSONArray("ingredients");


            System.out.println(response);

            ArrayList<HashMap<String, String>> list = new ArrayList<>();
            for (int i = 0; i < ingredients.length(); i++) {
                JSONObject ingredient = ingredients.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("id", ingredient.getString("id"));
                map.put("name", ingredient.getString("name"));
                map.put("amount", ingredient.getString("amount"));
                list.add(map);
            }


            this.boodschappenlayout.removeAllViews();

            for (HashMap<String, String> map : list) {

                boodschappenlayout.addView(createCheckbox(map));
            }

            writeFile(file, String.valueOf(new JSONArray(list)));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

        for (HashMap<String, String> map : list) {
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
            return generateID(numbers, id);
        } else {
            return id;
        }

    }

}
