package com.logn.yunupan.activity.scan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.logn.yunupan.R;
import com.logn.yunupan.module.QRScan.qrcode.core.QRCodeView;
import com.logn.yunupan.module.QRScan.zxing.QRCodeDecoder;
import com.logn.yunupan.utils.DisplayUtils;
import com.logn.yunupan.utils.PermissionUtils;
import com.logn.yunupan.utils.ScanMsgUtil;
import com.logn.yunupan.utils.SpUtils;
import com.logn.yunupan.utils.StatusBarUtil;
import com.logn.yunupan.utils.eventbus.EventBusInstance;
import com.logn.yunupan.utils.eventbus.EventToastInfo;
import com.logn.yunupan.utils.logger.Logger;
import com.logn.yunupan.views.TitleBarView.TitleBar;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

import static com.logn.yunupan.data.PostFlag.FIRST_USE_CAMERA;

/**
 * Scan QR activity
 *
 * @author liufengkai
 *         Created by liufengkai on 16/8/1.
 */
public class ScanQRActivity extends RxAppCompatActivity
        implements ScanQRContract.View {

    /**
     * create scan QR Activity
     */
    public ScanQRActivity() {
        ScanQRContract.Presenter presenter = new ScanQRPresenter(this);
        this.setPresenter(presenter);
    }

    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案

    // qr code view
    @BindView(R.id.zxingview)
    QRCodeView mQRCodeView;

    // title bar
    @BindView(R.id.scan_qr_activity_title_bar)
    TitleBar titleBar;

    // layout to show info
    @BindView(R.id.scan_qr_permission_layout)
    FrameLayout mPermissionLayout;

    //private CatLoadingView loadingView;
    private boolean isClose;

    private ScanMsgUtil.ScanType type;
    private String pathFromAlbum;
    private QRCodeDecoder.Delegate delegateAlbum = new QRCodeDecoder.Delegate() {
        @Override
        public void onDecodeQRCodeSuccess(String result) {
            actionAfterQR(result);
            pathFromAlbum = null;
        }

        @Override
        public void onDecodeQRCodeFailure() {
            onFailed();
            pathFromAlbum = null;
        }
    };

    private QRCodeView.Delegate delegate = new QRCodeView.Delegate() {
        @Override
        public void onScanQRCodeSuccess(String result) {

            Logger.i(result);

            //loadingView.show(getSupportFragmentManager(), "");
            actionAfterQR(result);
        }

        @Override
        public void onScanQRCodeOpenCameraError(Throwable throwable) {
            throwable.printStackTrace();
            // fix get permission in Xiaomi device under 6.0
            if (Build.MANUFACTURER.equals("Xiaomi")
                    && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                mQRCodeView.post(new Runnable() {
                    @Override
                    public void run() {
                        showNoPermissionLayout();
                        mQRCodeView.stopSpotAndHiddenRect();
                        mQRCodeView.stopCamera();
                    }
                });
            }
        }
    };

    /**
     * 二维码识别成功后的跳转行为
     *
     * @param result
     */
    private void actionAfterQR(String result) {
        if (result == null)
            result = "fuck____";
        EventBusInstance.getBusInstance().post(new EventToastInfo("扫描到的信息：\t" + result));
//        type = ScanMsgUtil.getType(result);
//        if (type == ScanMsgUtil.ScanType.SUBSCRIBE) {
//            mPresenter.getSubscribeInfo(ScanMsgUtil.getSubscribeID(result));
//        } else if (type == ScanMsgUtil.ScanType.PERSON) {
//            mPresenter.getSamplePersonInfo(ScanMsgUtil.getPersonInfo(result));
//        } else if (type == ScanMsgUtil.ScanType.CLASS) {
//            String[] info = ScanMsgUtil.getClassInfo(result);
//            assert info != null;
//            mPresenter.getClassInfo(info[0], info[1]);
//        } else if (type == ScanMsgUtil.ScanType.WEB) {
//            intent2WebPage(result);
//        } else {
//            intent2Others(result);
//        }
    }

    private ScanQRContract.Presenter mPresenter;

    private View mPermissionRequestLayout;

    private View mPermissionNoRequestLayout;

    private TitleBar.OnTitleClickListener onTitleClickListener = new TitleBar.OnTitleClickListener() {
        @Override
        public void onLeftClick() {
            finish();
        }

        @Override
        public void onRightClick() {
            if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                    && PermissionUtils.lacksPermissions(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE))) {
                EventBusInstance.getBusInstance().post(new EventToastInfo("打开相册"));
//                Routers.open(getContext(), IntentRouter.getActivityIndex(IntentRouter.ImageSelect));
//                overridePendingTransition(R.anim.in_from_right, R.anim.fade_out_to_left);
            } else {
                Toast.makeText(getContext(), "无法获取存储权限，请在应用设置处设置", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onTitleClick() {
        }
    };
//
//    @Subscribe
//    public void onEvent(IntentToPersonInfo info) {
//        pathFromAlbum = info.getPath();
//    }

    private void qrAlbum(String path) {
        //loadingView.show(getSupportFragmentManager(), "");
        int[] screen = DisplayUtils.getScreenParams(getContext());
        int width = screen[0];
        int height = screen[1];

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        float scale = (float) ((options.outWidth * 1.0 / width) < 1 ? 1
                : options.outWidth * 1.0 / width);
        float scale2 = (float) ((options.outHeight * 1.0 / height) < 1 ? 1
                : options.outHeight * 1.0 / height);
        scale = scale < scale2 ? scale2 : scale;//select a bigger scale number.
        options.outHeight = (int) (options.outHeight / scale);
        options.outWidth = (int) (options.outWidth / scale);
        options.inSampleSize = Math.round(scale);
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(path, options);
        QRCodeDecoder.decodeQRCode(bm, delegateAlbum);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_qr);

        StatusBarUtil.setColor(this, getResources().getColor(R.color.status_bar_color));

        ButterKnife.bind(this);

        dp30 = DisplayUtils.dip2px(getContext(), 30);

        mQRCodeView.setDelegate(delegate);

        titleBar.setOnTitleClickListener(onTitleClickListener);

        //loadingView = new CatLoadingView();
        //loadingView.setLoadingFinished(this);

        initialView();

        pathFromAlbum = null;
    }


    private void initialView() {
        // fix cinema windows
        fixCinemaWindows();
    }

    private void createView() {
        // create with request permission
        createPermissionRequestLayout();
        // create with no request and intent to setting
        createPermissionNoRequestLayout();
    }

    private void createPermissionRequestLayout() {
        mPermissionRequestLayout =
                LayoutInflater.from(getContext())
                        .inflate(R.layout.fragment_unable_cinema_permission, null);

        RxView.clicks(mPermissionRequestLayout.findViewById(R.id.permission_apply_button))
                .compose(RxPermissions.getInstance(this).ensure(Manifest.permission.CAMERA))
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showNoPermissionLayout();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        // agree
                        if (aBoolean) {
                            startCamera();
                        } else {
                            /**
                             * we should change initial flag at this point
                             * {@link ScanQRActivity#onResume()}
                             * the first if block
                             */
                            SpUtils.put(getContext(), FIRST_USE_CAMERA, false);
                            showNoPermissionLayout();
                        }
                    }
                });
    }

    /**
     * show first view
     */
    private void showFirstRequestLayout() {
        // always remove all views
        mPermissionLayout.removeAllViews();

        mPermissionLayout.addView(mPermissionRequestLayout);
    }

    private void showNoPermissionLayout() {
        // always remove all views
        mPermissionLayout.removeAllViews();

        mPermissionLayout.addView(mPermissionNoRequestLayout);
    }

    private void removeNoLayout() {
        mPermissionLayout.removeAllViews();
    }

    private void stopCamera() {
        mQRCodeView.stopCamera();
        mQRCodeView.stopSpotAndHiddenRect();
    }

    private void startCamera() {
        mQRCodeView.startCamera();
        mQRCodeView.startSpotAndShowRect();
    }

    private void createPermissionNoRequestLayout() {
        mPermissionNoRequestLayout =
                LayoutInflater.from(this)
                        .inflate(R.layout.fragment_permission_deteny_layout, null);

        Button intentToSetting = (Button) mPermissionNoRequestLayout.
                findViewById(R.id.permission_apply_button);

        intentToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
                startActivity(intent);
            }
        });
    }

    public int dp30;

    private void fixCinemaWindows() {
        // do it after ui create
        // make titleBar fixWindows only be used to fix after 4.4
        titleBar.post(new Runnable() {
            @Override
            public void run() {
                // only sdk >= 19 should use
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    // set top margin as the statusBarHeights
//                    ((FrameLayout.LayoutParams) titleBar.getLayoutParams())
//                            .topMargin = DisplayUtils.getStatusBarHeight(ScanQRActivity.this);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) titleBar.getLayoutParams();
                    int statusBarHeight = DisplayUtils.getStatusBarHeight(ScanQRActivity.this);
                    statusBarHeight = statusBarHeight <= 0 ? dp30 : statusBarHeight;
                    params.topMargin = statusBarHeight;
                    titleBar.setLayoutParams(params);
                }
                titleBar.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        createView();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopSpotAndHiddenRect();
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pathFromAlbum != null) {
            qrAlbum(pathFromAlbum);
        }

        boolean firstFlag = false;

        if (!SpUtils.contains(getContext(), FIRST_USE_CAMERA)) {
            // init first condition
            firstFlag = true;
        }


        boolean lack = PermissionUtils.lacksPermissions(
                getContext(), Manifest.permission.CAMERA);


        if (!lack) {
            startCamera();
            removeNoLayout();
            return;
        }

        stopCamera();

        // 首次开启没有权限
        if (firstFlag) {
            showFirstRequestLayout();
        } else {
            showNoPermissionLayout();
        }
    }

    @Override
    protected void onPause() {
        mQRCodeView.onDestroy();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setPresenter(ScanQRContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void intent2Others(String msg) {
        isClose = false;
        //loadingView.closeCatView(true, 2000);
        Toast.makeText(getContext(), msg + "\n\n" + getResources().getString(R.string.no_support_type), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailed() {
        isClose = false;
//        loadingView.closeCatView(true, 2000);
//        Toast.makeText(getContext(), getResources().getString(R.string.illegal_num), Toast.LENGTH_SHORT).show();
    }

    private void finish2NextPage() {
        finish();
        //overridePendingTransition(R.anim.in_from_right, R.anim.fade_out_to_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
