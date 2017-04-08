package com.logn.yunupan.views.TitleBarView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.logn.yunupan.R;
import com.logn.yunupan.utils.ABColorUtil;
import com.logn.yunupan.utils.DisplayUtils;
import com.logn.yunupan.utils.GeneratedId;

@SuppressLint("ClickableViewAccessibility")
public class TitleBar extends RelativeLayout implements OnClickListener,
        OnTouchListener {

    private TitleBarButton leftButton;
    private TitleBarButton rightButton;
    private TextView titleTextView;

    private LayoutParams leftParam;
    private LayoutParams rightParam;
    private LayoutParams titleParam;

    private float leftWidth;
    private float rightWidth;

    private String titleText;
    private int titleColor;
    private float titleSize;
    private int leftBackground;
    private int rightBackground;

    private String leftText;
    private String rightText;

    private int leftTextColor;
    private int rightTextColor;

    private int leftTextColorPressed;
    private int rightTextColorPressed;

    private int bashLineColor;

    private int btPadding;


    private float leftButtonMargin, rightButtonMargin;

    private int leftBackPressed = 0;
    private int rightBackPressed = 0;

    private final static int DEFAULT_WIDTH = 48;
    private final static int DEFAULT_COLOR = Color.parseColor("#FFFFFF");
    private final static int DEFAULT_BASHLINE_COLOR = Color.parseColor("#cdcdcd");
    private final static int DEFAULT_TITLE_SIZE = 20;
    private final static int DEFAULT_PADDING = 20;
    private final static int DEFAULT_MARGIN = 4;

    private Context context;

    /**
     * 背景颜色
     */
    private int backGround;

    private boolean isCenterView = false;

    private OnTitleClickListener listener;

    public interface OnTitleClickListener {

        void onLeftClick();

        void onRightClick();

        void onTitleClick();
    }

    public TitleBar(Context context) {
        this(context, null);

    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(VERSION_CODES.HONEYCOMB)
    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;

        getAttrs(attrs);

        initView();

    }

    public void setOnTitleClickListener(OnTitleClickListener listener) {

        this.listener = listener;
    }

    public void setTitle(CharSequence charSequence) {
        titleTextView.setText(charSequence);
    }

    public void setTitleColor(int color) {
        titleColor = color;
        titleTextView.setTextColor(titleColor);
    }

    public void setLeftVisibility(int visible) {
        if (visible == View.INVISIBLE
                && leftButton.getVisibility() == View.VISIBLE) {
            AnimationSet set = new AnimationSet(true);
            AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
            ScaleAnimation scale = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f,
                    1, 0.5f, 1, 0.5f);
            anim.setDuration(200);
            scale.setDuration(200);
            set.setInterpolator(new DecelerateInterpolator());
            set.addAnimation(scale);
            set.addAnimation(anim);
            leftButton.setAnimation(set);
        }
        visible = visible == View.GONE ? View.INVISIBLE : visible;
        leftButton.setVisibility(visible);
    }

    public void setRightVisibility(int visible) {
        visible = visible == View.GONE ? View.INVISIBLE : visible;
        rightButton.setVisibility(visible);
    }

    public void setLeftPressedImage(int id) {
        this.leftBackPressed = id;
    }

    public void setRightPressedImage(int id) {
        this.rightBackPressed = id;
    }

    public void setLeftImageResource(int resource) {
        this.leftBackground = resource;
        leftButton.setImageResource(leftBackground);
    }

    public void setRightImageResource(int resource) {
        this.rightBackground = resource;
        rightButton.setImageResource(rightBackground);
    }

    public void setLeftText(CharSequence text) {
        leftButton.setText(text);
    }

    public void setRightText(CharSequence text) {
        rightButton.setText(text);
    }

    public void setRightTextColor(int color) {
        this.rightTextColor = color;
        rightButton.setTextColor(color);
    }

    public void setLeftTextColor(int color) {
        this.leftTextColor = color;
        leftButton.setTextColor(color);
    }

    public void setTextColor(int color) {
        this.leftTextColor = color;
        this.rightTextColor = color;
        rightButton.setTextColor(color);
        leftButton.setTextColor(color);
    }

    public void setPressedTextColor(int color) {
        this.leftTextColorPressed = color;
        this.rightTextColorPressed = color;
    }

    public void setBtPadding(int padding) {
        this.btPadding = (int) getPx(padding);
        leftButton.setPadding(btPadding, btPadding, btPadding, btPadding);
        rightButton.setPadding(btPadding, btPadding, btPadding, btPadding);
        titleTextView.setPadding(btPadding, 0, btPadding, 0);
    }

    /**
     * <font color='#444444'> <strong> ×Ô´øÍ¸Ã÷×´Ì¬À¸ÌØÐ§</strong> </font><br>
     * <br>
     *
     * @param activity
     */
    @TargetApi(VERSION_CODES.KITKAT)
    public void translucentStatus(Activity activity) {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusHeight = getStatusHeight(activity.getApplicationContext());
            if (statusHeight == -1) {
                return;
            }
            int preHeight = getLayoutParams().height;
            getLayoutParams().height = preHeight + statusHeight;
            setLayoutParams(getLayoutParams());
            setPadding(0, statusHeight, 0, 0);
        }
    }

    private int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    private void getAttrs(AttributeSet attrs) {

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.TitleBar);

        leftButtonMargin = ta.getDimension(R.styleable.TitleBar_leftButtonMargin,
                getPx(DEFAULT_MARGIN));

        rightButtonMargin = ta.getDimension(R.styleable.TitleBar_rightButtonMargin,
                getPx(DEFAULT_MARGIN));

        leftWidth = ta.getDimension(R.styleable.TitleBar_leftWidth,
                getPx(DEFAULT_WIDTH));
        leftBackground = ta.getResourceId(R.styleable.TitleBar_leftButtonBackground,
                0);
        leftTextColor = ta.getColor(R.styleable.TitleBar_leftTextColor,
                DEFAULT_COLOR);
        leftText = ta.getString(R.styleable.TitleBar_leftText);

        rightWidth = ta.getDimension(R.styleable.TitleBar_rightWidth,
                getPx(DEFAULT_WIDTH));
        rightBackground = ta.getResourceId(
                R.styleable.TitleBar_rightButtonBackground, 0);
        rightTextColor = ta.getColor(R.styleable.TitleBar_rightTextColor,
                DEFAULT_COLOR);
        rightText = ta.getString(R.styleable.TitleBar_rightText);
        titleText = ta.getString(R.styleable.TitleBar_titleBarText);
        titleColor = ta.getColor(R.styleable.TitleBar_titleBarColor,
                DEFAULT_COLOR);

        titleSize = ta.getDimension(R.styleable.TitleBar_titleSize,
                getPx(DEFAULT_TITLE_SIZE));

        btPadding = (int) ta.getDimension(R.styleable.TitleBar_titleBarButtonPadding,
                DEFAULT_PADDING);

        backGround = ((ColorDrawable) getBackground()).getColor();

        leftBackPressed = ta.getColor(R.styleable.TitleBar_leftButtonPressed,
                ABColorUtil.colorBurn(backGround));

        rightBackPressed = ta.getColor(R.styleable.TitleBar_rightButtonPressed,
                ABColorUtil.colorBurn(backGround));
        bashLineColor = ta.getColor(R.styleable.TitleBar_bashLineColor, DEFAULT_BASHLINE_COLOR);

        ta.recycle();
    }

    @SuppressLint("NewApi")
    @TargetApi(VERSION_CODES.JELLY_BEAN)
    private void initView() {
        RelativeLayout innerLayout = new RelativeLayout(context);
        LayoutParams innerLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) getPx(48)
        );
        addView(innerLayout, innerLayoutParams);

        leftButton = new TitleBarButton(context);
        rightButton = new TitleBarButton(context);
        titleTextView = new TextView(context);

        // 自动隐藏按钮
        if (leftBackground == 0
                && leftText == null) {
            hideLeftButton();
        }

        if (rightBackground == 0
                && rightText == null) {
            hideRightButton();
        }

        leftButton.setText(leftText);
        leftButton.setTextColor(leftTextColor);
        leftButton.setImageResource(leftBackground);
        leftButton.setTextSize(getPx(14));
        leftButton.setOnClickListener(this);
        leftButton.setOnTouchListener(this);
        leftButton.setGravity(Gravity.CENTER);
        leftButton.getImageView().setScaleType(ImageView.ScaleType.FIT_CENTER);
        leftButton.setPadding(btPadding, btPadding, btPadding, btPadding);

        rightButton.setText(rightText);
        rightButton.setTextColor(rightTextColor);
        rightButton.setGravity(Gravity.CENTER);
        rightButton.setImageResource(rightBackground);
        rightButton.setOnClickListener(this);
        rightButton.setTextSize(getPx(14));
        rightButton.setOnTouchListener(this);
        rightButton.getImageView().setScaleType(ImageView.ScaleType.FIT_CENTER);
        rightButton.setPadding(btPadding, btPadding, btPadding, btPadding);

        titleTextView.setText(titleText);
        titleTextView.setSingleLine(true);
        titleTextView.setEllipsize(TextUtils.TruncateAt.END);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        titleTextView.setTextColor(titleColor);
        titleTextView.setGravity(Gravity.CENTER);
        titleTextView.setOnClickListener(this);
        titleTextView.setPadding(btPadding, 0, btPadding, 0);
        int width = DisplayUtils.getScreenParams(getContext())[0];
        titleTextView.setMaxWidth(width - DisplayUtils.dp2px(getContext(), 128));

        leftParam = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        if (leftText == null) {
            leftParam.width = (int) leftWidth;
            leftParam.height = (int) leftWidth;
        }

        leftParam.addRule(RelativeLayout.CENTER_VERTICAL);
        leftParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        leftParam.leftMargin = (int) leftButtonMargin;

        rightParam = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        if (rightText == null) {
            rightParam.width = (int) rightWidth;
            rightParam.height = (int) rightWidth;
        }

        rightParam.addRule(RelativeLayout.CENTER_VERTICAL);
        rightParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rightParam.rightMargin = (int) rightButtonMargin;

        titleParam = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        titleParam.addRule(RelativeLayout.CENTER_VERTICAL);
        titleParam.addRule(RelativeLayout.CENTER_HORIZONTAL);


        innerLayout.addView(leftButton, leftParam);
        innerLayout.addView(titleTextView, titleParam);
        innerLayout.addView(rightButton, rightParam);

        int innerId = GeneratedId.generateViewId();
        innerLayout.setId(innerId);

        View bashLine = new View(context);
        bashLine.setBackgroundColor(bashLineColor);
        LayoutParams bash = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (getPx(1) / 2));
        bash.addRule(BELOW, innerId);
        bashLine.setLayoutParams(bash);

        addView(bashLine, bash);
    }

    private float getPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }

    private int px2sp(float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            if (v == leftButton) {
                listener.onLeftClick();
            }
            if (v == rightButton) {
                listener.onRightClick();
            }
            if (v == titleTextView || isCenterView) {
                listener.onTitleClick();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (v == leftButton) {
                leftButton.setBackgroundColor(leftBackPressed);
            } else if (v == rightButton) {
                rightButton.setBackgroundColor(rightBackPressed);
            }
            return false;
        }


        if (event.getAction() == MotionEvent.ACTION_UP) {
            leftButton.setTextColor(leftTextColor);
            rightButton.setTextColor(rightTextColor);

            leftButton.setBackgroundColor(Color.TRANSPARENT);
            leftButton.getImageView().setScaleType(ImageView.ScaleType.FIT_CENTER);

            rightButton.setBackgroundColor(Color.TRANSPARENT);
            rightButton.getImageView().setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        return false;
    }

    public void hideLeftButton() {
        leftButton.setVisibility(GONE);
    }

    public void hideRightButton() {
        rightButton.setVisibility(GONE);
    }

    public void showLeftButton() {
        leftButton.setVisibility(VISIBLE);
    }

    public void showRightButton() {
        rightButton.setVisibility(VISIBLE);
    }

    public void setCenterView(View view) {
        if (titleTextView != null) {
            removeView(titleTextView);
        }

        view.setVisibility(VISIBLE);
        removeView(view);
        isCenterView = true;

        titleParam = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        titleParam.addRule(RelativeLayout.CENTER_VERTICAL);
        titleParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        view.setLayoutParams(titleParam);
        addView(view, titleParam);
    }

    public void setRightWidth(float rightWidth) {
        this.rightWidth = rightWidth;
        rightParam.height = (int) rightWidth;
        rightParam.width = (int) rightWidth;
    }

    public void setRightPadding(int padding) {
        rightButton.setPadding(padding, padding, padding, padding);
    }


    public TitleBarButton getLeftButton() {
        return leftButton;
    }

    public TitleBarButton getRightButton() {
        return rightButton;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }
}
