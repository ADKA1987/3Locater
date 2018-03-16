package a3locater.tre.se.a3locater.util;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import a3locater.tre.se.a3locater.domain.EmployeeLocation;

/**
 * Created by LENOVO on 3/10/2018.
 */

public class Search extends AsyncTask<String, String, EmployeeLocation> {
    private String userName;
    private String url;
    private EmployeeLocation employeeLocation;

    public Search(String url,String userName)  {
        this.userName = userName;
        this.url = url;

    }
    @Override
    protected EmployeeLocation doInBackground(String... args) {
        URL regUrl;
        HttpURLConnection urlConnection = null;
        try {
            regUrl = new URL(url+userName);
            urlConnection = (HttpURLConnection) regUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            Log.v("UrlConnection",  urlConnection.getContent().toString());
            int responseCode = urlConnection.getResponseCode();
            String responseMessage = urlConnection.getResponseMessage();
            if(responseCode == HttpURLConnection.HTTP_OK){
                String responseString = readStream(urlConnection.getInputStream());
                Log.v("CatalogClient-Response", responseString);
                employeeLocation = parseEmployeeData(responseString);
            }else{
                Log.v("CatalogClient", "Response code:"+ responseCode);
                Log.v("CatalogClient", "Response message:"+ responseMessage);
            }
        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }


        return employeeLocation;
    }

    private EmployeeLocation parseEmployeeData(String responseString) {
        EmployeeLocation employeeName= null;
        try {

            JSONObject jObj = new JSONObject(responseString);
            String name = jObj.getString("name");
            String location = jObj.getString("locationId");
            String locationImage = jObj.getString("floorPlan");
            String profilePic = jObj.getString("profilePic");
            // String isbn = items.getJSONObject(i).getJSONObject("volumeInfo").getJSONArray("industryIdentifiers").getJSONObject(1).getString("identifier");
            //the value of progress is a placeholder here....
            employeeName = new EmployeeLocation(name, location, locationImage,profilePic);

            Log.v("EmployeeLocation", "name "+ name + ", location "+ location+", locationImage "+locationImage+ ", profilePic"+ profilePic);
        } catch (JSONException e) {
            Log.e("EmployeeLocation", "unexpected JSON exception", e);
        }

        return employeeName;
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
    @Override
    protected void onPostExecute(EmployeeLocation employeeLocation) {
        if (null == employeeLocation){
            employeeLocation = new EmployeeLocation("Not Found","Not Found","Not Found", "Not Found");
        }
        System.out.println(employeeLocation.toString());
        super.onPostExecute(employeeLocation);
        //make use of data..

    }

}
