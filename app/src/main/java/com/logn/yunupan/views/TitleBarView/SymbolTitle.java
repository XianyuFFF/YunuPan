package com.logn.yunupan.views.TitleBarView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.logn.yunupan.R;
import com.logn.yunupan.utils.ABColorUtil;
import com.logn.yunupan.utils.DisplayUtils;

/**
 * 带图标的标题
 *
 * @author liufengkai
 *         Created by liufengkai on 16/6/28.
 */
@SuppressLint("ClickableViewAccessibility")
public class SymbolTitle extends RelativeLayout {
    private String titleString;
    private int imageResId;
    private Context context;
    private TextView titleView;
    private ImageView imageView;

    private int titleResId;
    private int titleTextSize;
    private int imageWidth;
    private int titleColor;

    private int backGround = 0;
    private int pressedColor = 0;

    private LayoutParams rightParams;
    private LayoutParams leftParams;

    public SymbolTitle(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setClickable(true);

        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.SymbolTitle);


        this.titleString = typedArray.getString(R.styleable.SymbolTitle_titleText);
        this.imageResId = typedArray.getResourceId(R.styleable.SymbolTitle_rightImage,
                R.mipmap.icon_table_select_weeks);
        this.titleResId = typedArray.getResourceId(R.styleable.SymbolTitle_titleResId,
                8);
        this.titleTextSize = (int) typedArray.getDimension(R.styleable.SymbolTitle_titleTextSize,
                DisplayUtils.dip2px(context, 20));
        this.imageWidth = (int) typedArray.getDimension(R.styleable.SymbolTitle_rightImageWidth,
                DisplayUtils.dip2px(context, 20));

        this.titleColor = typedArray.getColor(R.styleable.SymbolTitle_titleColor,
                context.getResources().getColor(R.color.back_ground_gray_title_color));

        typedArray.recycle();
        initView();
    }


    private void initView() {
        titleView = new TextView(context);
        imageView = new ImageView(context);

        imageView.setImageResource(imageResId);
        titleView.setText(titleString);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        titleView.setId(titleResId);
        titleView.setTextColor(titleColor);

        leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParams.addRule(CENTER_VERTICAL);
        titleView.setLayoutParams(leftParams);

        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        rightParams.addRule(CENTER_VERTICAL);
        rightParams.addRule(RIGHT_OF, titleResId);
        rightParams.leftMargin = DisplayUtils.dip2px(context, 4);
        rightParams.width = imageWidth;
        rightParams.height = imageWidth;
        imageView.setLayoutParams(rightParams);

        // 不一定由背景 先下个断言
        assert getBackground() != null;
        int backGround = ((ColorDrawable) getBackground()).getColor();
        int pressedColor = ABColorUtil.colorBurn(backGround);
        // 动态生成selector的颜色
        this.setBackgroundDrawable(ABColorUtil.selectorDrawable(context, Color.TRANSPARENT, pressedColor));

        int dp8 = (int) context.getResources().getDimension(R.dimen.dp_8);

        this.setPadding(dp8, 0, 0, dp8);
        addView(titleView, leftParams);
        addView(imageView, rightParams);


    }

    public void setText(String text) {
        this.titleView.setText(text);
    }

    public void setTextSize(int size) {
        this.titleView.setTextSize(size);
    }

    public void addToLayout(RelativeLayout group, SymbolTitle title) {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        title.setLayoutParams(layoutParams);
        group.addView(title, layoutParams);
    }
}
