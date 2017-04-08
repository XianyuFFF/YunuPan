package com.logn.yunupan.views;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.logn.yunupan.adapter.SimpleAdapter;
import com.logn.yunupan.utils.clipboard.ClipboardManagerCompat;
import com.logn.yunupan.utils.eventbus.EventBusInstance;
import com.logn.yunupan.utils.eventbus.EventToastInfo;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OurEDA on 2017/4/8.
 */

public class DialogFileOperation {
    public static final String DIALOG_FILE_INFO = "文件信息";
    public static final String DIALOG_COPY = "复制提取码";
    public static final String DIALOG_QR = "生成二维码";
    public static final String DIALOG_DELETE_RECORD = "删除记录";


    private SimpleAdapter adapter;
    private DialogPlus dialog;
    private DialogPlusBuilder builder;
    private List<String> list;
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
        if (builder != null) {
            builder.setOnItemClickListener(listener);
        }
    }

    public void setList(List<String> list) {
        this.list = list;
        if (adapter != null) {
            adapter.setList(list);
        }
    }

    private static DialogFileOperation dialogFileOperation;

    private DialogFileOperation(final Context context) {

        list = new ArrayList<>();
        initList();
        adapter = new SimpleAdapter(context, true);
        adapter.setList(list);
        builder = DialogPlus.newDialog(context)
                .setAdapter(adapter)
                .setExpanded(false)
                .setGravity(Gravity.CENTER)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
//                        switch (list.get(position)) {
//                            case DIALOG_FILE_INFO:
//                                break;
//                            case DIALOG_COPY:
//                                ClipboardManagerCompat clipboardManagerCompat = ClipboardManagerCompat.create(context);
//                                clipboardManagerCompat.addText("时间戳：" + System.currentTimeMillis());
//                                break;
//                            case DIALOG_QR:
//                                break;
//                            case DIALOG_DELETE_RECORD:
//                                break;
//                            default:
//                        }
//                        dialog.dismiss();
                    }
                });
    }

    private void initList() {
        list.add(DIALOG_FILE_INFO);
        list.add(DIALOG_COPY);
        list.add(DIALOG_QR);
        list.add(DIALOG_DELETE_RECORD);
    }

    public void show() {
        if (builder != null) {
            dialog = builder.create();
            dialog.show();
        }else {
            EventBusInstance.getBusInstance().post(new EventToastInfo("null???????"));
        }
    }

    public static DialogFileOperation createDialog(Context context) {
        if (dialogFileOperation == null) {
            dialogFileOperation = new DialogFileOperation(context);
        }
        return dialogFileOperation;
    }
}
