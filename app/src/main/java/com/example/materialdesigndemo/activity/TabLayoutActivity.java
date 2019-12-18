package com.example.materialdesigndemo.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.materialdesigndemo.R;
import com.example.materialdesigndemo.adapter.NewsFragmentAdapter;
import com.example.materialdesigndemo.fragment.NewsFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

//滚动的条目，实现ViewPage与手指的联动

public class TabLayoutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_tab_layout );

        initView ( );
        initViewPager ( );
    }

    private void initViewPager() {
//        1、静态组装tab的标题集合
        List <String> titles = new ArrayList <> ( );
        titles.add ( "头条" );
        titles.add ( "社会" );
        titles.add ( "国内" );
        titles.add ( "国际" );
        titles.add ( "体育" );
        titles.add ( "军事" );
        titles.add ( "科技" );
//2.初始化数据个数fragment的集合
        List <Fragment> fragments = new ArrayList <> ( );
        for (String title : titles) {
            tabs.addTab ( tabs.newTab ( ).setText ( title ) );
            fragments.add ( new NewsFragment ( ) );
        

        }
//3、创建adapter
        NewsFragmentAdapter adapter = new NewsFragmentAdapter ( getSupportFragmentManager ( ), fragments, titles );
//       4.viewPager设置adapter
        viewPager.setAdapter ( adapter );
//        5.关联TableLayout与ViewPager的实现，关键点：adapter必须重写getPageTitle   实现滑动
        tabs.setupWithViewPager ( viewPager );
    }

    private void initView() {
        tabs = findViewById ( R.id.tabs_layout );
        viewPager = findViewById ( R.id.view_pager );

        // 初始化Toolbar
        toolbar = findViewById ( R.id.tool_bar );
        toolbar.setTitle ( "TabLayout示例" );
        setSupportActionBar ( toolbar );
    }
}