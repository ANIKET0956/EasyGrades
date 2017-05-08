package com.example.aniket.easygrades.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aniket.easygrades.R;

/**
 * The fragment is called when a user swipes to see his profile.
 * The user details along with their department and degree is
 * being highlighted on the layout.
 */

public class ThreeFragment extends Fragment{
    /** This is an empty publuc constructor which is necessary for proper functioning*/
    public ThreeFragment() {
        // Required empty public constructor
    }

    /**
     *
     * When Activity is started and application is not loaded, then both onCreate() methods will be called.
     * But for subsequent starts of Activity , the onCreate() of application will not be called
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *
     * When Activity is started and application is not loaded, then both onCreate() methods will be called.
     * But for subsequent starts of Activity , the onCreate() of application will not be called
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_three, container, false);
    }

}