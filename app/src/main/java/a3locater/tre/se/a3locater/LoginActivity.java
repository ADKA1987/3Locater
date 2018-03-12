package a3locater.tre.se.a3locater;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import a3locater.tre.se.a3locater.domain.UserDetails;
import a3locater.tre.se.a3locater.util.MySingleton;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailView;
    private TextView mLoginError;
    private ProgressDialog progressDialog;
    private Intent intent;
    private String email;
    private String name,id;
    private boolean doubleBackToExitPressedOnce = false;
    static final int READ_BLOCK_SIZE = 500;
    private String userInfo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        boolean fileStatus = alreadyLoggedIn();
        intent = new Intent(getApplicationContext(), MainActivity.class);
        if (fileStatus) {

            intent.putExtra("Email", email);
            intent.putExtra("name", name);
            intent.putExtra("id", "id");
            startActivity(intent);
        }
        mEmailView = findViewById(R.id.email);
        mLoginError =  findViewById(R.id.loginError);

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserLogin();

            }
        });
        //checkUserLogin();
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void checkUserLogin() {
             email = mEmailView.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
                return;
            }else {
                RequestQueue queue  = Volley.newRequestQueue(this);
                String url = "https://taptocheckin.herokuapp.com/checkin/getUserDetails?email="+email;
                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                UserDetails userDetails = null;
                                if (null== response) {
                                    Toast.makeText(getApplicationContext(),"Incorrect Email.", Toast.LENGTH_SHORT).show();
                                    //System.out.println("Response: " + response.toString());
                                }else {
                                    try {
                                        userDetails = new UserDetails(
                                                response.get("empId").toString()
                                                , response.get("name").toString()
                                                , response.get("email").toString()
                                                , response.get("mobileNumber").toString()
                                                , response.get("role").toString()
                                                , response.get("team").toString()
                                                , response.get("profilePic").toString());
                                        System.out.println("userDetails: " + userDetails.toString());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    StringBuffer buf = new StringBuffer();

                                    String data = buf.append(userDetails.getEmpId()).append(",")
                                            .append(userDetails.getName()).append(",")
                                            .append(userDetails.getEmail()).append(",")
                                            .append(userDetails.getMobileNumber()).append(",")
                                            .append(userDetails.getRole()).append(",")
                                            .append(userDetails.getTeam()).append(",")
                                            .append(userDetails.getProfilePic())
                                            .toString();
                                    writeToFile(data);

                                    intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("empId", userDetails.getEmpId());
                                    intent.putExtra("name", userDetails.getName());
                                    intent.putExtra("email", userDetails.getEmail());
                                    intent.putExtra("mobileNumber", userDetails.getMobileNumber());
                                    intent.putExtra("role", userDetails.getRole());
                                    intent.putExtra("team", userDetails.getTeam());
                                    intent.putExtra("profilePic", userDetails.getProfilePic());
                                    startActivity(intent);
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "An error occurred.", Toast.LENGTH_SHORT).show();
                            }
                        });
            // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        }

    }

    private boolean alreadyLoggedIn() {
        boolean fileStatus = false;

            //reading text from file
            try {
                FileInputStream fileIn = openFileInput(Environment.getExternalStorageDirectory()+"/3Locator/mytextfile.txt");

               InputStreamReader InputRead = new InputStreamReader(fileIn);

                char[] inputBuffer = new char[READ_BLOCK_SIZE];

                int charRead;

                while ((charRead = InputRead.read(inputBuffer)) > 0) {
                    // char to string conversion
                    String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                    String[] ar=readstring.split(",");
                    name = ar[0].toString();
                    email  = ar[1].toString();
                    userInfo += readstring;

                }
                InputRead.close();
                fileStatus = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        return fileStatus;
    }
    private void writeToFile(String data) {
        try {
            FileOutputStream fileout =openFileOutput(Environment.getExternalStorageDirectory()+"/3Locator/mytextfile.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(data);
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

