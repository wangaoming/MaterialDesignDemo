package com.example.materialdesigndemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.materialdesigndemo.R;
import com.example.materialdesigndemo.adapter.NewsAdapter;
import com.example.materialdesigndemo.model.News;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CoordinatorActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final int GET_NEWS = 1;

    private NewsHandler handler;
    private List <News> newsList;
    private NewsAdapter adapter;

    private Toolbar toolbar;
    private RecyclerView rvNews;
    private SwipeRefreshLayout refreshLayout;
    //    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_coordinatory );

        handler = new NewsHandler ( this );

        initView ( );
        initData ( );
    }

    private void initView() {
        // 1. 初始化Toolbar

        // 2. 初始化RecyclverView列表和自动刷新布局
        rvNews = findViewById ( R.id.rv_news );
        refreshLayout = findViewById ( R.id.refresh );
//       解决SwipeREfreshLayout的下拉刷新与Coordintorlayout的冲突
        rvNews.addOnScrollListener ( new RecyclerView.OnScrollListener ( ) {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged ( recyclerView, newState );
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled ( recyclerView, dx, dy );
                int toRowPos = (rvNews == null || rvNews.getChildCount ( ) == 0) ? 0 :
                        rvNews.getChildAt ( 0 ).getTop ( );
                refreshLayout.setEnabled ( toRowPos >= 0 );
            }
        } );

        refreshLayout.setOnRefreshListener ( this );
    }

    private void setSupportActionBar(Toolbar toolbar) {

    }


    private void initData() {

        String url = "http://v.juhe.cn/toutiao/index?key=75b707a22268c6da84fc8e7c15ecb8d7&type=top";
        OkHttpClient client = new OkHttpClient.Builder ( )
                .connectTimeout ( 10, TimeUnit.SECONDS )
                .writeTimeout ( 10, TimeUnit.SECONDS )
                .readTimeout ( 10, TimeUnit.SECONDS )
                .build ( );
        Request request = new Request.Builder ( ).url ( url ).build ( );
        client.newCall ( request ).enqueue ( new Callback ( ) {
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

                        Message msg = handler.obtainMessage ( );
                        msg.what = GET_NEWS;
                        msg.obj = data.toJSONString ( );
                        handler.sendMessage ( msg );
                    }else{
                        Message msg = handler.obtainMessage ();
                    }
                }
            }
        } );
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
            final CoordinatorActivity activity = (CoordinatorActivity) this.ref.get ( );
            if (msg.what == GET_NEWS) {
                String json = (String) msg.obj;
                activity.newsList = JSON.parseArray ( json, News.class );

                DividerItemDecoration decoration = new DividerItemDecoration ( activity, DividerItemDecoration.VERTICAL );
                activity.rvNews.addItemDecoration ( decoration );
                activity.rvNews.setLayoutManager ( new LinearLayoutManager ( activity ) );

                activity.adapter = new NewsAdapter ( activity.newsList );
                activity.rvNews.setAdapter ( activity.adapter );
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

    public void sendEmail(View view) {
        Snackbar.make ( view, "正在发送邮件。。。", Snackbar.LENGTH_LONG ).show ( );
    }
}