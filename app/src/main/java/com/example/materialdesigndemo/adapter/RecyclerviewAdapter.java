package com.example.materialdesigndemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.materialdesigndemo.R;
import com.example.materialdesigndemo.news.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter <RecyclerviewAdapter.ViewHolder> {
    private Context context;
    private List <Data> datas;

    private OnItemClickListener listener;

    private OnItemLongClickListener longClickListener;

    public RecyclerviewAdapter(Context context, List <Data> datas) {
        this.context = context;
        this.datas = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from ( context ).inflate ( R.layout.item_news, parent, false );
        ViewHolder viewHolder = new ViewHolder ( view );
//        viewHolder.tvTitle1=view.findViewById(R.id.mtvtitle);
//        viewHolder.tvTitle2=view.findViewById(R.id.mtvtitle2);
//        viewHolder.tvTitle3=view.findViewById(R.id.mtvtitle3);
//        viewHolder.imgView=view.findViewById(R.id.mimv);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        holder.tvTitle1.setText ( datas.get ( i ).getTitle ( ) );
        holder.tvTitle2.setText ( datas.get ( i ).getAuthor_name ( ) );
        Date date = datas.get ( i ).getDate ( );
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss EEEEEEEEE" );
        holder.tvTitle3.setText ( format.format ( date ) );
        Glide.with ( context ).load ( datas.get ( i ).getThumbnail_pic_s ( ) ).into ( holder.imgView );
        holder.itemView.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick ( i );
                }
            }
        } );

        holder.itemView.setOnLongClickListener ( new View.OnLongClickListener ( ) {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onClick ( i );
                }
                return true;
            }
        } );
    }


    @Override
    public int getItemCount() {
        return datas.size ( );
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super ( v );
        }

        TextView tvTitle1, tvTitle2, tvTitle3;
        ImageView imgView;
    }


    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }


    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
}
