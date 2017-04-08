package com.logn.yunupan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logn.yunupan.R;
import com.logn.yunupan.adapter.holders.DownloadItemHolder;
import com.logn.yunupan.entity.greendao.FileDownloadBean;
import com.logn.yunupan.utils.FileSizeUtils;
import com.logn.yunupan.utils.FileTypeUtils;
import com.logn.yunupan.utils.TimeUtils;
import com.logn.yunupan.utils.eventbus.EventProgressD;
import com.logn.yunupan.utils.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OurEDA on 2017/3/31.
 */

public class DownloadListAdapter extends RecyclerView.Adapter<DownloadItemHolder> {
    private Context context;
    /**
     * data source
     */
    private List<FileDownloadBean> items;

    public DownloadListAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
    }


    @Override
    public DownloadItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_list, parent, false);
        DownloadItemHolder holder = new DownloadItemHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(DownloadItemHolder holder, int position) {
        //设置各种状态
        //文件名，图标，大小，时间，提取码，
        // 进度条，状态....
        FileDownloadBean bean = items.get(position);
        String fileName = bean.getFile_name();

        holder.fileImg.setImageResource(FileTypeUtils.getBitmapId(fileName));
        holder.fileName.setText(fileName);
        holder.fileSize.setText(FileSizeUtils.formatFileSize(bean.getFile_size()));
        holder.fileTime.setText(TimeUtils.getTimeWithLong(bean.getTime()));
        holder.fileCode.setText(bean.getFile_code());

        holder.fileProgress.setVisibility(View.VISIBLE);
        holder.fileProgress.setProgress(bean.getDownload_progress());

        holder.fileStatus.setVisibility(View.VISIBLE);
        holder.fileStatus.setText(R.string.app_trans_downloading);

        if (bean.getDownload_hasDown()) {
            holder.fileStatus.setText(R.string.app_trans_down);
            holder.fileProgress.setVisibility(View.GONE);

        } else {
            if (bean.isDownload_isDownloading()) {
                holder.fileStatus.setText(R.string.app_trans_downloading);
            } else {
                holder.fileStatus.setText(R.string.app_trans_pause);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    /**
     * change the data at position
     *
     * @param position
     * @param bean
     */
    public void setItems(int position, FileDownloadBean bean) {
        if (position < items.size()) {
            items.set(position, bean);
        }
    }

    /**
     * add data at position one, then notify item change
     *
     * @param item
     */
    public void add(FileDownloadBean item) {
        items.add(0, item);
        this.notifyItemChanged(0);
    }

    public void change() {
        this.notifyItemChanged(0);
    }

    public void setProgress(EventProgressD eventD) {
        String code = eventD.getBean().getFile_code();
        int position;
        for (position = 0; position < items.size(); position++) {
            //找到位置，并刷新。
            if (items.get(position).getFile_code().equals(code)) {
                items.set(position, eventD.getBean());
//                notifyItemChanged(position);
                notifyItemRangeChanged(0, items.size() - 1);
                break;
            }
        }
        //没找到的话，添加进去
        if (position >= items.size()) {
            add(eventD.getBean());
        }
    }

    public void moveItem(String code) {
        Logger.e("准备移除：->" + code);
        if (code == "") {
            return;
        }
        for (int position = 0; position < items.size(); position++) {
            //找到位置，并刷新。
            if (items.get(position).getFile_code().equals(code)) {
                //移除数据集和条目
                items.remove(position);
                notifyItemRemoved(position);
                break;
            }
        }
    }
}
