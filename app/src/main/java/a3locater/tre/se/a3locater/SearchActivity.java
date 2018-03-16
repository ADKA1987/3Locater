package a3locater.tre.se.a3locater;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import a3locater.tre.se.a3locater.domain.EmployeeLocation;
import a3locater.tre.se.a3locater.util.Search;

public class SearchActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    private EditText searchText;
    private ImageButton searchBtn;
    private String searchUrl = "https://taptocheckin.herokuapp.com/checkin/getUserLocation?name=";
    private Context mContext;
    private LinearLayout layout;
    ArrayList<String> areaList;
    private Spinner areaSpinner,floorSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ArrayList<String> floorsList = getIntent().getStringArrayListExtra("floors");
        areaList = getIntent().getStringArrayListExtra("areas");
        mContext = getApplicationContext();
        searchBtn = findViewById(R.id.searchBtn);
        searchText = findViewById(R.id.searchText);
        searchText.setHint("Enter name");
        layout = findViewById(R.id.searchList);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText.setText(null);
            }
        });
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (null == searchText.getText().toString() || searchText.getText().toString().isEmpty()) {
                        Toast.makeText(mContext, "Nothing to search for..", Toast.LENGTH_LONG).show();
                        //searchText.setText("Nothing to search for..");
                    } else {
                        //System.out.println("Entry to get execute search");
                        // Toast.makeText(mContext,"Entry to get execute search",Toast.LENGTH_LONG).show();

                        AsyncTask<String, String, EmployeeLocation> execute = new Search(searchUrl, searchText.getText().toString()).execute();
                        try {
                            if (null == execute.get()) {
                                Toast.makeText(mContext, "Could not find the location for the given name.", Toast.LENGTH_LONG).show();
                            } else {
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
                return false;
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == searchText.getText().toString() || searchText.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "Nothing to search for..", Toast.LENGTH_LONG).show();
                    //searchText.setText("Nothing to search for..");
                } else {
                    //System.out.println("Entry to get execute search");
                    // Toast.makeText(mContext,"Entry to get execute search",Toast.LENGTH_LONG).show();

                    AsyncTask<String, String, EmployeeLocation> execute = new Search(searchUrl, searchText.getText().toString()).execute();
                    try {
                        if (null == execute.get()) {
                            Toast.makeText(mContext, "Could not find the location for the given name.", Toast.LENGTH_LONG).show();
                        } else {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

      //  floorSpinner = findViewById(R.id.floor_spinner);
       // areaSpinner = findViewById(R.id.area_spinner);


        ArrayList<String> items = new ArrayList<>();
        for (String floor : floorsList) {
            items.add("Floor " + floor);
        }
        //= new String[] { "Chai Latte", "Green Tea", "Black Tea" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,

                android.R.layout.simple_spinner_item, items);

       // floorSpinner.setAdapter(adapter);

        //floorSpinner.setOnItemSelectedListener(this );
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = ItemOneFragment.newInstance();
                                break;
                            case R.id.action_item2:
                                selectedFragment = ItemTwoFragment.newInstance();
                                break;
                            case R.id.action_item3:
                                selectedFragment = ItemThreeFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ItemOneFragment.newInstance());
        transaction.commit();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {
        String selectedItem= String.valueOf(floorSpinner.getSelectedItem());
        if(selectedItem.contentEquals("Floor 01")){
            ArrayList<String> areaitems = new ArrayList<>();

            for (String area : areaList){
                areaitems.add("Area "+area);
            }

            ArrayAdapter<String> areaadapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, areaitems);
            areaadapter.notifyDataSetChanged();
            areaSpinner.setAdapter(areaadapter);
            areaSpinner.setVisibility(View.VISIBLE);
            // areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //   @Override
            // public void onItemSelected(AdapterView<?> parent, View view,
            //                          int position, long id) {
            //  Log.v("item", (String) parent.getItemAtPosition(position));
            //}

            //@Override
            // public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
            // }
            //});
        }else{
            Toast.makeText(mContext,"No Areas in this floor.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
    private void setEmployeeLocation(EmployeeLocation employeeLocation) {

        byte[] decodedString = Base64.decode(employeeLocation.getLocationImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Bitmap.createScaledBitmap(decodedByte,200,200,true);
        Drawable verticalImage = new BitmapDrawable(getResources(),decodedByte );

        byte[] profileString = Base64.decode(employeeLocation.getProfilePic(), Base64.DEFAULT);
        Bitmap profileByte = BitmapFactory.decodeByteArray(profileString, 0, profileString.length);
        Bitmap.createScaledBitmap(profileByte,200,200,true);
        Drawable profileImage = new BitmapDrawable(getResources(),profileByte );


        ImageView profileImageView = new ImageView(this);
        /*LinearLayout.LayoutParams proflayoutParams = new LinearLayout.LayoutParams(400, 400);
        profileImageView.setLayoutParams(proflayoutParams);*/


        TextView dynamicTextView = new TextView(this);
        dynamicTextView.setTextSize(20);
        dynamicTextView.setPadding(20, 10, 10, 50);
        dynamicTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dynamicTextView.setText(employeeLocation.getEmpName());

        profileImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        profileImageView.setImageDrawable(profileImage);

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
