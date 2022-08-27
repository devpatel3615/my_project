package com.example.finalchocohub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class Pay_pal extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);

        setTitle("Pay Pal");
        webView=(WebView)findViewById(R.id.payment_webview);
        payment();

    }
    public void payment()
    {
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //webView.loadUrl(url);
        webView.loadUrl("https://www.paypal.com/");
    }
}