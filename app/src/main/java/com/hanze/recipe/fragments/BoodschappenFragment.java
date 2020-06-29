package com.hanze.recipe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hanze.recipe.R;
import com.hanze.recipe.ServerConnection;
import com.hanze.recipe.ServerConnectionDelete;
import com.hanze.recipe.ServerConnectionPut;

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
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BoodschappenFragment extends Fragment {

    private boolean loggedIn;
    private Spinner spinner;

    private LinearLayout boodschappenlayout;

    private View view;

    private File listFile;

    private File ingredientFile;

    public BoodschappenFragment(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.boodschappen_fragment, container, false);


        this.listFile = new File(getContext().getFilesDir(), "list.json");
        this.ingredientFile = new File(getContext().getFilesDir(), "ingredients.json");

        boodschappenlayout = view.findViewById(R.id.boodschappenlijst);
        spinner = view.findViewById(R.id.spinner);

        setHasOptionsMenu(true);
        updateListView();
        addDeleteButtonListener(view);
        addAddButtonListener(view);

        fetchIngredients();


        return view;
    }

    private void fetchIngredients() {

        ServerConnection sc = new ServerConnection(getContext());
        JSONObject response = null;

        ArrayList<HashMap<String, String>> ingredientArray = null;
        try {
            try {
                response = sc.fetch(new URL(ServerConnection.URL_ROOT + "ingredients"));
            } catch (ConnectException e) {
                e.printStackTrace();
            }
            JSONArray ingredients = response.getJSONArray("ingredients");

            ArrayList<HashMap<String, String>> list = new ArrayList<>();
            for (int i = 0; i < ingredients.length(); i++) {
                JSONObject ingredient = ingredients.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("id", ingredient.getString("id"));
                map.put("name", ingredient.getString("name"));
                list.add(map);
            }

            ingredientArray = list;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                ingredientArray = readFileContent(ingredientFile);


            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        updateSpinner(ingredientArray);


    }

    private void updateSpinner(ArrayList<HashMap<String, String>> ingredientArray) {
        ArrayList<String> spinnerArray = new ArrayList<>();
        for (HashMap<String,String> ingredient : ingredientArray) {
            spinnerArray.add(ingredient.get("name"));
        }


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (loggedIn) {
            inflater.inflate(R.menu.boodschappen_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload:
                try {
                    upload();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.download:
                download();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void upload() throws MalformedURLException {
        JSONArray ingredients = null;
        ingredients = new JSONArray();
        ArrayList<HashMap<String, String>> list = readFile(listFile);

        for (HashMap<String, String> ingredientMap : list) {
            try {
                JSONObject ingredient = new JSONObject();
                ingredient.put("name", ingredientMap.get("name"));
                ingredient.put("amount", ingredientMap.get("amount"));
                ingredients.put(ingredient);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        ServerConnectionDelete scDelete = new ServerConnectionDelete(getContext());
        JSONObject res = scDelete.deleteAllIngredients(new URL(ServerConnection.URL_ROOT + "deleteboodschappenlijstje"));


        if (list.size() != 0) {
            ServerConnectionPut scPut = new ServerConnectionPut(getContext());
            JSONObject resPut = scPut.putBoodschappenlijstje(ingredients, new URL(ServerConnection.URL_ROOT + "boodschappenlijstje"));
        }





    }

    private void download() {
        try {
            ServerConnection sc = new ServerConnection(getContext());
            JSONObject response = sc.fetch(new URL(ServerConnection.URL_ROOT + "boodschappenlijstje"));
            JSONArray ingredients = response.getJSONArray("ingredients");



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

            writeFile(listFile, String.valueOf(new JSONArray(list)));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        }

    }

    private void addAddButtonListener(View view) {

        Button button = view.findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {

                Spinner spinner = getFragmentView().findViewById(R.id.spinner);
                EditText amountInput = getFragmentView().findViewById(R.id.amountInput);
                addToFile(listFile, String.valueOf(spinner.getSelectedItem()), String.valueOf(amountInput.getText()));
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

        ArrayList<HashMap<String, String>> list = readFile(listFile);

        Iterator<HashMap<String, String>> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().get("id").equals(String.valueOf(id))) {
                it.remove();
            }
        }

        writeFile(listFile, String.valueOf(new JSONArray(list)));


    }


    private void updateListView() {
        ArrayList<HashMap<String, String>> map = null;
        if (boodschappenlayout.getChildCount() > 0) {
            boodschappenlayout.removeAllViews();
        }
        try {
            map = readFileContent(listFile);
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
        checkbox.setText(ingredient.get("name") + " - " + ingredient.get("amount"));
        checkbox.setId(Integer.parseInt(ingredient.get("id")));
        checkbox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        checkbox.setTextSize(24);
        return checkbox;
    }

    private ArrayList<HashMap<String, String>> readFileContent(File file) throws IOException {

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
                    map.put("amount", obj.getString("amount"));
                } catch (JSONException e) {

                }


                mapArray.add(map);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        return mapArray;

    }

    public void addToFile(File file, String ingredient, String amount) {
        ArrayList<HashMap<String, String>> list = readFile(file);

        ArrayList<Integer> ids = new ArrayList<>();

        for (HashMap<String, String> map : list) {
            ids.add(Integer.valueOf(map.get("id")));
        }

        int id = generateID(ids, 0);


        HashMap<String, String> map = new HashMap<>();

        map.put("id", String.valueOf(id));
        map.put("name", ingredient);
        map.put("amount", amount);

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
