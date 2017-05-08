package com.example.aniket.easygrades;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.example.aniket.easygrades.helper.MyCursorAdapter;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aniket on 4/29/2017.
 */

/**
 * This Asyncronous Task is called when user clicks on note download.
 * The task is run on seperate thread and takes URL of data as input.
 * The data is stored in external storage if the permission is provided.
 *
 */

class DownloadFileFromUrl extends AsyncTask<String,Integer,String>{

        /**
         * Before starting background thread
         * */
        private PowerManager.WakeLock mWakeLock;
        Context context;


        public static String course_name;
        public static String notes_id;
        public static String notes_url;

        public  DownloadFileFromUrl(Context c,String course,String notes,String url)
        {
            context =c;
            course_name = course;
            notes_id = notes;
            notes_url = url;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
            PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            Log.d("show_time","time");
            MainActivity.adapter.mProgressDialog.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {

                URL url = new URL(f_url[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                String Path = Environment.getExternalStorageDirectory() + "/download/";

                FileOutputStream fos = new FileOutputStream(new File(Path+ FilenameUtils.getName(url.getPath())));
                Log.d("tag url",FilenameUtils.getName(url.getPath()));

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[702];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                Log.d("Download Manager", "Error: " + e);
            }
            Log.v("Download Manager", "Check: ");

            MainActivity.jparse.send_tag_download(course_name,notes_id,notes_url);

            return "";
        }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        MainActivity.adapter.mProgressDialog.setIndeterminate(false);
        MainActivity.adapter.mProgressDialog.setMax(100);
        MainActivity.adapter.mProgressDialog.setProgress(progress[0]);
    }



    /**
         * After completing background task
         * **/

        @Override
        protected void onPostExecute(String file) {
            mWakeLock.release();
            MainActivity.adapter.mProgressDialog.dismiss();
            Toast.makeText(context,"Download Completed",Toast.LENGTH_SHORT).show();
            System.out.println("Downloaded");
        }

}

