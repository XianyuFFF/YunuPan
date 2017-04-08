package com.logn.yunupan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.logn.yunupan.R;

import java.util.ArrayList;
import java.util.List;

public class SimpleAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private boolean isGrid;

    private List<String> list;

    public void setList(List<String> list) {
        this.list = list;
    }

    public SimpleAdapter(Context context, boolean isGrid) {
        layoutInflater = LayoutInflater.from(context);
        this.isGrid = isGrid;
        list = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.info, parent, false);
//      if (isGrid) {
//        view = layoutInflater.inflate(R.layout.simple_grid_item, parent, false);
//      } else {
//        view = layoutInflater.inflate(R.layout.simple_list_item, parent, false);
//      }

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.dialog_info);
//      viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Context context = parent.getContext();
        viewHolder.textView.setText(list.get(position));
        /*switch (position) {
            case 0:
                viewHolder.textView.setText(context.getString(R.string.app_download));
//        viewHolder.imageView.setImageResource(R.drawable.back);
                break;
            case 1:
                viewHolder.textView.setText(context.getString(R.string.app_download_file));
//        viewHolder.imageView.setImageResource(R.drawable.icon_download_cloud2);
                break;
            default:
                viewHolder.textView.setText(context.getString(R.string.app_trans_stop));
//        viewHolder.imageView.setImageResource(R.drawable.back);
                break;
        }*/

        return view;
    }

    public static class ViewHolder {
        public TextView textView;
//    ImageView imageView;
    }
}
