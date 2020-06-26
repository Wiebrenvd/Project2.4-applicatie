package com.hanze.recipe;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequests {

    private final String USER_AGENT = "Mozilla/5.0";

    private final String GET_URL = "https://localhost:9090/SpringMVCExample";

    public String createRequest() throws IOException {

        String output = sendGET();
        System.out.println("GET DONE");
        return output;
    }

    private String sendGET() throws IOException {
        URL obj = new URL("https://www.android.com");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        int responseCode = con.getResponseCode();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            System.out.println("GET request not worked");
        }

        return null;
    }

}