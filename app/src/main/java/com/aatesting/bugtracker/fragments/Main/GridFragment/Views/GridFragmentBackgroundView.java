package com.aatesting.bugtracker.fragments.Main.GridFragment.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

import com.aatesting.bugtracker.fragments.Main.GridFragment.GridFragmentSettings;

/**
 * basically the grid on the background
 */

public class GridFragmentBackgroundView extends View {
    private Paint paint = new Paint();
    private float dp;

    public GridFragmentBackgroundView(Context context) {
        super(context);
        paint.setColor(Color.parseColor("#606060"));

        dp = context.getResources().getDisplayMetrics().density;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();

        //float yOff = topbar.getHeight();
        float yOff = 0;
        for (int x = 0; x < 1000; x++){
            for (int y = 0; y < 1000; y++){
                float xVal = x * GridFragmentSettings.spacing * dp;
                float yVal = y * GridFragmentSettings.spacing * dp + yOff;

                canvas.drawCircle(xVal, yVal, 2.5f, paint);
            }
        }
    }
}
