package com.example.aniket.easygrades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aniket on 4/29/2017.
 */

/**
 * Rating of course page on parameters specified.
 * The users submit ratings of course enrolled.
 *
 */
public class CourseRating extends Activity {

    static public  TextView name1,name2,name3,name4,name5;
    String course;
    static public RatingBar rating1,rating2,rating3,rating4,rating5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);
        Intent intent = getIntent();
        course = intent.getStringExtra(MainActivity.COURSE_TAG);
        TextView text = (TextView)findViewById(R.id.course);
        text.setText(course);

         name1 = (TextView)findViewById(R.id.text1);
         name2 = (TextView)findViewById(R.id.text2);
         name3 = (TextView)findViewById(R.id.text3);
         name4 = (TextView)findViewById(R.id.text4);
         name5 = (TextView)findViewById(R.id.text5);


        rating1 = (RatingBar)findViewById(R.id.ratingBar1);
        rating2 = (RatingBar)findViewById(R.id.ratingBar2);
        rating3 = (RatingBar)findViewById(R.id.ratingBar3);
        rating4 = (RatingBar)findViewById(R.id.ratingBar4);
        rating5 = (RatingBar)findViewById(R.id.ratingBar5);

        MainActivity.jparse.get_param_names(course);

        Button button = (Button)findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c1 = name1.getText().toString();
                String c2 = name2.getText().toString();
                String c3 = name3.getText().toString();
                String c4 = name4.getText().toString();
                String c5 = name5.getText().toString();


                String r1 = Float.toString(rating1.getRating());
                String r2 = Float.toString(rating2.getRating());
                String r3 = Float.toString(rating3.getRating());
                String r4 = Float.toString(rating4.getRating());
                String r5 = Float.toString(rating5.getRating());


                MainActivity.jparse.submit_ratings(course,c1,c2,c3,c4,c5,r1,r2,r3,r4,r5);

                rating1.setRating(0);
                rating2.setRating(0);
                rating3.setRating(0);
                rating4.setRating(0);
                rating5.setRating(0);

            }
        });

    }
}

