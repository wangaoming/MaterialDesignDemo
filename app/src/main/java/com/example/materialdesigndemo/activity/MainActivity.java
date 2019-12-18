package com.example.materialdesigndemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.materialdesigndemo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        // 设置标题栏的标题
        Toolbar toolbar = findViewById ( R.id.tool_bar );
//        toolbar.setNavigationIcon(R.mipmap.ic_comment);
        toolbar.setTitle ( "Android 5.0新特性" );
        toolbar.setLogo ( R.mipmap.ic_launcher );
        toolbar.setSubtitle ( "Material Design Support控件" );
        setSupportActionBar ( toolbar );
    }

    public void onClick(View view) {
        switch (view.getId ( )) {
            case R.id.btn_recycler:
                Intent intent = new Intent ( MainActivity.this, RecycleViewActivity.class );
                startActivity ( intent );
                break;
            case R.id.btn_collapseing:
                intent = new Intent ( MainActivity.this, CoordinatorActivity.class );
                startActivity ( intent );
                break;
            case R.id.btn_tab:
                intent = new Intent ( MainActivity.this, TabLayoutActivity.class );
                startActivity ( intent );
                break;
            case R.id.btn_navigation:
                intent = new Intent ( MainActivity.this, NavigationActivity.class );
                startActivity ( intent );
                break;
            case R.id.btn_notification:
                intent = new Intent ( MainActivity.this, NotificationActivity.class );
                startActivity ( intent );
                break;
        }
    }
}