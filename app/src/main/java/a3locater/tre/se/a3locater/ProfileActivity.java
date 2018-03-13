package a3locater.tre.se.a3locater;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private Intent mIntent;
    private TextView userName, userEmail, userNumber, userRole, userTeam;
    private ImageView userImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent mIntent = getIntent();

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
}
