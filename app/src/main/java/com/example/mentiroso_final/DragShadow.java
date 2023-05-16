package com.example.mentiroso_final;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

public class DragShadow extends View.DragShadowBuilder {
    ColorDrawable greyBox;

    public DragShadow(View view) {
        super(view);
        greyBox = new ColorDrawable(Color.LTGRAY);
    }

    @Override
    public void onDrawShadow(Canvas canvas) {
        getView().draw(canvas);
    }

    @Override
    public void onProvideShadowMetrics(Point shadowSize,
                                       Point shadowTouchPoint) {
        View v = getView();

        int height = v.getHeight();
        int width = v.getWidth();

        greyBox.setBounds(0, 0, width, height);
        shadowSize.set(width, height);
        shadowTouchPoint.set(width / 2, height / 2);
    }
}