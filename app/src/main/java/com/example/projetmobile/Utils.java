package com.example.projetmobile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    public static String getDataFromHTTP(String urlString) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            inputStream.close();
            bufferedReader.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result.toString();
    }
    public static String getProfileImageUrlFromJson(String jsonString) {
        try {
            JSONObject userObject = new JSONObject(jsonString);
            JSONObject pictureObject = userObject.getJSONArray("results").getJSONObject(0).getJSONObject("picture");
            return pictureObject.getString("large");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
