package com.example.aniket.easygrades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Aniket on 4/29/2017.
 */

/**
 * Rating of course page on parameters specified.
 * The users submit ratings of course enrolled.
 *
 */
public class CourseRating extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);
        Intent intent = getIntent();
        String course = intent.getStringExtra(MainActivity.COURSE_TAG);
        TextView text = (TextView)findViewById(R.id.course);
        text.setText(course);

    }
}

