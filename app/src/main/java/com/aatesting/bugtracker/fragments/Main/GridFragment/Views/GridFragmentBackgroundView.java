package com.aatesting.bugtracker.fragments.Main.GridFragment.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * basically the grid on the background
 */

public class GridFragmentBackgroundView extends View {
    public float spacing = 30f;
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

        float spacing = 30f;

        //float yOff = topbar.getHeight();
        float yOff = 0;
        for (int x = 0; x < 100; x++){
            for (int y = 0; y < 100; y++){
                float xVal = x * spacing * dp;
                float yVal = y * spacing * dp + yOff;

                canvas.drawCircle(xVal, yVal, 2.5f, paint);
            }
        }
    }
}
