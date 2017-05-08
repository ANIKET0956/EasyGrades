package com.example.aniket.easygrades;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.aniket.easygrades.View.ViewDialog;
import com.example.aniket.easygrades.helper.FilePath;
import com.example.aniket.easygrades.helper.MyCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
    public static String KEY_ID = "key_id";
    public static String KEY_URL = "key_url";
    public static String KEY_TITLE = "key_title";
    public static String KEY_TAG1 = "key_tag1";
    public static String KEY_TAG2 = "key_tag2";
    public static String KEY_TAG3 = "key_tag3";
    public static String KEY_NUM_DOWNLOAD = "key_num_download";

    public static String localhost = "192.168.43.251";
    public static String GET_OBJ_URL = "http://"+localhost+"/get_courses.php";
    public static String ADD_NOTE_URL = "http://"+localhost+"/upload_notes.php";
    public static String GET_NOTES_COURSE_URL = "http://"+localhost+"/get_notes.php";
    public static String DOWNLOAD_URL =  "http://" + localhost + "/uploads/";
    public static String NOTES_DOWNLOAD = "http://" + localhost +  "/download_notes.php";
    public static String GET_PARAMS_COURSE = "http://"+ localhost + "/get_tags.php";
    public static String UPDATE_RATING = "http://"+ localhost + "/rate_course.php";
    public static String URL_LOGIN = "http://" + localhost + "/login_user.php";
    public static String URL_REGISTER = "http://" + localhost + "/register_user.php";


    public static String COURSE_TAG = "course_tag";
    private String selectedFilePath;
    public static SearchView searchView;
    public static JSONparse jparse;

    public static ArrayList<HashMap<String,String>> course_inform = new ArrayList<HashMap<String, String>>();
    public static HashMap<String,HashMap<String,String>> notes_course = new HashMap<String, HashMap<String, String>>();
    public static Set<String> all_tags =  new HashSet<String>();


    public static String KEY_COURSE_ID = "key_course_id";
    public static String KEY_PROF_NAME = "prof_name";
    public static String KEY_DEPT_NAME = "dept_name";



    // Suggestions when users try to find in search menu.


    // Basic layout features.
    private SimpleCursorAdapter mAdapter;

    // Data fetched from server database.
    static public ArrayList<HashMap<String,String>> Totaldata = new ArrayList<HashMap<String, String>>();
    static public ArrayList<HashMap<String,String>> Searchdata = new ArrayList<HashMap<String, String>>();

    public static ListView listview_main;
    public static MyAdapter adapter;
    public Activity mActivity;
    MenuItem star_menu;
    String query;
    public static  String course_name=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get intent to know if this has been called by the search button.
        handleIntent(getIntent());

        setContentView(R.layout.activity_main);


        mActivity = this;
        listview_main = (ListView)findViewById(R.id.listview);



        if(query!=null ) {
            Search_Home(query);
        }
        else
            initalise_Home();


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




        FloatingActionButton button = (FloatingActionButton)findViewById(R.id.floatingActionButton);

        button.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor("#0000e5")));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    showFileChooser();

            }});




        // To check if this is HomePage or QueryPage.

    }


    // Some Initalising data.
    public void initalise_Home()
    {

        jparse.get_notes_course(course_name);

        AdapterView.OnItemClickListener myListViewClicked;


        myListViewClicked = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        };

        listview_main.setOnItemClickListener(myListViewClicked);

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
        listview_main.setAdapter(adapter);

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
                    ViewDialog box = new ViewDialog(getSupportActionBar().getTitle().toString());
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

                searchView.setQuery("", false);
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
        final String [] SUGGESTIONS = new String[all_tags.size()];
        int index=0;
        for (String tag_find: all_tags)
        {   SUGGESTIONS[index] = tag_find;
            index++;
        }
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
        else
            course_name = intent.getStringExtra(COURSE_TAG);
    }

}
