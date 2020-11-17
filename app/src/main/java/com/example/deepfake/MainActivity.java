package com.example.deepfake;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private FragmentManager mFragManager;
    private FragmentTransaction mFragTransaction;
    private frag_home mFragHome;
    private frag_add mFragAdd;
    private frag_mypage mFragMyPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottomNavi);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        setFrag(0);
                        break;
                    case R.id.action_add:
                        setFrag(1);
                        break;
                    case R.id.action_mypage:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });

        mFragHome = new frag_home();
        mFragAdd = new frag_add();
        mFragMyPage = new frag_mypage();
        setFrag(0);
    }

    private void setFrag(int n) {
        mFragManager = getSupportFragmentManager();
        mFragTransaction = mFragManager.beginTransaction();
        switch (n) {
            case 0:
                mFragTransaction.replace(R.id.main_frame, mFragHome);
                mFragTransaction.commit();
                break;
            case 1:
                mFragTransaction.replace(R.id.main_frame, mFragAdd);
                mFragTransaction.commit();
                break;
            case 2:
                mFragTransaction.replace(R.id.main_frame, mFragMyPage);
                mFragTransaction.commit();
                break;
        }
    }
}