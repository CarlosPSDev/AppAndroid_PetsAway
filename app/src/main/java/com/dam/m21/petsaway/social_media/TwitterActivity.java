package com.dam.m21.petsaway.social_media;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dam.m21.petsaway.R;

public class TwitterActivity extends AppCompatActivity {
    WebView webViewTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        webViewTwitter = findViewById(R.id.webViewTwitter);
        webViewTwitter.setWebViewClient(new WebViewClient());
        webViewTwitter.loadUrl("https://twitter.com/away_pets");

        WebSettings webSet = webViewTwitter.getSettings();
        webSet.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if(webViewTwitter.canGoBack()) webViewTwitter.goBack();
        else super.onBackPressed();
    }
}
