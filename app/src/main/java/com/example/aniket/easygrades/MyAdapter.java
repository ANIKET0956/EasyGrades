package com.example.aniket.easygrades;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aniket on 4/29/2017.
 */

/**
 * This is a customised class of Base Adapter.
 * It provies the basic layout of the list items.
 * It sets the contents of the list items from data list.
 * It provides the basic view of each element.
 */


public class MyAdapter extends  BaseAdapter {

        private Activity activity;
        private ArrayList<HashMap<String, String>> data;
        private static LayoutInflater inflater=null;
        public ProgressDialog mProgressDialog;


    public MyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
            activity = a;
            data=d;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View vi=convertView;
            if(convertView==null)
                vi = inflater.inflate(R.layout.notes, null);

            final HashMap<String,String>  item = data.get(position);
            TextView note_title = (TextView)vi.findViewById(R.id.textView);
            TextView tag1  = (TextView)vi.findViewById(R.id.textView2);
            TextView tag2  = (TextView)vi.findViewById(R.id.textView3);
            TextView tag3  = (TextView)vi.findViewById(R.id.textView4);

            ImageView download = (ImageView)vi.findViewById(R.id.download);

            note_title.setText(item.get(MainActivity.KEY_TITLE));

            if(item.get(MainActivity.KEY_TAG1)==null) {
                tag1.setVisibility(View.GONE);
            }
            else {
                tag1.setText(item.get(MainActivity.KEY_TAG1));
            }
            if(item.get(MainActivity.KEY_TAG2)==null) {
                tag2.setVisibility(View.GONE);
            }
            else {
                tag2.setText(item.get(MainActivity.KEY_TAG2));
            }
            if(item.get(MainActivity.KEY_TAG3)==null) {
                tag3.setVisibility(View.GONE);
            }
            else {
                tag3.setText(item.get(MainActivity.KEY_TAG3));
            }

            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage("A message");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);


            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        //  DownloadFromUrl(item.get(MainActivity.KEY_URL));
                        String[] params = {item.get(MainActivity.KEY_URL)};
                        new DownloadFileFromUrl(activity).execute(params);
                }
            });

            return vi;
        }


}


