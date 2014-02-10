package com.rise.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.base.L;
import com.rise.R;

/**
 * Created by kai.wang on 2/7/14.
 * <p/>
 * 下拉显示Header
 */
public class PullableListView extends LinearLayout {

    private float downY = 0.0f;
    private View headerView;
    private int headerViewHeight;
    private ListView listView;
    private boolean headerShowing = false;
    private int currentHeaderMargin = 0;

    public PullableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        headerView = LayoutInflater.from(context).inflate(R.layout.pull_more_list_view_header, null);
        addHeaderView(headerView);
    }

    private void addHeaderView(View headerView) {
        addView(headerView);
        measureView(headerView);
        headerViewHeight = headerView.getMeasuredHeight();
        LayoutParams p = (LayoutParams) headerView.getLayoutParams();
        p.topMargin = -headerViewHeight;
        currentHeaderMargin = -headerViewHeight;
    }

    private void adjustHeaderView(int margin) {
        LayoutParams p = (LayoutParams) headerView.getLayoutParams();
        p.topMargin = margin;
        headerView.setLayoutParams(p);
        currentHeaderMargin = margin;
        L.i("currentHeaderMargin:" + currentHeaderMargin);
        if (margin == -headerViewHeight) {
            headerShowing = false;
        } else {
            headerShowing = true;
        }
    }

    // 估算headview的width和height
    private void measureView(View headerView) {
        LayoutParams p = (LayoutParams) headerView.getLayoutParams();
        if (p == null) {
            p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        headerView.setLayoutParams(p);
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        headerView.measure(childWidthSpec, childHeightSpec);
    }

    public ListView getListView() {
        return listView;
    }

    public void createListView(ListView listView) {
        this.listView = listView;
        addView(listView);
    }

    private void applyHeaderPadding(MotionEvent ev) {
        float movingY = ev.getRawY();
        int marginTop = (int) ((movingY - downY) / 2);
//        L.i("headerViewHeight:" + headerViewHeight);
//        L.i("movingY:" + movingY);
//        L.i("downY:" + downY);
//        L.i("marginTop:" + marginTop);
        // 上滑
        if (marginTop < 0) {
            L.i("上滑,marginTop:" + marginTop);
            int i = currentHeaderMargin + marginTop;
            if (i < -headerViewHeight) {
                adjustHeaderView(-headerViewHeight);
            } else {
                // 露出一小部分时设置为全部隐藏
                if (i + headerViewHeight < 10 && i + headerViewHeight > 0) {
                    i = -headerViewHeight;
                }
                adjustHeaderView(i);
            }
        } else { // 下拉
            L.i("下拉,marginTop:" + marginTop);
            int i = currentHeaderMargin + marginTop;
            if (i > 0) {
                adjustHeaderView(0);
            } else {
                adjustHeaderView(i);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            downY = ev.getRawY();
        } else if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_UP) {
            if (listView.getFirstVisiblePosition() == 0) {
                // header正在显示或者没显示并且向下拉调整header
                if (headerShowing || downY - ev.getRawY() < 0) {
                    result = true;
                }
            } else {
                headerShowing = false;
            }
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_UP) {
            applyHeaderPadding(event);
            downY = event.getRawY();
        }
        return super.onTouchEvent(event);
    }
}
