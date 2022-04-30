package com.aatesting.bugtracker.fragments.Main.GridFragment.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

public class GridFragmentArrowView extends View {
    //http://blogs.sitepointstatic.com/examples/tech/canvas-curves/bezier-curve.html

    private final int arrowColor = Color.argb(125, 150, 150, 150);
    private float dp;

    public GridFragmentArrowView(Context context, float xOff, float yOff){
        super(context);

        dp = context.getResources().getDisplayMetrics().density;

        this.setX(0 * dp);
        this.setY(0 * dp);
        this.setMinimumWidth(50000);
        this.setMinimumHeight(50000);
    }

    public GridFragmentArrowView(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint linePaint = linePaint();
        Paint arrowPaint = arrowPaint();

        float yOff = 10 * dp;

        //wid 90 * dp
        //89.818184 117.09091
        //gBoardView(120, 450); //bottom one
        //gBoardView(210, 90); //top one

        float xs = (120 + 45) * dp, xe = (210 + 45) * dp, xb = 200 * dp;
        float ys = (450) * dp - yOff, ye = (210) * dp + yOff, yb = 400 * dp;
        //define paths
        Path linePath;
        linePath = new Path();
        linePath.moveTo(xs, ys); //starting point
        linePath.cubicTo(xb, yb, xb, yb, xe, ye); //first 2 are the curves last one is ending pos

        float xScal = .75f, yScal = 1f;

        Path arrowPath = new Path(); {
            arrowPath.moveTo(30 * xScal, 120 * yScal);
            arrowPath.lineTo(90 * xScal, 60 * yScal);
            arrowPath.lineTo(150 * xScal, 120 * yScal);
            arrowPath.lineTo(30 * xScal, 120 * yScal);
        }

        //draw paths
        //canvas.drawPath(linePath, linePaint);
        canvas.drawPath(arrowPath, arrowPaint);
        //canvas.drawCircle(x3, y3, 10 * dp, arrowPaint);
        canvas.drawLine(xs, ys, xe, ye, linePaint);
        canvas.drawRect(120 * dp, 450 * dp, (120 * dp) + (89.818184f * dp), (450 * dp) + (117.09091f * dp), linePaint);
        canvas.drawRect(210 * dp, 90 * dp, (210 * dp) + (89.818184f * dp), (90 * dp) + (117.09091f * dp), linePaint);
    }


    private Paint linePaint(){
        Paint linePaint = new Paint();

        linePaint.setColor(arrowColor);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(25);

        return linePaint;
    }

    private Paint arrowPaint(){
        Paint arrowPaint = new Paint();

        arrowPaint.setColor(arrowColor);
        arrowPaint.setAntiAlias(true);
        arrowPaint.setStrokeCap(Paint.Cap.ROUND);
        arrowPaint.setStrokeJoin(Paint.Join.ROUND);
        arrowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //the smoothness of the edges, the bigger the smoother they are and the bigger the arrow is
        arrowPaint.setStrokeWidth(5);

        return arrowPaint;
    }
}
