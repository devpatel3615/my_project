package com.example.finalchocohub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class Google_pay extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Goggle Pay");
        setContentView(R.layout.activity_google_pay);
        webView=(WebView)findViewById(R.id.payment_webview);

        payment();
    }
    public void payment() {
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //webView.loadUrl(url);
        webView.loadUrl("https://pay.google.com/");
    }

}