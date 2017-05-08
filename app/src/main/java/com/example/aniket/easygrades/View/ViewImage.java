package com.example.aniket.easygrades.View;



import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aniket.easygrades.ImageLoader;
import com.example.aniket.easygrades.R;

import java.io.InputStream;
import java.net.URL;

/**
 * The Activity allows us to load Image when a user clicks on the image
 * icon in the notes list. The activity takes care of user configuration and
 * accordingly chnages view to potrait or landscape mode.
 */

public class ViewImage extends Activity {
    private long enqueue;
    private DownloadManager dm;
    private String downloadUrl ;
    private String sourceUrl ;
    private static ImageView iv;
    private static Bitmap bmp;
    private static Button bDownload;
    private static BroadcastReceiver receiver;
    private ImageLoader image_loader;
    public  String course_name;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_image);
        String uri = getIntent().getStringExtra("uri");
        course_name = getIntent().getStringExtra("course");
        System.out.println("ViewImage activity: " + uri);
        downloadUrl = uri;
        iv = (ImageView) findViewById(R.id.imageView1);
       // image_loader.DisplayImage(downloadUrl,iv);
        LoadImage(downloadUrl);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.d("config","change");
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    public static void LoadImage(final String url) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(url).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                    Log.e("Load Image error:", e.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bmp != null) {
                    iv.setImageBitmap(bmp);
                }
                else{
                    System.out.println("null bitmap!!");
                }
            }

        }.execute();
    }

}