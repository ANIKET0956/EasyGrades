package com.example.aniket.easygrades;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.aniket.easygrades.helper.FilePath;
import com.example.aniket.easygrades.helper.MyCursorAdapter;

import org.apache.commons.io.FilenameUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.os.StrictMode.*;


/**
 * Main Activity when we log into Course HomePage
 * Contains Listview of notes available
 * Upload and Download options made avaialble.
 * Can naviagate to HomePage and CourseRating activity.
 *
 */
public class MainActivity extends AppCompatActivity {


    // Static Variables defined
    private static final int PICK_FILE_REQUEST = 1;
    public static String KEY_URL = "key_url";
    public static String KEY_TITLE = "key_title";
    public static String KEY_TAG1 = "key_tag1";
    public static String KEY_TAG2 = "key_tag2";
    public static String KEY_TAG3 = "key_tag3";
    public static String localhost = "192.168.43.41";
    public static String COURSE_TAG = "course_tag";
    private String selectedFilePath;
    public static SearchView searchView;



    // Suggestions when users try to find in search menu.
    private static final String[] SUGGESTIONS = {
            "Lecture 1", "Lecture 2", "Course", "HUL" , "Manali" ,  "Trip", "Form"
    };



    // Basic layout features.
    private SimpleCursorAdapter mAdapter;

    // Data fetched from server database.
    public ArrayList<HashMap<String,String>> Totaldata = new ArrayList<HashMap<String, String>>();
    public ArrayList<HashMap<String,String>> Searchdata = new ArrayList<HashMap<String, String>>();

    public ListView listview;
    public static MyAdapter adapter;
    public Activity mActivity;
    MenuItem star_menu;
    String query;
    String course_name=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get intent to know if this has been called by the search button.
        handleIntent(getIntent());

        setContentView(R.layout.activity_main);


        mActivity = this;


        // Initialise sugesstion adapter.
        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] {android.R.id.text1};
        mAdapter = new MyCursorAdapter(this,android.R.layout.simple_list_item_2, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            ThreadPolicy policy = new ThreadPolicy.Builder().permitAll().build();
            setThreadPolicy(policy);
        }

        // Verifying Permissions.
        verifyStoragePermissions(this);

        if(course_name!=null) {
            getSupportActionBar().setTitle(course_name);
        }

        listview = (ListView)findViewById(R.id.listview);


        FloatingActionButton button = (FloatingActionButton)findViewById(R.id.floatingActionButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    showFileChooser();

            }});


        // To check if this is HomePage or QueryPage.
        if(query!=null)
            Search_Home(query);
        else
            initalise_Home();

    }


    // Some Initalising data.
    public void initalise_Home()
    {
        Totaldata.clear();

        HashMap<String,String> item =  new HashMap<String, String>();
        item.put(KEY_URL,"http://"+localhost+"/cms/uploader/uploads/IMG_1389.JPG");
        item.put(KEY_TITLE,"Manali");
        item.put(KEY_TAG1,"Trip");
        Totaldata.add(item);


        HashMap<String,String> item1 =  new HashMap<String, String>();
        item1.put(KEY_URL,"http://"+localhost+"/cms/uploader/uploads/IMG_1485.JPG");
        item1.put(KEY_TITLE,"ParaGliding");
        item1.put(KEY_TAG1,"Images");
        Totaldata.add(item1);

        HashMap<String,String> item2 =  new HashMap<String, String>();
        item2.put(KEY_URL,"http://"+localhost+"/cms/uploader/uploads/PPR_59367558.pdf");
        item2.put(KEY_TITLE,"State Bank Of India");
        item2.put(KEY_TAG1,"Form");
        Totaldata.add(item2);


        HashMap<String,String> item3 =  new HashMap<String, String>();
        item3.put(KEY_URL,"http://"+localhost+"/cms/uploader/uploads/FreeWill.pdf");
        item3.put(KEY_TITLE,"Free Will");
        item3.put(KEY_TAG1,"HUL");
        Totaldata.add(item3);


        HashMap<String,String> item4 =  new HashMap<String, String>();
        item4.put(KEY_URL,"http://"+localhost+"/cms/uploader/uploads/CMSDesignDoc.pdf");
        item4.put(KEY_TITLE,"CMS Doc Design");
        item4.put(KEY_TAG1,"Course");
        Totaldata.add(item4);

        HashMap<String,String> item5 =  new HashMap<String, String>();
        item5.put(KEY_URL,"http://"+localhost+"/cms/uploader/uploads/Software_Design_Doc.pdf");
        item5.put(KEY_TITLE,"Software Design Doc");
        item5.put(KEY_TAG1,"Course");
        Totaldata.add(item5);


        HashMap<String,String> item6 =  new HashMap<String, String>();
        item6.put(KEY_URL,"http://"+localhost+"/cms/uploader/uploads/1003_DSA.pdf");
        item6.put(KEY_TITLE,"DSA");
        item6.put(KEY_TAG1,"Course");
        Totaldata.add(item6);

        adapter = new MyAdapter(this,Totaldata);
        listview.setAdapter(adapter);

    }

    // Seraching Notes on the basis of single query.
    public void Search_Home(String query)
    {
        Searchdata.clear();

        for(int i=0;i<Totaldata.size();i++)
        {   HashMap<String,String> item = Totaldata.get(i);
            if(item.get(KEY_TAG1)!=null)
            {
                if(item.get(KEY_TAG1).equals(query))Searchdata.add(item);
            }
            if(item.get(KEY_TAG2)!=null)
            {
                if(item.get(KEY_TAG2).equals(query))Searchdata.add(item);
            }
            if(item.get(KEY_TAG3)!=null)
            {
                if(item.get(KEY_TAG3).equals(query))Searchdata.add(item);
            }

        }

        adapter = new MyAdapter(this,Searchdata);
        listview.setAdapter(adapter);

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //no data present
                    return;
                }

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this,selectedFileUri);

                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    ViewDialog box = new ViewDialog();
                    box.showDialog(mActivity,selectedFilePath,selectedFileUri);
                }else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem star = (MenuItem)menu.findItem(R.id.action_search);
        star_menu = (MenuItem)menu.findItem(R.id.action_search);

        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setSubmitButtonEnabled(true);

        // Enable OnSearchClickListener.
        searchView.setOnSearchClickListener(new SearchView.OnClickListener(){
            @Override
            public void onClick(View v){
                star.setVisible(false);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                star_menu.setVisible(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                initalise_Home();

                return false;
            }
        });

        // Enable Suggestion  Adapter.
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Log.d("click on ","suggest");
                Cursor cursor = (Cursor)searchView.getSuggestionsAdapter().getItem(position);
                String text = cursor.getString(cursor.getColumnIndex("cityName"));
                searchView.setQuery(text,false);
                return false;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
               // star.setVisible(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                populateAdapter(s);
                return false;
            }
        });


        return true;
    }

    // You must implements your logic to get data using OrmLite
    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
        for (int i=0; i<SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
            {
                c.addRow(new Object[] {i, SUGGESTIONS[i]});
            }
        }
        mAdapter.changeCursor(c);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Get the id of menu button.
        if (id == R.id.action_search) {
            Intent intent = new Intent(this,CourseRating.class);
            intent.putExtra(COURSE_TAG,course_name);
            startActivity(intent);
        }
        if(id==android.R.id.home)
        {
            Log.d("tag home","home");

            onBackPressed();
        }

        return  true;
    }


    // To do when user presses back button.
    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        if(searchView!=null) {
            if (!searchView.isIconified()) {
                searchView.setQuery("", false);
                searchView.setIconified(true);
                star_menu.setVisible(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                initalise_Home();

            } else {
                super.onBackPressed();
            }
        }
        else
        {
            super.onBackPressed();
        }
    }

    // Handling new intent.
    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            if(query!=null)
                Search_Home(query);
            else
                initalise_Home();

        }

        if(course_name==null)course_name = intent.getStringExtra(COURSE_TAG);

    }
}
