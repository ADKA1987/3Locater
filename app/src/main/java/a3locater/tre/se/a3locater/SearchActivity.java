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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import a3locater.tre.se.a3locater.domain.EmployeeLocation;
import a3locater.tre.se.a3locater.util.Search;

public class SearchActivity extends AppCompatActivity {
    private EditText searchText;
    private ImageButton searchBtn;
    private String searchUrl = "https://taptocheckin.herokuapp.com/checkin/getUserLocation?name=";
    private Context mContext;
    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ArrayList<String> floorsList= getIntent().getStringArrayListExtra("floors");
        ArrayList<String> areaList = getIntent().getStringArrayListExtra("areas");
        mContext = getApplicationContext();
        searchBtn =   findViewById(R.id.searchBtn);
        searchText = findViewById(R.id.searchText);
        searchText.setHint("Enter name");
        layout = findViewById(R.id.searchList);
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
                            Toast.makeText(mContext,"Could not find the location for the given name.",Toast.LENGTH_LONG).show();
                        }else{
                            layout.removeAllViews();
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

        Spinner dynamicSpinner = findViewById(R.id.floor_spinner);
        ArrayList<String> items = new ArrayList<>();
        for (String floor : floorsList){
            items.add(floor);
        }
         //= new String[] { "Chai Latte", "Green Tea", "Black Tea" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,

                android.R.layout.simple_spinner_item, items);

        dynamicSpinner.setAdapter(adapter);

        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        Spinner areaSpinner = findViewById(R.id.area_spinner);
        ArrayList<String> areaitems = new ArrayList<>();
        for (String area : areaList){
            areaitems.add(area);
        }
        //= new String[] { "Chai Latte", "Green Tea", "Black Tea" };

        ArrayAdapter<String> areaadapter = new ArrayAdapter<String>(mContext,

                android.R.layout.simple_spinner_item, areaitems);

        areaSpinner.setAdapter(adapter);

        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }
    private void setEmployeeLocation(EmployeeLocation employeeLocation) {




        byte[] decodedString = Base64.decode(employeeLocation.getLocationImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Bitmap.createScaledBitmap(decodedByte,200,200,true);
        Drawable verticalImage = new BitmapDrawable(getResources(),decodedByte );

        TextView dynamicTextView = new TextView(this);
        dynamicTextView.setTextSize(20);
        dynamicTextView.setPadding(20, 300, 20, 100);
        dynamicTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dynamicTextView.setText(employeeLocation.getEmpName());

        ImageView dynamicImageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1200, 1200);
        dynamicImageView.setLayoutParams(layoutParams);
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
