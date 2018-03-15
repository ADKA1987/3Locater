package a3locater.tre.se.a3locater.util;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import a3locater.tre.se.a3locater.domain.UserDetails;

/**
 * Created by alaleiwi001 on 2018-03-15.
 */

public class SendMyLocation  extends AsyncTask<String, Void, Integer> {
    private String createUrl= "https://taptocheckin.herokuapp.com/checkin/mylocation/";
    private UserDetails userDetails;
    private String location;
    public SendMyLocation(String location, UserDetails userDetails){
        this.userDetails = userDetails;
        this.location =location;
    }
    @Override
    protected Integer doInBackground(String... strings) {
        int statusCode=500;
       try{
           URL regUrl = new URL(createUrl);
           HttpURLConnection conn = (HttpURLConnection) regUrl.openConnection();
           conn.setRequestMethod("POST");
           conn.setRequestProperty("Content-Type", "application/json");
           conn.setRequestProperty("Accept","application/json");
           conn.setDoOutput(true);
           conn.setDoInput(true);

           JSONObject jsonParam = new JSONObject();
           jsonParam.put("empId", userDetails.getEmpId());
           jsonParam.put("locationId", location);
           Log.i("JSON", jsonParam.toString());
           OutputStreamWriter os = new OutputStreamWriter (conn.getOutputStream());
           os.write(jsonParam.toString());
           os.flush();
           os.close();
           statusCode =conn.getResponseCode();

           System.out.println("MSG: " +conn.getResponseMessage());
           Log.i("STATUS", String.valueOf(conn.getResponseCode()));
           Log.i("MSG" , String.valueOf(conn.getResponseMessage().toString()));
           conn.disconnect();
       }catch (ProtocolException e) {
           e.printStackTrace();
       } catch (MalformedURLException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       } catch (JSONException e) {
           e.printStackTrace();
       }
        return  statusCode;
    }
}
