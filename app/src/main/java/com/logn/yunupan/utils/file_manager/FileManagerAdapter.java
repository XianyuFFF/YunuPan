package com.logn.yunupan.utils.file_manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.logn.yunupan.R;
import com.logn.yunupan.data.FileManagerItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by qjm3662 on 2016/9/28 0028.
 */

public class FileManagerAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<FileManagerItem> fileList = null;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public static ImageLoader imageLoader = ImageLoader.getInstance();
    // 配置图片加载及显示选项（还有一些其他的配置，查阅doc文档吧）
    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.picture)// 在ImageView加载过程中显示图片
            .showImageForEmptyUri(R.drawable.picture) // image连接地址为空时
            .showImageOnFail(R.drawable.picture) // image加载失败

            .cacheInMemory(true) // 加载图片时会在内存中加载缓存
            .cacheOnDisk(true) // 加载图片时会在磁盘中加载缓存
            .displayer(new FadeInBitmapDisplayer(200)) // 设置用户加载图片task(这里是圆角图片显示)
            .build();

    public FileManagerAdapter(Context context, List<FileManagerItem> fileList) {
        this.fileList = fileList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        System.out.println("get count : " + fileList.size());
        return fileList.size();
    }

    @Override
    public Object getItem(int i) {
        return fileList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.local_file_recording_item, null);
            holder.tv_fileName = (TextView) view.findViewById(R.id.tv_fileName);
            holder.img_icon = (ImageView) view.findViewById(R.id.img_head);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        FileManagerItem fileManagerItem = fileList.get(i);
        holder.tv_fileName.setText(fileList.get(i).getFileName());
        if (fileManagerItem.isImgOrVid()) {
            imageLoader.displayImage("file://" + fileList.get(i).getFilePath(), holder.img_icon, options, animateFirstListener);
        } else {
            holder.img_icon.setImageBitmap(fileList.get(i).getIcon());
        }
        return view;
    }

    class ViewHolder {
        private ImageView img_icon;
        private TextView tv_fileName;
    }


    /**
     * 图片加载监听事件
     **/
    public static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500); // 设置image隐藏动画500ms
                    displayedImages.add(imageUri); // 将图片uri添加到集合中
                }
            }
        }
    }


    /**
     * 缩小图片
     *
     * @param map
     * @param num
     * @return
     */
    private Bitmap small(Bitmap map, float num) {
        Matrix matrix = new Matrix();
        matrix.postScale(num, num);
        return Bitmap.createBitmap(map, 0, 0, map.getWidth(), map.getHeight(), matrix, true);
    }
}
