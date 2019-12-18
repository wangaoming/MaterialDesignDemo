package com.example.materialdesigndemo.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.example.materialdesigndemo.R;
import com.example.materialdesigndemo.adapter.NewsFragmentAdapter;
import com.example.materialdesigndemo.fragment.NewsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class DesignNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TabLayout tabs;
    private ViewPager viewPager;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_design_navigation );
//       另一种byId方式 使用ButterKnife

        Toolbar toolbar = findViewById ( R.id.tool_bar );
//       初始化toolbar
        setSupportActionBar ( toolbar );
//        初始化floatingactionbutton
        FloatingActionButton fab = findViewById ( R.id.fab );
        fab.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                Snackbar.make ( view, "Replace with your own action", Snackbar.LENGTH_LONG )
                        .setAction ( "Action", null ).show ( );
            }
        } );
//        抽屉菜单以及导航的设置
        drawer = findViewById ( R.id.drawer_layout );
        navigationView = findViewById ( R.id.nav_view );
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

//        DrawerLayout的初始化，以及设置切换
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle ( this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close );
        toggle.syncState ( );
        drawer.addDrawerListener ( toggle );
        toggle.syncState ( );
//        左侧抽屉导航项与内容模板的显示的Vewpager做联动
        NavController navController = Navigation.findNavController ( this, R.id.view_pager );
        NavigationUI.setupActionBarWithNavController ( this, navController, mAppBarConfiguration );
        NavigationUI.setupWithNavController ( navigationView, navController );

        tabs = findViewById ( R.id.tabs_layout );
        viewPager = findViewById ( R.id.view_pager );
        navigationView.setNavigationItemSelectedListener ( this );

        initViewPager ( );
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen ( GravityCompat.START )) {
            drawer.closeDrawer ( GravityCompat.START );
        } else {
            super.onBackPressed ( );
        }
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

    //加载项首选菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ( ).inflate ( R.menu.design_navigation, menu );
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController ( this, R.id.nav_host_fragment );
        return NavigationUI.navigateUp ( navController, mAppBarConfiguration )
                || super.onSupportNavigateUp ( );
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
        drawer.closeDrawer ( GravityCompat.START );
        return true;
    }
}
