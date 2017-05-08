package com.example.aniket.easygrades;

/**
 * Created by Aniket on 4/30/2017.
 */


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.aniket.easygrades.fragments.OneFragment;
import com.example.aniket.easygrades.fragments.ThreeFragment;
import com.example.aniket.easygrades.fragments.TwoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 *     This is the first page that a user will see after it has loggen in to the application. <br>
 *     It contains three fragments which are represented as TABs in the homepage
 */
public class HomePage extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    /**
     *  When Activity is started and application is not loaded, then both onCreate() methods will be called.
     *  But for subsequent starts of Activity , the onCreate() of application will not be called
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        toolbar = (Toolbar) findViewById(R.id.toolbar_add);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager_add);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs_add);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Setup the pager which will contain adapter to display all the three fragments as TABs
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Courses");
        adapter.addFragment(new TwoFragment(), "Find Course");
        adapter.addFragment(new ThreeFragment(), "Profile");
        viewPager.setAdapter(adapter);
    }

    /**
     *   Subclass for the Page Adapter. All the functions of this class are used only in Homepage Class and nowhere else.
     *   Therefore it is defined as a subclass of the Homepage Class.
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
