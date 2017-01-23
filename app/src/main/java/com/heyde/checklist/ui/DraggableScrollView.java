package com.heyde.checklist.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Daniel on 1/22/2017.
 */

public class DraggableScrollView extends ScrollView {
    public DraggableScrollView(Context context) {
        super(context);
    }

    public DraggableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DraggableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DraggableScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (ev.getX() > this.getWidth()/9) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }


}
