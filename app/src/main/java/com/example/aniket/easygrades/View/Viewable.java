package com.example.aniket.easygrades.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.aniket.easygrades.R;

/**
 * Created by Aniket on 5/8/2017.
 */

public class Viewable extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        Intent intent = getIntent();
        String web_url = intent.getStringExtra("url_web");
        WebView wv = (WebView)findViewById(R.id.webview);
        Log.d("search url",web_url);
        wv.loadUrl(web_url);
    }
}
