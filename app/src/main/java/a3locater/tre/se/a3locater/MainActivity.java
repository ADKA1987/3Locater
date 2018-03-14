package a3locater.tre.se.a3locater;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.net.URL;

import a3locater.tre.se.a3locater.domain.UserDetails;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private Context mContext;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    private boolean doubleBackToExitPressedOnce = false;
    public static final String TAG = "NfcDemo";
    private TextView floorTextView, areaTextView,deskTextView;
    private TextView userEmail,userName;
    private Intent intent;
    private NavigationView navigationView;
    private View headerView;
    private NfcAdapter mNfcAdapter;
    private WebView webview;
    private UserDetails userDetails;
    private String createUrl= "https://taptocheckin.herokuapp.com/checkin/mylocation/";
    private ImageView userImageNav;
    private int responseStatus;
    private DrawerLayout mDrawerLayout;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         mContext = getApplicationContext();
        Intent mIntent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         ActionBar actionbar = getSupportActionBar();
         actionbar.setDisplayHomeAsUpEnabled(true);
         actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

         mDrawerLayout = findViewById(R.id.drawer_layout);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        navigationView =  findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
         mContext = this.getApplication();
        userDetails = new UserDetails(
                 mIntent.getSerializableExtra("empId").toString()
                 , mIntent.getSerializableExtra("name").toString()
                 , mIntent.getSerializableExtra("email").toString()
                 , mIntent.getSerializableExtra("mobileNumber").toString()
                 , mIntent.getSerializableExtra("role").toString()
                 , mIntent.getSerializableExtra("team").toString()
                 , mIntent.getSerializableExtra("profilePic").toString());
        userEmail =   headerView.findViewById(R.id.userEmail);
        userName =   headerView.findViewById(R.id.userName);
        userImageNav = headerView.findViewById(R.id.userImageNav);
        userEmail.setText(mIntent.getSerializableExtra("email").toString());
        userName.setText(mIntent.getSerializableExtra("name").toString());

         byte[] decodedString = Base64.decode(mIntent.getSerializableExtra("profilePic").toString(), Base64.DEFAULT);
         Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

         Drawable verticalImage = new BitmapDrawable(getResources(),decodedByte );
         userImageNav.setImageDrawable(verticalImage);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer =   findViewById(R.id.drawer_layout);

         NavigationView navigationView =  findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);
        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }
        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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
    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent profileActivity = new Intent(getApplicationContext(), ProfileActivity.class);
            profileActivity.putExtra("id", userDetails.getEmpId());
            profileActivity.putExtra("name", userDetails.getName());
            profileActivity.putExtra("email", userDetails.getEmail());
            profileActivity.putExtra("mobileNumber", userDetails.getMobileNumber());
            profileActivity.putExtra("role", userDetails.getRole());
            profileActivity.putExtra("team", userDetails.getTeam());
            profileActivity.putExtra("profilePic", userDetails.getProfilePic());
            startActivity(profileActivity);
        } else if (id == R.id.nav_intranet) {
            Intent intranetIntent = new Intent(getApplicationContext(), IntranetActivity.class);
            startActivity(intranetIntent);
        }  else if (id == R.id.nav_search) {
            Intent intranetIntent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intranetIntent);
        }else if (id == R.id.nav_logout){
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            File file = new File(this.getFilesDir(),"3Locator");
            File gpxfile = new File(file, "mytextfile.txt");
            gpxfile.delete();
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }


private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

    @Override
    protected String doInBackground(Tag... params) {
        Tag tag = params[0];

        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            // NDEF is not supported by this Tag.
            return null;
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {



            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {

                    return readText(ndefRecord);
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Unsupported Encoding", e);
                }
            }
        }

        return null;
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"

        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    @Override
    protected void onPostExecute(String result) {
         if (result != null) {
          sendPost(result);
         }else{
             Toast.makeText(mContext,"Nothing to read from the nfc tag",Toast.LENGTH_LONG).show();
         }
    }
    }

    public void sendPost(String result) {
        final String location = result;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
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
                    setStatusCode(conn.getResponseCode());
                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        if (responseStatus == 200){
            Toast.makeText(mContext,"Successfly regestered",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(mContext,"Failed to regester",Toast.LENGTH_LONG).show();
        }
        System.out.println("responseStatus: "+responseStatus);


    }
    private void setStatusCode (int responseStatus){
        this.responseStatus = responseStatus;

    }
}