package com.logn.yunupan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logn.yunupan.R;
import com.logn.yunupan.adapter.holders.UploadItemHolder;
import com.logn.yunupan.adapter.listener.OnRecyclerViewItemClickListener;
import com.logn.yunupan.entity.greendao.FileUploadBean;
import com.logn.yunupan.utils.FileSizeUtils;
import com.logn.yunupan.utils.FileTypeUtils;
import com.logn.yunupan.utils.TimeUtils;
import com.logn.yunupan.utils.eventbus.EventProgressU;
import com.logn.yunupan.utils.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OurEDA on 2017/3/20.
 */

public class UploadListAdapter extends RecyclerView.Adapter<UploadItemHolder> {
    private Context context;
    /**
     * data source
     */
    private List<FileUploadBean> items;
    private OnRecyclerViewItemClickListener listener;

    public void setListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public UploadListAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
    }


    @Override
    public UploadItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_list, parent, false);
        final UploadItemHolder holder = new UploadItemHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do some thing
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, holder.getPosition());
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(UploadItemHolder holder, int position) {
        String fileName = items.get(position).getFile_name();

        holder.fileImg.setImageResource(FileTypeUtils.getBitmapId(fileName));
        holder.fileName.setText(fileName);
        holder.fileSize.setText(FileSizeUtils.formatFileSize(items.get(position).getFile_size()));
        holder.fileTime.setText(TimeUtils.getTimeWithLong(items.get(position).getTime()));

        holder.fileProgress.setVisibility(View.VISIBLE);
        holder.fileProgress.setProgress(0);

        holder.fileCode.setVisibility(View.GONE);

        holder.fileStatus.setVisibility(View.VISIBLE);
        holder.fileStatus.setText(R.string.app_trans_uploading);

        if (items.get(position).isUpload_hasDown()) { //上传完成后
            holder.fileProgress.setVisibility(View.GONE);
            holder.fileCode.setVisibility(View.VISIBLE);

            holder.fileStatus.setText(R.string.app_trans_down);
            holder.fileCode.setText(items.get(position).getFile_code());
        } else {
            holder.fileProgress.setProgress(items.get(position).getUpload_progress());
            if (items.get(position).isUpload_isUploading()) {

                holder.fileStatus.setText(R.string.app_trans_uploading);
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
    public void setItems(int position, FileUploadBean bean) {
        if (position < items.size()) {
            items.set(position, bean);
            Log.e("adapter", "数据源修改成功:\tprogress:" + items.get(position).getUpload_progress());
        }
    }

    /**
     * add data at position one, then notify item change
     *
     * @param item
     */
    public void add(FileUploadBean item) {
        items.add(0, item);
        this.notifyItemInserted(0);
    }

    public void change() {
        this.notifyItemChanged(0);
    }

    public void setProgress(EventProgressU eventU) {
        String md5 = eventU.getBean().getFile_MD5();
        int position;
        for (position = 0; position < items.size(); position++) {
            //找到位置，并刷新。
            if (items.get(position).getFile_MD5().equals(md5)) {
                items.set(position, eventU.getBean());
//                notifyItemChanged(position);
                notifyItemRangeChanged(0, items.size() - 1);
                break;
            }
        }
        //没找到的话，添加进去
        if (position >= items.size()) {
            add(eventU.getBean());
        }
    }

    public void moveItem(String MD5, String path) {
        Logger.e("准备移除：->" + path);
        if (MD5 == "" || path == "") {
            return;
        }
        for (int position = 0; position < items.size(); position++) {
            //找到位置，并刷新。
            if (items.get(position).getFile_MD5() == null) {
                continue;
            }
            Logger.e("MD5:\t" + items.get(position).getFile_MD5() + "\n\t" + MD5);
            if (items.get(position).getFile_MD5().equals(MD5)) {
                //移除数据集和条目
                items.remove(position);
                notifyItemRemoved(position);
                break;
            }
        }
    }
}
