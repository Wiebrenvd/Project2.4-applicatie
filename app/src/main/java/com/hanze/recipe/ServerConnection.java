package com.hanze.recipe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.concurrent.ExecutionException;

public class ServerConnection extends AsyncTask<URL, Void, JSONObject> {

    public static final String URL_ROOT = "http://192.168.1.5:3000/";
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public ServerConnection(Context context) {
        this.context = context;
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
    protected JSONObject doInBackground(URL... urlParam) {
        try {
            HttpURLConnection con = null;
            URL url = urlParam[0];
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            SharedPreferences pref = context.getSharedPreferences("pref", 0); // 0 - for private mode
            if (pref.getString("jwt", null) != null) {
                System.out.println(pref.getString("jwt", null));
                con.addRequestProperty("Authorization", pref.getString("jwt", null));
            }

            System.out.println(con.getInputStream());
            String server_response = readStream(con.getInputStream());
            JSONObject response = new JSONObject(server_response);
            Log.d("Response", String.valueOf(response));
            saveJWT(response);

            return response;
        } catch (ConnectException e) {
            System.out.println("Connection failed");
            return new JSONObject();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String encryptPassword(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }


    private boolean saveJWT(JSONObject response) throws JSONException {
        try {
            String token = response.getString("token");

            SharedPreferences pref = context.getSharedPreferences("pref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("jwt", token);
            editor.apply();
            return true;
        } catch (JSONException e) {
            return false;
        }
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


    public JSONObject fetch(URL url) {
        try {
            return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
