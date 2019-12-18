package com.example.materialdesigndemo.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.materialdesigndemo.R;
import com.example.materialdesigndemo.activity.NewsDetailActivity;
import com.example.materialdesigndemo.adapter.NewsAdapter;
import com.example.materialdesigndemo.model.News;

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

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final int GET_NEWS = 1;

    private NewsHandler handler;
    private List<News> newsList;
    private NewsAdapter adapter;

    private RecyclerView rvNews;
    private SwipeRefreshLayout refreshLayout;

    public NewsFragment() {
        // Required empty public constructor
    }
//传参数
    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        handler = new NewsHandler(this);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        rvNews = view.findViewById(R.id.rv_news);
        refreshLayout = view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        //        refreshLayout.setColorSchemeResources(
//                android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
    }

    private void initData() {
        String url = "http://v.juhe.cn/toutiao/index?key=75b707a22268c6da84fc8e7c15ecb8d7&type=top";
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("NewsFragment", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    String json = response.body().string();
                    JSONObject obj = JSON.parseObject(json);
                    JSONObject result = obj.getJSONObject("result");
                    if(result != null) {
                        JSONArray data = result.getJSONArray("data");

                        Message msg = handler.obtainMessage();
                        msg.what = GET_NEWS;
                        msg.obj = data.toJSONString();
                        handler.sendMessage(msg);
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        initData();
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    static class NewsHandler extends Handler {
        private WeakReference<Fragment> ref;

        public NewsHandler(Fragment fragment) {
            this.ref = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final NewsFragment fragment = (NewsFragment) this.ref.get();
            if(msg.what == GET_NEWS) {
                String json = (String) msg.obj;
                fragment.newsList = JSON.parseArray(json, News.class);
                fragment.adapter = new NewsAdapter(fragment.newsList);
//                设置分割条
                DividerItemDecoration decoration = new DividerItemDecoration(fragment.getContext(), DividerItemDecoration.VERTICAL);
                fragment.rvNews.addItemDecoration(decoration);
//                获取上下文
                fragment.rvNews.setLayoutManager(new LinearLayoutManager(fragment.getContext()));
                fragment.rvNews.setAdapter(fragment.adapter);
                fragment.adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(fragment.getContext(), NewsDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("news", fragment.newsList.get(position));
                        intent.putExtras(bundle);
                        fragment.getContext().startActivity(intent);
                    }
                });
            }
        }
    }

}