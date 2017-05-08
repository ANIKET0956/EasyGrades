package com.example.aniket.easygrades;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.aniket.easygrades.View.ViewImage;

import org.apache.commons.io.FilenameUtils;

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
        public ImageLoader imageLoader;


    public MyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
            activity = a;
            data=d;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage("A message");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            imageLoader = new ImageLoader(activity.getApplicationContext(),a);
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
            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.notes, null);

            vi.setFocusable(false);

            final HashMap<String, String> item = data.get(position);
            TextView note_title = (TextView) vi.findViewById(R.id.textView);
            TextView tag1 = (TextView) vi.findViewById(R.id.textView2);
//            TextView tag2  = (TextView)vi.findViewById(R.id.textView3);
            //          TextView tag3  = (TextView)vi.findViewById(R.id.textView4);
            final EditText download_num = (EditText) vi.findViewById(R.id.edit_download);
            final ImageView image_view = (ImageView) vi.findViewById(R.id.image_view);

            RelativeLayout rl = (RelativeLayout) vi.findViewById(R.id.layout);
            LinearLayout l1 = (LinearLayout) vi.findViewById(R.id.layout_1);
            //
            //  LinearLayout l2 = (LinearLayout)vi.findViewById(R.id.layout_2);
            //   LinearLayout l3 = (LinearLayout)vi.findViewById(R.id.layout_3);


            ImageView download = (ImageView) vi.findViewById(R.id.download);


            String tag_combined = "";
            if (item.containsKey(MainActivity.KEY_TAG1))
                tag_combined += item.get(MainActivity.KEY_TAG1);
            if (item.containsKey(MainActivity.KEY_TAG2))
                tag_combined += " | " + item.get(MainActivity.KEY_TAG2);
            if (item.containsKey(MainActivity.KEY_TAG3))
                tag_combined += " | " + item.get(MainActivity.KEY_TAG3);


            tag1.setText(tag_combined);


            if (position % 2 == 0) {
                image_view.setImageResource(R.drawable.image_icon);
            }

            String url_item = item.get(MainActivity.KEY_URL);
            String extension = FilenameUtils.getExtension(url_item);
            if (extension.equals("png") || extension.equals("jpg")) {
                imageLoader.DisplayImage(url_item, image_view);
            }


            note_title.setText(item.get(MainActivity.KEY_TITLE));
           download_num.setText(item.get(MainActivity.KEY_NUM_DOWNLOAD));

            final Integer index = position;


            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        //  DownloadFromUrl(item.get(MainActivity.KEY_URL));
                        String[] params = {item.get(MainActivity.KEY_URL)};
                        new DownloadFileFromUrl(activity,MainActivity.course_name,item.get(MainActivity.KEY_ID),item.get(MainActivity.KEY_URL).replace(MainActivity.DOWNLOAD_URL,"")).execute(params);
                        Integer number = Integer.parseInt(download_num.getText().toString());
                        download_num.setText(Integer.toString(number+1));

                }
            });

            image_view.setOnClickListener(new View.OnClickListener(){

                @Override
                public  void onClick(View v){
                   //activity.getApplicationContext().startActivity(intent);
                    String url_item = item.get(MainActivity.KEY_URL);
                    String extension = FilenameUtils.getExtension(url_item);

                    if(extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg"))
                    {   Intent intent = new Intent(activity,ViewImage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        intent.putExtra("uri",url_item);
                        intent.putExtra("course",MainActivity.course_name);
                        activity.getApplicationContext().startActivity(intent);
                    }
                    else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.get(MainActivity.KEY_URL)));
                        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        activity.getApplicationContext().startActivity(browserIntent);
                    }

                }
            });

            return vi;

        }



}


