package a3locater.tre.se.a3locater;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import a3locater.tre.se.a3locater.domain.UserDetails;
import a3locater.tre.se.a3locater.util.MySingleton;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailView;
    private TextView mLoginError;
    private ProgressDialog progressDialog;
    private Intent intent;
    private String email;
    private String name,id,mobileNumber,role,team,profilePic;
    private boolean doubleBackToExitPressedOnce = false;
    static final int READ_BLOCK_SIZE = 500;
    private String userInfo = "";
    AnimationDrawable rocketAnimation;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
           addShortcut();

        intent = new Intent(getApplicationContext(), MainActivity.class);
        mEmailView = findViewById(R.id.email);
        mLoginError =  findViewById(R.id.loginError);
        intent = new Intent(getApplicationContext(), MainActivity.class);
        alreadyLoggedIn();

         Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserLogin();

            }
        });

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

                                    String data = buf.append("EmpId;"+userDetails.getEmpId()).append('\n')
                                            .append("Name;"+userDetails.getName()).append('\n')
                                            .append("Email;"+userDetails.getEmail()).append('\n')
                                            .append("MobileNumber;"+userDetails.getMobileNumber()).append('\n')
                                            .append("Role;"+userDetails.getRole()).append('\n')
                                            .append("Team;"+userDetails.getTeam()).append('\n')
                                            .append("ProfilePic;"+userDetails.getProfilePic())
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

    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(), LoginActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "3Locator");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.drawable.tre));

        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addIntent.putExtra("duplicate", false);  //may it's already there so don't duplicate
        getApplicationContext().sendBroadcast(addIntent);
    }
    private void alreadyLoggedIn() {

        BufferedReader br = null;
        FileReader fr = null;
        File file = new File(this.getFilesDir(),"3Locator");
        File gpxfile = new File(file, "mytextfile.txt");
        //String fileName = "mytextfile.txt";
            //reading text from file
            try {
                fr = new FileReader(gpxfile);
                br = new BufferedReader(fr);
                String sCurrentLine;
                StringBuilder details = new StringBuilder();
                Map<String,String> map = new HashMap<>();
                while ((sCurrentLine = br.readLine()) != null) {
                    System.out.println(sCurrentLine);
                    String [] ar = sCurrentLine.split(";");
                    map.put(ar[0],ar[1]);

                }
                id =map.get("EmpId");
                name = map.get("Name");
                email = map.get("Email");
                mobileNumber =  map.get("MobileNumber");
                role =  map.get("Role");
                team =   map.get("Team");
                profilePic =   map.get("ProfilePic");

                intent.putExtra("empId",id);
                intent.putExtra("name",name);
                intent.putExtra("email",email);
                intent.putExtra("mobileNumber",mobileNumber);
                intent.putExtra("role",role);
                intent.putExtra("team",team);
                intent.putExtra("profilePic",profilePic);

                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    private void writeToFile(String data) {

        File file = new File(this.getFilesDir(),"3Locator");

        if(!file.exists()){
            file.mkdir();
        }

        try {
            File gpxfile = new File(file, "mytextfile.txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(data);
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

