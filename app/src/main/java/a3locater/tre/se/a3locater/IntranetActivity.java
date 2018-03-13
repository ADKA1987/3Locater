package a3locater.tre.se.a3locater;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class IntranetActivity extends AppCompatActivity {
    private WebView webView;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intranet);

       webView = findViewById(R.id.webView);

       webView.setWebViewClient(new WebViewClient());
       webView.getSettings().setJavaScriptEnabled(true);
       webView.getSettings().setDomStorageEnabled(true);
       webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
       webView.loadUrl("https://intranet.tre.se/");
    }
}
