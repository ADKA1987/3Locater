package a3locater.tre.se.a3locater;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import a3locater.tre.se.a3locater.domain.EmployeeLocation;
import a3locater.tre.se.a3locater.util.Search;

public class SearchActivity extends AppCompatActivity {
    private EditText searchText;
    private ImageButton searchBtn;
    private String searchUrl = "https://taptocheckin.herokuapp.com/checkin/getUserLocation?name=";
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = getApplicationContext();
        searchBtn =   findViewById(R.id.searchBtn);
        searchText = findViewById(R.id.searchText);
        searchText.setHint("Enter name");
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText.setText(null);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null == searchText.getText().toString() ||searchText.getText().toString().isEmpty() ){
                    Toast.makeText(mContext,"Nothing to search for..",Toast.LENGTH_LONG).show();
                    //searchText.setText("Nothing to search for..");
                }else {
                    //System.out.println("Entry to get execute search");
                   // Toast.makeText(mContext,"Entry to get execute search",Toast.LENGTH_LONG).show();

                    AsyncTask<String, String, EmployeeLocation> execute = new Search(searchUrl , searchText.getText().toString()).execute();
                    try {
                        if(null == execute.get()){
                            Toast.makeText(mContext,"Cannot find the employee.",Toast.LENGTH_LONG).show();
                        }else{
                            setEmployeeLocation(execute.get());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void setEmployeeLocation(EmployeeLocation employeeLocation) {
        LinearLayout layout = findViewById(R.id.searchList);



        byte[] decodedString = Base64.decode(employeeLocation.getLocationImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Drawable verticalImage = new BitmapDrawable(getResources(),decodedByte );

        TextView dynamicTextView = new TextView(this);
        dynamicTextView.setTextSize(20);
        dynamicTextView.setPadding(20, 300, 20, 100);
        dynamicTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dynamicTextView.setText(employeeLocation.getEmpName());

        ImageView dynamicImageView = new ImageView(this);
        dynamicImageView.setImageDrawable(verticalImage);

        TextView dynamicTextViewLocation = new TextView(this);
        dynamicTextViewLocation.setTextSize(20);
        dynamicTextViewLocation.setPadding(20, 300, 20, 100);
        dynamicTextViewLocation.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dynamicTextViewLocation.setText(employeeLocation.getLocation() );



        layout.addView(dynamicTextView);
        layout.addView(dynamicImageView);
        layout.addView(dynamicTextViewLocation);

        //this.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }
}
