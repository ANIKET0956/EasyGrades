package com.example.aniket.easygrades.View;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aniket.easygrades.MainActivity;
import com.example.aniket.easygrades.R;
import com.example.aniket.easygrades.fragments.OneFragment;
import com.example.aniket.easygrades.helper.FilePath;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aniket on 4/29/2017.
 */

/**
 * This generates a dialog box when user tries to upload a file.
 * The generated dialog box asks for tags and titles.
 * The data is uploaded to server and metadata send to database.
 *
 */


public class ViewDialog {

    ProgressDialog Pdialog;
    Activity mActivity;

    public static String course_name;

    public ViewDialog(String course)
    {
        course_name = course;
    }

    // Upload Server URL.
    public static String UPLOAD_URL = "http://" + MainActivity.localhost+
            "/UploadToServer.php";

    String note_title,note_tag1,note_tag2,note_tag3;
    EditText widget;

    Dialog dialog;

    public void showDialog(final Activity activity, final String selectedFilePath,Uri uri){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        mActivity = activity;
        TextView textview = (TextView)dialog.findViewById(R.id.textView5);
        textview.setText("Path : "+ FilenameUtils.getName(uri.getPath()));

        widget = (EditText)dialog.findViewById(R.id.editText);

        Button button = (Button)dialog.findViewById(R.id.button);
        dialog.show();

        //  Running new thread to avoid doing task on UI thread.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedFilePath != null){


                    Pdialog = ProgressDialog.show(activity,"","Uploading File...",true);


                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            note_title = widget.getText().toString();
                            widget = (EditText)dialog.findViewById(R.id.tag1);
                            note_tag1 = widget.getText().toString();
                            widget = (EditText)dialog.findViewById(R.id.tag2);
                            note_tag2 = widget.getText().toString();
                            widget = (EditText)dialog.findViewById(R.id.tag3);
                            note_tag3 = widget.getText().toString();

                            //creating new thread to handle Http Operations
                            String basename = FilenameUtils.getBaseName(selectedFilePath)+"."+FilenameUtils.getExtension(selectedFilePath);
                            MainActivity.jparse.addNote(note_title,note_tag1,note_tag2,note_tag3,basename,course_name);

                            int res = uploadFile(selectedFilePath);
                            if(res > 0){
                                dialog.dismiss();
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(activity,"File Uploaded",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    }).start();
                }else{
                    Toast.makeText(activity,"Please choose a File First",Toast.LENGTH_SHORT).show();
                }

            }


    });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });

    }


    /**Uploading the Selected FilePath. */
    public int uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            Pdialog.dismiss();

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                 }
            });
            return 0;
        }else{
            try{

                // Creating Connection.
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(UPLOAD_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i("Server Response", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "", res = "";
                while ((line = rd.readLine()) != null) {
                    res += line;
                }


                Log.d("response echo",res);
                System.out.println(res);
                rd.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity,"File Not Found: " + selectedFilePath ,Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(mActivity, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
            Pdialog.dismiss();
            return serverResponseCode;
        }
    }
}
