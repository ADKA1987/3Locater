package a3locater.tre.se.a3locater.util;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import a3locater.tre.se.a3locater.domain.FreeSeatResponse;
import a3locater.tre.se.a3locater.domain.Location;

public class FreeLocations extends AsyncTask<String, String, Location> {

    private String userName;
    private String url;
    private Location locations;

    public FreeLocations(String url, String userName) {
        this.userName = userName;
        this.url = url;
    }


    @Override
    protected Location doInBackground(String... args) {
        Log.v("I AM INNNNNNNNNNNNN", "dummy");
        System.out.println("INNNNNNNNNNNNNNN");
        URL regUrl;
        HttpURLConnection urlConnection = null;
        try {
            Log.v("I AM INNNNNNNNNNNNN", "URL");
            regUrl = new URL(url);
            urlConnection = (HttpURLConnection) regUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            Log.v("UrlConnection", urlConnection.getContent().toString());
            int responseCode = urlConnection.getResponseCode();
            String responseMessage = urlConnection.getResponseMessage();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String responseString = readStream(urlConnection.getInputStream());
                Log.v("CatalogClient-Response", responseString);
                locations = parseLocationData(responseString);
            } else {
                Log.v("CatalogClient", "Response code:" + responseCode);
                Log.v("CatalogClient", "Response message:" + responseMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }


        return locations;
    }

    private Location parseLocationData(String responseString) {
        Gson gson = new Gson();
        Type type = new TypeToken<FreeSeatResponse>() {
        }.getType();
        FreeSeatResponse freeSeatResponse = gson.fromJson(responseString, type);
        Log.v("test floor ", freeSeatResponse.getLocation().getFloors().get(0));
        return freeSeatResponse.getLocation();
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


}