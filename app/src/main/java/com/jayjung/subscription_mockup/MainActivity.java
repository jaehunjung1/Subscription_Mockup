package com.jayjung.subscription_mockup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean beenToEditActivity = false;
    int editPos = -1;
    boolean beenToGroupEditActivity = false;
    int groupEditPos = -1;

    SharedPreferences sharedPref;

    TabLayout tabLayout;
    ViewPager viewPager;

    NotificationFragment notificationFragment;
    GroupFragment groupFragment;

    NotificationManager notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.main_viewpager);
        initViewPager(viewPager);

        tabLayout = findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (beenToEditActivity) {
            beenToEditActivity = false;
            Intent intent = getIntent();
            if (intent.getBooleanExtra("canceled", false))
                return;

            boolean isAdd = intent.getBooleanExtra("isAdd", false);

            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            String channel = intent.getStringExtra("channel");

            notificationFragment.addOrEditNotification(isAdd, title, text, channel);
        } else if (beenToGroupEditActivity) {
            beenToGroupEditActivity = false;
            Intent intent = getIntent();
            if (intent.getBooleanExtra("canceled", false))
                return;

            boolean isAdd = intent.getBooleanExtra("isAdd", false);

            String groupName = intent.getStringExtra("name");

            groupFragment.addOrEditGroup(isAdd, groupName);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void initViewPager(ViewPager viewPager) {
        notificationFragment = new NotificationFragment();
        groupFragment = new GroupFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), 2);
        adapter.addFragment(notificationFragment, "NOTIFICATION");
        adapter.addFragment(groupFragment, "CHANNEL GROUP");

        viewPager.setAdapter(adapter);
    }

    public void onStartButtonClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            groupFragment.onStartButtonClick(notificationManager, view);
        }

        notificationFragment.onStartButtonClick(view);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        final ArrayList<String> tabNameArrayList = new ArrayList<>();


        public ViewPagerAdapter(@NonNull FragmentManager manager, int numOfTabs) {
            super(manager, numOfTabs);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        public void addFragment(Fragment fragment, String name) {
            fragmentArrayList.add(fragment);
            tabNameArrayList.add(name);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNameArrayList.get(position);
        }
    }
}
