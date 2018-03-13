package a3locater.tre.se.a3locater;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Intent mIntent;
    private TextView userName, userEmail, userNumber, userRole, userTeam;
    private ImageView userImage;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent mIntent = getIntent();
        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userNumber = findViewById(R.id.user_number);
        userRole = findViewById(R.id.user_role);
        userTeam = findViewById(R.id.user_team);
        userImage = findViewById(R.id.user_profile_image);

        userName.setText(mIntent.getSerializableExtra("name").toString());
        userEmail.setText(mIntent.getSerializableExtra("email").toString());
        userNumber.setText(mIntent.getSerializableExtra("mobileNumber").toString());
        userRole.setText(mIntent.getSerializableExtra("role").toString());
        userTeam.setText(mIntent.getSerializableExtra("team").toString());

        byte[] decodedString = Base64.decode(mIntent.getSerializableExtra("profilePic").toString(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        Drawable verticalImage = new BitmapDrawable(getResources(),decodedByte );
        userImage.setImageDrawable(verticalImage);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                //mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent profileActivity = new Intent(getApplicationContext(), ProfileActivity.class);
            profileActivity.putExtra("name", mIntent.getSerializableExtra("name").toString());
            profileActivity.putExtra("email",mIntent.getSerializableExtra("email").toString());
            profileActivity.putExtra("mobileNumber", mIntent.getSerializableExtra("mobileNumber").toString());
            profileActivity.putExtra("role", mIntent.getSerializableExtra("role").toString());
            profileActivity.putExtra("team", mIntent.getSerializableExtra("team").toString());
            profileActivity.putExtra("profilePic", mIntent.getSerializableExtra("profilePic").toString());
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
}
