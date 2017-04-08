package com.logn.yunupan.adapter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.logn.yunupan.R;

/**
 * Created by OurEDA on 2017/3/20.
 */

public class UploadItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public TextView fileName;
    public TextView fileCode;
    public TextView fileSize;
    public TextView fileTime;
    public TextView fileStatus;
    public NumberProgressBar fileProgress;

    public ImageView fileImg;

    public UploadItemHolder(View itemView) {
        super(itemView);
        //在这里绑定所有控件
        this.fileImg = (ImageView) itemView.findViewById(R.id.item_list_image);
        this.fileName = (TextView) itemView.findViewById(R.id.item_list_filename);
        this.fileCode = (TextView) itemView.findViewById(R.id.item_list_code);
        this.fileSize = (TextView) itemView.findViewById(R.id.item_list_file_size);
        this.fileTime = (TextView) itemView.findViewById(R.id.item_list_file_time);
        this.fileStatus = (TextView) itemView.findViewById(R.id.item_list_file_status);
        this.fileProgress = (NumberProgressBar) itemView.findViewById(R.id.item_list_progress);

        //为item绑定监听器
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //do something
    }
}
