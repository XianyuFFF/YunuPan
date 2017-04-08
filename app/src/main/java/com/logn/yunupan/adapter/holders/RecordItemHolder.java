package com.logn.yunupan.adapter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.logn.yunupan.R;
import com.logn.yunupan.utils.ToastShort;

import org.w3c.dom.Text;


/**
 * Created by OurEDA on 2016/12/17.
 */

public class RecordItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView fileName;
    public TextView fileCode;
    public TextView fileSize;
    public TextView fileTime;

    public ImageView fileImg;

    public RecordItemHolder(View itemView) {
        super(itemView);
        //在这里绑定所有控件
        this.fileImg = (ImageView) itemView.findViewById(R.id.item_list_image);
        this.fileName = (TextView) itemView.findViewById(R.id.item_list_filename);
        this.fileCode = (TextView) itemView.findViewById(R.id.item_list_code);
        this.fileSize = (TextView) itemView.findViewById(R.id.item_list_file_size);
        this.fileTime = (TextView) itemView.findViewById(R.id.item_list_file_time);


        //为item绑定监听器
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //do something
        ToastShort.showS("点击我了:" + getPosition() + ":" + getAdapterPosition());
    }
}
