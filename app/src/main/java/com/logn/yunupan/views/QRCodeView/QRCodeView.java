package com.logn.yunupan.views.QRCodeView;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.logn.yunupan.R;
import com.logn.yunupan.module.QRScan.zxing.QRCodeEncoder;
import com.logn.yunupan.utils.DisplayUtils;
import com.logn.yunupan.utils.ScanMsgUtil;

/**
 * 二维码展示dialog
 * <p>
 * Created by ran on 2016/10/31.
 */

public class QRCodeView extends DialogFragment {

    private ScanMsgUtil.ScanType type = ScanMsgUtil.ScanType.OTHERS;
    private String data = "QRCode";

    Dialog mDialog;

    public QRCodeView() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mDialog == null) {
            mDialog = new Dialog(getActivity(), R.style.cart_dialog);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.setContentView(R.layout.dialog_qr_code);
            mDialog.getWindow().setGravity(Gravity.CENTER);

            View view = mDialog.getWindow().getDecorView();
            final ImageView ivQR = (ImageView) view.findViewById(R.id.iv_dialog_qr_code);
            final TextView tvQR = (TextView) view.findViewById(R.id.tv_dialog_qr_msg);

            Bitmap bitmapLogo = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.logo_cloud_transparent);
            Bitmap bitmap = QRCodeEncoder.syncEncode(data, DisplayUtils.dip2px(getContext(), 250), Color.BLACK, bitmapLogo, true);

            ivQR.setImageBitmap(bitmap);
            tvQR.setText(getType(type));
        }
        return mDialog;
    }

    public void setType(ScanMsgUtil.ScanType type) {
        this.type = type;
    }

    private String getType(ScanMsgUtil.ScanType type) {
//        if (type == ScanMsgUtil.ScanType.PERSON) {
//            return "扫一扫上面二维码，和我聊天";
//        } else if (type == ScanMsgUtil.ScanType.SUBSCRIBE) {
//            return "扫一扫上面二维码，关注公众号";
//        } else if (type == ScanMsgUtil.ScanType.CLASS) {
//            return "扫一扫上面二维码，添加课程";
//        } else {
//            return "扫一扫上面二维码，获取内容";
//        }

        return "扫一扫上面二维码，获取提取码";
    }

    /**
     * @param data
     */
    public void setData(String data) {
        this.data = data;
    }
}
