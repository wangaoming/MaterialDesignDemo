package com.example.materialdesigndemo.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.materialdesigndemo.R;
import com.example.materialdesigndemo.adapter.NewsFragmentAdapter;
import com.example.materialdesigndemo.fragment.NewsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_navigation );
//       初始化toolbar
        toolbar = findViewById ( R.id.tool_bar );
        setSupportActionBar ( toolbar );
//        抽屉菜单以及导航的设置
        drawerLayout = findViewById ( R.id.drawer_layout );
        navigationView = findViewById ( R.id.nav_view );
        //        DrawerLayout的初始化，以及设置切换
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle ( this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close );
        toggle.syncState ( );
        drawerLayout.addDrawerListener ( toggle );
        toggle.syncState ( );

        navigationView.setNavigationItemSelectedListener ( this );



        tabs = findViewById ( R.id.tabs_layout );
        viewPager = findViewById ( R.id.view_pager );
        initViewPager ( );
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen ( GravityCompat.START )) {
            drawerLayout.closeDrawer ( GravityCompat.START );
        } else {
            super.onBackPressed ( );
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId ( )) {
            case R.id.nav_camera:
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_send:
                break;
            case R.id.nav_share:
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer ( GravityCompat.START );
        return true;
    }

    private void initViewPager() {
        List <String> titles = new ArrayList <> ( );
        titles.add ( "精选" );
        titles.add ( "新闻" );
        titles.add ( "体育" );
        titles.add ( "购物" );
        titles.add ( "视频" );
        titles.add ( "健康" );

        List <Fragment> fragments = new ArrayList <> ( );
        for (String title : titles) {
            tabs.addTab ( tabs.newTab ( ).setText ( title ) );
            fragments.add ( new NewsFragment ( ) );
        }

        NewsFragmentAdapter adapter = new NewsFragmentAdapter ( getSupportFragmentManager ( ), fragments, titles );
        viewPager.setAdapter ( adapter );
        tabs.setupWithViewPager ( viewPager );
    }
}