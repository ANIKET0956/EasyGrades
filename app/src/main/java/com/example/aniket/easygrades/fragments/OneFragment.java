package com.example.aniket.easygrades.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aniket.easygrades.MainActivity;
import com.example.aniket.easygrades.R;


public class OneFragment extends Fragment{

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_one, container, false);
//        return inflater.inflate(R.layout.fragment_one, container, false);

        ListView lv = (ListView) v.findViewById(R.id.courseListView);

        // storing string resources into Array
        String[] courses_available = getResources().getStringArray(R.array.courses_available);

        // Binding resources Array to ListAdapter
        ArrayAdapter<String> adapters = new ArrayAdapter<String>(this.getContext(), R.layout.courses_list_item, R.id.label, courses_available);
        lv.setAdapter(adapters);

        // listening to single list item on click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                String product = ((TextView) view).getText().toString();

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);

                // sending data to new activity
                i.putExtra(MainActivity.COURSE_TAG,product);
                startActivity(i);
            }
        });
        return v;
    }
}