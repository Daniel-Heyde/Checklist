package com.heyde.checklist.ui;

import android.app.Activity;
import android.graphics.Point;
import android.view.View;

import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

/**
 * Created by Daniel on 2/15/2017.
 */

public class OffsetViewTarget implements Target {

    private int mOffsetX;
    private int mOffsetY;
    private View mView;

    public OffsetViewTarget(View view, int offsetX, int offsetY, Activity activity) {
        mOffsetX = offsetX;
        mOffsetY = offsetY;
        mView = view;
    }

    @Override
    public Point getPoint() {
        int[] location = new int[2];
        mView.getLocationInWindow(location);
        int x = location[0] + mView.getWidth() / 2 + mOffsetX;
        int y = location[1] + mView.getHeight() / 2 + mOffsetY;
        return new Point(x, y);
    }
}
