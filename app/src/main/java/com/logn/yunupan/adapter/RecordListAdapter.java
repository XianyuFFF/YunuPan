package com.logn.yunupan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logn.yunupan.R;
import com.logn.yunupan.adapter.holders.RecordItemHolder;
import com.logn.yunupan.adapter.listener.OnRecyclerViewItemClickListener;
import com.logn.yunupan.adapter.listener.OnRecyclerViewItemLongClickListener;
import com.logn.yunupan.entity.greendao.FileInfoBean;
import com.logn.yunupan.utils.FileSizeUtils;
import com.logn.yunupan.utils.FileTypeUtils;
import com.logn.yunupan.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * \
 * Created by OurEDA on 2016/12/17.
 */

public class RecordListAdapter extends RecyclerView.Adapter<RecordItemHolder> {

    private List<FileInfoBean> items;//存储的数据
    private Context context;

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener;

    public RecordListAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
    }

    @Override
    public RecordItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item布局
        //这里可以以viewType的值不同为item加载不同的布局
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_list, parent, false);
        final RecordItemHolder holder = new RecordItemHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerViewItemClickListener != null) {
                    onRecyclerViewItemClickListener.onItemClick(v, holder.getPosition());
                }
            }
        });
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewItemLongClickListener != null) {
                    return onRecyclerViewItemLongClickListener.onLongClick(v, holder.getPosition());
                }
                return false;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecordItemHolder holder, int position) {
        String fileName = items.get(position).getFile_name();
        //文件图标
        holder.fileImg.setImageResource(FileTypeUtils.getBitmapId(fileName));
        holder.fileName.setText(fileName);
        holder.fileCode.setText(items.get(position).getFile_code());
        holder.fileTime.setText(TimeUtils.getTimeWithLong(items.get(position).getTime()));
        holder.fileSize.setText(FileSizeUtils.formatFileSize(items.get(position).getFile_size()));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void add(FileInfoBean item) {
        items.add(item);
        this.notifyItemInserted(items.size());
    }

    public void del(FileInfoBean item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getFile_code().equals(item.getFile_code())) {
                items.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public void setOnRecyclerViewItemLongClickListener(OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener) {
        this.onRecyclerViewItemLongClickListener = onRecyclerViewItemLongClickListener;
    }
}
