package com.hanze.recipe;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequests extends Thread {

    private final String USER_AGENT = "Mozilla/5.0";

    public void run(){
        String output = null;
        try {
            output = sendGET();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(output);
        //return output;
    }

    private String sendGET() throws IOException {
        URL obj = new URL("https://www.android.com");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "json");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            Log.d("thread: ", response.toString());
            System.out.println(response.toString());
            return response.toString();
        } else {
            Log.d("thread: ", "GET request not worked");
            System.out.println("GET request not worked");
        }

        return null;
    }

}