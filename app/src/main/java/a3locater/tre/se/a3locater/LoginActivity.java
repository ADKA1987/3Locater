package a3locater.tre.se.a3locater;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
                String url = "https://fastvedio.herokuapp.com/read/"+email+".";
                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                if (null== response) {
                                    Toast.makeText(getApplicationContext(), (CharSequence) "Incorrect Email.", Toast.LENGTH_SHORT).show();
                                    System.out.println("Response: " + response.toString());
                                }else{
                                    intent = new Intent(getApplicationContext(), MainActivity.class);
                                    try {
                                        intent.putExtra("Email", response.get("email").toString());
                                        intent.putExtra("name", response.get("name").toString());
                                        intent.putExtra("id", response.get("id").toString());
                                        StringBuffer buf = new StringBuffer ();
                                        String data =  buf.append(intent.getSerializableExtra("Email")).append(",").append(intent.getSerializableExtra("name")).toString();
                                        writeToFile(data);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(intent);
                                }
                                System.out.println("Response: " + response.toString());
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
                FileInputStream fileIn = openFileInput("mytextfile.txt");
                InputStreamReader InputRead = new InputStreamReader(fileIn);

                char[] inputBuffer = new char[READ_BLOCK_SIZE];

                int charRead;

                while ((charRead = InputRead.read(inputBuffer)) > 0) {
                    // char to string conversion
                    String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                    String[] ar=readstring.split(",");
                    email = ar[0].toString();
                    name = ar[1].toString();
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
            FileOutputStream fileout =openFileOutput("mytextfile.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(data);
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

