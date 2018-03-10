package a3locater.tre.se.a3locater.util;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import a3locater.tre.se.a3locater.MainActivity;
import a3locater.tre.se.a3locater.R;
import a3locater.tre.se.a3locater.domain.EmployeeNames;

/**
 * Created by LENOVO on 3/10/2018.
 */

public class Search extends AsyncTask<String, String, EmployeeNames> {
    private String userName;
    private String url;
    private EmployeeNames employeeName;

    public Search(String url,String userName)  {
        this.userName = userName;
        this.url = url;

    }

    public List<EmployeeNames> getLocation(){
        List<EmployeeNames> employeeNamesList = new ArrayList<>();
        EmployeeNames employeeNames = new EmployeeNames("VR","F04A04D05","Non");
        employeeNamesList.add(employeeNames);
        return employeeNamesList;
    }
    @Override
    protected EmployeeNames doInBackground(String... args) {
        URL regUrl;
        HttpURLConnection urlConnection = null;


        try {

            regUrl = new URL(url+userName);
            urlConnection = (HttpURLConnection) regUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            int responseCode = urlConnection.getResponseCode();
            String responseMessage = urlConnection.getResponseMessage();
            if(responseCode == HttpURLConnection.HTTP_OK){
                String responseString = readStream(urlConnection.getInputStream());
                Log.v("CatalogClient-Response", responseString);
                employeeName = parseEmployeeData(responseString);
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


        return employeeName;
    }

    private EmployeeNames parseEmployeeData(String responseString) {
        EmployeeNames employeeName= null;
        try {

            JSONObject jObj = new JSONObject(responseString);
            String name = jObj.getString("name");
            String location = jObj.getString("location");
            // String isbn = items.getJSONObject(i).getJSONObject("volumeInfo").getJSONArray("industryIdentifiers").getJSONObject(1).getString("identifier");
            //the value of progress is a placeholder here....
            employeeName = new EmployeeNames(name, location, null);

            Log.v("EmployeeNames", "name "+ name + "location "+ location );
        } catch (JSONException e) {
            Log.e("EmployeeNames", "unexpected JSON exception", e);
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
    protected void onPostExecute(EmployeeNames employeeNames) {
        System.out.println(employeeNames.toString());
        super.onPostExecute(employeeNames);
        //make use of data..

    }

}
