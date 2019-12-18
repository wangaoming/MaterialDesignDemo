package com.example.materialdesigndemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.materialdesigndemo.R;
import com.example.materialdesigndemo.adapter.NewsAdapter;
import com.example.materialdesigndemo.adapter.NewsGridAdapter;
import com.example.materialdesigndemo.model.News;
import com.example.materialdesigndemo.utils.HttpsUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class RecycleViewActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {
    // 常量：聚合数据的url网址和handle的消息
    private static final int GET_NEWS = 1;

    // 控件对象
    private Toolbar toolbar;
    private RecyclerView rvNews;
    private SwipeRefreshLayout refreshLayout;

    // 数据处理的对象
    private NewsHandler handler;
    private List <News> newsList;
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_recycle_view );

        handler = new NewsHandler ( this );

        initView ( );
        initData ( );
    }

    private void initView() {
        // 1. 初始化Toolbar
        toolbar = findViewById ( R.id.tool_bar );
        toolbar.setTitle ( "RecyclverView示例" );
        setSupportActionBar ( toolbar );

        // 2. 初始化RecyclverView列表和自动刷新布局
        rvNews = findViewById ( R.id.rv_news );
        refreshLayout = findViewById ( R.id.refresh );

        // 3. 设置SwipeRefreshLayout的事件监听和进度条颜色
        refreshLayout.setOnRefreshListener ( this );
//        refreshLayout.setColorSchemeResources(
//                android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
    }


    private void initData() {
        String url = "http://v.juhe.cn/toutiao/index?key=75b707a22268c6da84fc8e7c15ecb8d7&type=top";
        Request request = new Request.Builder ( ).url ( url ).build ( );
        HttpsUtil.getInstance ( ).newCall ( request ).enqueue ( new Callback ( ) {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e ( "NewsListActivity", e.getMessage ( ) );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful ( )) {
                    String json = response.body ( ).string ( );
                    JSONObject obj = JSON.parseObject ( json );
                    JSONObject result = obj.getJSONObject ( "result" );
                    if (result != null) {
                        JSONArray data = result.getJSONArray ( "data" );
                        if (data != null && !data.isEmpty ( )) {
                            Message msg = handler.obtainMessage ( );
                            msg.what = GET_NEWS;
                            msg.obj = data.toJSONString ( );  //
                            handler.sendMessage ( msg );
                        }
                    }
                } else {
                    Log.e ( "NewsListActivity", response.message ( ) );
                }
            }
        } );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ( ).inflate ( R.menu.recycler_style, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId ( )) {
            case R.id.item_linear:
                rvNews.setLayoutManager ( new LinearLayoutManager ( this ) );
                adapter = new NewsAdapter ( newsList );
                rvNews.setAdapter ( adapter );
                break;
            case R.id.item_grid:
                rvNews.setLayoutManager ( new GridLayoutManager ( this, 2 ) );
                NewsGridAdapter adapter = new NewsGridAdapter ( newsList );
                rvNews.setAdapter ( adapter );
                break;
            case R.id.item_stagger:
                rvNews.setLayoutManager ( new StaggeredGridLayoutManager ( 3, LinearLayoutManager.VERTICAL ) );
                adapter = new NewsGridAdapter ( newsList );
                rvNews.setAdapter ( adapter );
                break;
        }
        return super.onOptionsItemSelected ( item );
    }

    @Override
    public void onRefresh() {
        initData ( );
        refreshLayout.postDelayed ( new Runnable ( ) {
            @Override
            public void run() {
                refreshLayout.setRefreshing ( false );
            }
        }, 3000 );
    }

    static class NewsHandler extends Handler {
        private WeakReference <Activity> ref;

        public NewsHandler(Activity activity) {
            this.ref = new WeakReference <> ( activity );
        }

        @Override
        public void handleMessage(Message msg) {
            final RecycleViewActivity activity = (RecycleViewActivity) this.ref.get ( );
            if (msg.what == GET_NEWS) {
                // 获取数据
                String json = (String) msg.obj;
                activity.newsList = JSON.parseArray ( json, News.class );

                // 设置RecyclerView的分割线和布局
//                DividerItemDecoration decoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
//                activity.rvNews.addItemDecoration(decoration);
                activity.rvNews.setLayoutManager ( new LinearLayoutManager ( activity ) );

                // 设置Adapter
                activity.adapter = new NewsAdapter ( activity.newsList );
                activity.rvNews.setAdapter ( activity.adapter );

                // 设置事件监听
                activity.adapter.setOnItemClickListener ( new NewsAdapter.OnItemClickListener ( ) {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent ( activity, NewsDetailActivity.class );
                        Bundle bundle = new Bundle ( );
                        bundle.putSerializable ( "news", activity.newsList.get ( position ) );
                        intent.putExtras ( bundle );
                        activity.startActivity ( intent );
                    }
                } );
            }
        }
    }

}