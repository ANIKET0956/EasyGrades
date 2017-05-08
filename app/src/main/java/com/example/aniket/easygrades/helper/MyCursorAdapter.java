package com.example.aniket.easygrades.helper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.aniket.easygrades.MainActivity;
import com.example.aniket.easygrades.R;

/**
 * Created by Aniket on 4/29/2017.
 * Customised Suggestion Adapter with preferred layouts.
 *  The adapter is intialized with suggestion values of tags.
 */

public class MyCursorAdapter extends SimpleCursorAdapter {
    public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        view.setBackgroundColor(Color.WHITE);
        final TextView text1 = (TextView)view.findViewById(android.R.id.text1);
    }

}