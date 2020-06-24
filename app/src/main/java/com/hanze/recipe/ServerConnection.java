package com.hanze.recipe;

import android.content.Context;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ServerConnection extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

    private URL url;
    private ArrayList<TextView> textViews = new ArrayList<>();

    public ServerConnection(URL url) {
        this.url = url;
    }

    /*
     * Doet een request naar de gegeven URL. Haalt alleen de keys eruit die je aan fetch meegeeft. Verandert de verkregen JSON naar een array van maps.
     *
     * Voorbeeld:
     * [{"idIngredient":2,"ingredientName":"Pasta", "country":"Netherlands"}, {"idIngredient":3,"ingredientName":"Rijst"}]
     * wordt
     * ArrayList( HashMap( "idIngredient":2,"ingredientName":"Pasta" ), HashMap( "idIngredient":3,"ingredientName":"Rijst" ) )
     *
     *
     */
    @Override
    protected ArrayList<HashMap<String, String>> doInBackground(String... keys) {
        try {
            HttpURLConnection myConnection =
                    (HttpURLConnection) url.openConnection();

            ArrayList<HashMap<String, String>> mapArray = new ArrayList<>();

            if (myConnection.getResponseCode() == 200) {
                String server_response = readStream(myConnection.getInputStream());
                JSONArray jsonArray = new JSONArray(server_response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<>();
                    JSONObject obj = jsonArray.getJSONObject(i);


                    for (int j = 0; j < keys.length; j++) {
                        try {
                            map.put(keys[j], obj.getString(keys[j]));
                        } catch (JSONException e) {
                            System.out.println(keys[j] + "cant be found");
                        }
                    }

                    mapArray.add(map);
                }
            }

            return mapArray;
        } catch (ConnectException e) {
            System.out.println("Connection failed");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }


    public ArrayList<HashMap<String, String>> fetch(String... keys) {
        try {
            return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, keys).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
