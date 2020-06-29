package com.hanze.recipe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.concurrent.ExecutionException;

public class ServerConnectionPost extends AsyncTask<URL, Void, JSONObject> {

    public static final String URL_ROOT = "http://192.168.1.5:3000/";
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String resp;
    public ServerConnectionPost(Context context) {
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
            con.setRequestMethod("POST"); // PUT is another valid option
            con.setDoOutput(true);
            byte[] out = resp.getBytes(StandardCharsets.UTF_8);

            int length = out.length;

            con.setFixedLengthStreamingMode(length);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.connect();
            try (OutputStream os = con.getOutputStream()) {
                os.write(out);

            }

            System.out.println(con.getInputStream());
            String server_response = readStream(con.getInputStream());
            JSONObject response = new JSONObject(server_response);
            saveJWT(response);

            return response;

        } catch (ConnectException e) {
            System.out.println("Connection failed");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject fetchLogin(String email, String password, URL... urlParam) throws MalformedURLException {
        String passwordHash = encryptPassword(password);
        resp = "{\"email\":\""+ email +"\",\"password\":\"" + passwordHash + "\"}";
        return fetch(urlParam[0]);
    }

    public JSONObject fetchRegister(String username, String email, String password, URL... urlParam) throws MalformedURLException {
        String passwordHash = encryptPassword(password);
        resp = "{\"username\":\""+ username +"\",\"email\":\""+ email +"\",\"password\":\"" + passwordHash + "\"}";
        System.out.println(resp);
        return fetch(urlParam[0]);
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
