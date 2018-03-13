package a3locater.tre.se.a3locater;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import a3locater.tre.se.a3locater.domain.EmployeeNames;
import a3locater.tre.se.a3locater.util.Search;

public class SearchActivity extends AppCompatActivity {
    private EditText searchText;
    private ImageButton searchBtn;
    private String searchUrl = "https://taptocheckin.herokuapp.com/checkin/getUserLocation?name=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchBtn =   findViewById(R.id.searchBtn);
        searchText = findViewById(R.id.searchText);
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
                    searchText.setText("Nothing to search for..");
                }else {
                    System.out.println("Entry to get execute search");
                    AsyncTask<String, String, EmployeeNames> execute = new Search(searchUrl , searchText.getText().toString()).execute();
                    try {
                        System.out.println("Trying to get execute search");
                        setEmployeesNames(execute.get());

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void setEmployeesNames(EmployeeNames employeeNames) {
        LinearLayout layout = findViewById(R.id.searchList);


        TextView dynamicTextView = new TextView(this);
        dynamicTextView.setTextSize(20);
        dynamicTextView.setPadding(20, 300, 20, 100);
        dynamicTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dynamicTextView.setText(employeeNames.getEmpName() +" "+ employeeNames.getLocation() +" "+ employeeNames.getLocationImage());
        layout.addView(dynamicTextView);

        //this.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }
}
