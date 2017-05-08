package com.example.aniket.easygrades;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aniket.easygrades.fragments.AppController;
import com.example.aniket.easygrades.fragments.OneFragment;

import org.apache.ivy.Main;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.aniket.easygrades.CourseRating.name1;
import static com.example.aniket.easygrades.CourseRating.name2;
import static com.example.aniket.easygrades.CourseRating.name3;
import static com.example.aniket.easygrades.CourseRating.name4;
import static com.example.aniket.easygrades.CourseRating.name5;
import static com.example.aniket.easygrades.MainActivity.Totaldata;
import static com.example.aniket.easygrades.MainActivity.adapter;
import static com.example.aniket.easygrades.MainActivity.all_tags;
import static com.example.aniket.easygrades.MainActivity.notes_course;

/**
 * Created by Aniket on 5/4/2017.
 */


/**
 * This class calls server side code to perform GET requests and update
 * all database entries. The class also retrieves objects with database and k
 * keep user screen sync with updated data.
 */

public class JSONparse {

    Activity mActivity;

    public JSONparse(Activity activity){
        mActivity = activity;
    }

     /**
     *   The function calls the server side abstract to get list of courses offered in course_term.
     */

    public void get_course(final String course_term) {

        StringRequest strReq;

        String updated_url =  MainActivity.GET_OBJ_URL + "?course_term=" + course_term;

        Log.d("updated_url",updated_url);

        strReq = new StringRequest(Request.Method.GET,
                updated_url ,new Response.Listener<String>() {

            @Override


            public void onResponse(String response) {
                Log.d("Get Course", "Register Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray j_array = jObj.getJSONArray("courses");
                        MainActivity.course_inform.clear();
                        for(int i=0; i<j_array.length(); i++)
                        {   JSONObject j_object = j_array.getJSONObject(i);
                            HashMap<String,String> hmap = new HashMap<String, String>();
                            String get_object = j_object.getString("course_id");
                            hmap.put(MainActivity.KEY_COURSE_ID,get_object);
                            get_object =  j_object.getString("prof_name");
                            hmap.put(MainActivity.KEY_PROF_NAME,get_object);
                            get_object = j_object.getString("dept_name");
                            hmap.put(MainActivity.KEY_DEPT_NAME,get_object);
                            MainActivity.course_inform.add(hmap);
                        }
                        String []  courses_available = new String[MainActivity.course_inform.size()];
                        for(int i=0;i< MainActivity.course_inform.size();i++)
                        {   String course_name = MainActivity.course_inform.get(i).get(MainActivity.KEY_COURSE_ID);
                            courses_available[i] = course_name;
                            Log.d("courses",courses_available[i]);
                        }


                        ArrayAdapter<String> adapters = new ArrayAdapter<String>(mActivity, R.layout.courses_list_item, R.id.label, courses_available);
                        OneFragment.lv.setAdapter(adapters);

                    }else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(mActivity,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Tag", "Registration Error: " + error.getMessage());
                Toast.makeText(mActivity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        String get_all_courses = "get_all_courses";
        AppController.getInstance().addToRequestQueue(strReq,get_all_courses);
    }

     /**
     *  The function fetches the detials of notes being uploaded in a course and stores all the tags information.
     */

    public void get_notes_course(final String course_id) {


        String updated_url = MainActivity.GET_NOTES_COURSE_URL + "?course_id=" + course_id;

        StringRequest strReq;
        strReq = new StringRequest(Request.Method.GET,
                updated_url, new Response.Listener<String>() {

            @Override


            public void onResponse(String response) {
                Log.d("Get notes course", "Register Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {


                        Totaldata.clear();
                        notes_course.clear();
                        all_tags.clear();

                        JSONArray j_array = jObj.getJSONArray("notes");
                        for(int i=0;i<j_array.length();i++) {
                            JSONObject j_object = j_array.getJSONObject(i);
                            String notes_id =  j_object.getString("notes_id");
                            String notes_title = j_object.getString("notes_title");
                            String notes_tag = j_object.getString("notes_tag");
                            String notes_url = j_object.getString("notes_obj_loc");
                            Integer notes_download = j_object.getInt("ratings");
                            if(notes_course.containsKey(notes_id))
                            {   HashMap<String,String> hmap = new HashMap<String, String>();
                                hmap = notes_course.get(notes_id);
                                if(hmap.containsKey("tag1")) {
                                    if (hmap.containsKey("tag2")) {
                                        hmap.put("tag3",notes_tag);
                                    } else {
                                        hmap.put("tag2",notes_tag);
                                    }
                                }
                                else
                                {
                                    hmap.put("tag1",notes_tag);
                                }
                                notes_course.put(notes_id,hmap);
                            }
                            else
                            {   HashMap<String,String> hmap = new HashMap<String, String>();
                                hmap.put("notes_title",notes_title);
                                hmap.put("tag1",notes_tag);
                                hmap.put("notes_url",notes_url);

                                hmap.put("notes_download",notes_download.toString());
                                notes_course.put(notes_id,hmap);
                            }
                            all_tags.add(notes_tag);
                        }


                        Log.d("initial size",Integer.toString(notes_course.size()));
                        for (Map.Entry<String, HashMap<String,String>> entry : notes_course.entrySet()) {
                            String key = entry.getKey();
                            HashMap<String,String> object = entry.getValue();
                            HashMap<String,String> add_val =  new HashMap<String,String>();
                            add_val.put(MainActivity.KEY_ID,key);
                            add_val.put(MainActivity.KEY_TITLE,object.get("notes_title"));
                            if(object.containsKey("tag1"))
                                add_val.put(MainActivity.KEY_TAG1,object.get("tag1"));
                            if(object.containsKey("tag2"))
                                add_val.put(MainActivity.KEY_TAG2,object.get("tag2"));
                            if(object.containsKey("tag3"))
                                add_val.put(MainActivity.KEY_TAG3,object.get("tag3"));
                            add_val.put(MainActivity.KEY_URL,MainActivity.DOWNLOAD_URL+object.get("notes_url"));
                            add_val.put(MainActivity.KEY_NUM_DOWNLOAD,object.get("notes_download"));
                            Totaldata.add(add_val);

                        }


                        Log.d("adapter size",Integer.toString(Totaldata.size()));
                        adapter = new MyAdapter(mActivity,Totaldata);
                        MainActivity.listview_main.setAdapter(adapter);

                       // SUGGESTIONS.clear();

                        
                    }else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        if(errorMsg.equals("no_notes")) {
                        }
                        else {
                            Toast.makeText(mActivity,
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Tag", "Registration Error: " + error.getMessage());
                Toast.makeText(mActivity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        String get_all_courses = "get_all_courses";
        AppController.getInstance().addToRequestQueue(strReq,get_all_courses);
    }

    /**
    * To rate a course, this function gets the list of parameters to rate a course.
     */


    public void get_param_names(final String course_id) {


        String updated_url = MainActivity.GET_PARAMS_COURSE + "?course_id=" + course_id;

        StringRequest strReq;
        strReq = new StringRequest(Request.Method.GET,
                updated_url, new Response.Listener<String>() {

            @Override


            public void onResponse(String response) {
                Log.d("Get notes course", "Register Response: " + response.toString());
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray j_array = jObj.getJSONArray("tags");
                        for(int i=0;i<j_array.length();i++)
                        {   JSONObject j_obj = j_array.getJSONObject(i);
                            String params = j_obj.getString("course_tag");
                            switch (i) {
                                case 0:
                                    name1.setText(params);
                                    break;
                                case 1:
                                    name2.setText(params);
                                    break;
                                case 2:
                                    name3.setText(params);
                                    break;
                                case 3:
                                    name4.setText(params);
                                    break;
                                case 4:
                                    name5.setText(params);
                                    break;
                                default:
                                    break;
                            }
                        }


                    }else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        if(errorMsg.equals("no_notes")) {
                        }
                        else {
                            Toast.makeText(mActivity,
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Tag", "Registration Error: " + error.getMessage());
                Toast.makeText(mActivity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        String get_all_courses = "get_all_courses";
        AppController.getInstance().addToRequestQueue(strReq,get_all_courses);
    }

    /**
    * The function performs a GET request and update the database entry with uploading the file on
     * the server.
     */

    public void addNote(final String note_title, final String  note_tag1, final String note_tag2, final String note_tag3,
    final String note_url, final String course_name) {
        // Tag used to cancel the request
        String tag_string_req = "req_add_note";


        String updated_url = MainActivity.ADD_NOTE_URL + "?notes_title=" + note_title  + "&notes_tag1=" +
                note_tag1 + "&notes_tag2=" + note_tag2 + "&notes_tag3=" + note_tag3 + "&notes_url=" + note_url +
                "&course_id=" + course_name;


        StringRequest strReq;
        Log.d("url find",updated_url);

        strReq = new StringRequest(Request.Method.GET,
                updated_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("add Note", "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                    } else {
                       }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Tag", "Registration Error: " + error.getMessage());
                Toast.makeText(mActivity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }




    public void send_tag_download(final String course_id,final String notes_id, final String notes_url) {
        // Tag used to cancel the request
        String tag_string_req = "req_add_note";

        String updated_url = MainActivity.NOTES_DOWNLOAD + "?course_id=" + course_id + "&notes_id=" + notes_id + "&notes_loc="
                + notes_url;

        StringRequest strReq;
        Log.d("url find",updated_url);

        strReq = new StringRequest(Request.Method.GET,
                updated_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Send tag download", "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Tag", "Registration Error: " + error.getMessage());
                Toast.makeText(mActivity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    public void submit_ratings(final String course_id,final String c1, final String c2, final String c3,
                               final String c4, final String c5, final String r1, final String r2, final String r3,
                               final String r4, final String r5) {
        // Tag used to cancel the request
        String tag_string_req = "req_add_note";

        String updated_url = MainActivity.UPDATE_RATING + "?course_id=" + course_id + "&course_tag1=" + c1 + "&course_rating1=" + r1
                + "&course_tag2=" + c2 + "&course_rating2=" + r2+ "&course_tag3=" + c3 + "&course_rating3=" + r3 +
                "&course_tag4=" + c4 + "&course_rating4=" + r4+ "&course_tag5=" + c5 + "&course_rating5=" + r5;

        StringRequest strReq;
        Log.d("url find",updated_url);

        strReq = new StringRequest(Request.Method.GET,
                updated_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Send tag download", "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Tag", "Registration Error: " + error.getMessage());
                Toast.makeText(mActivity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}

