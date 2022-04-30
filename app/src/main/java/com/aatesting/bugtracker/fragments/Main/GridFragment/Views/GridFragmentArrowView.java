package com.aatesting.bugtracker.fragments.Main.GridFragment.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;

public class GridFragmentArrowView extends View {
    //http://blogs.sitepointstatic.com/examples/tech/canvas-curves/bezier-curve.html

    private final int arrowColor = Color.argb(125, 150, 150, 150);
    private float dp;
    private float yOff;

    float xs = (120 + 45) * dp, ys = (450) * dp - yOff;
    float xe = (210 + 45) * dp, ye = (210) * dp + yOff;
    float xb = 200 * dp,        yb = 400 * dp;

    public GridFragmentArrowView(Context context, float xOff, float yOff){
        super(context);

        dp = context.getResources().getDisplayMetrics().density;
        yOff = 10 * dp;

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

        //89.818184 117.09091
        //gBoardView(120, 450); //bottom one
        //gBoardView(210, 90); //top one

        //define paths
        Path linePath = new Path();
        linePath.moveTo(xs, ys); //starting point
        linePath.cubicTo(xb, yb, xb, yb, xe, ye); //first 2 are the curves last one is ending pos

        float xScal = .75f, yScal = 1f;

        Path arrowPath = new Path(); {
            arrowPath.moveTo(30 * xScal, 120 * yScal);
            arrowPath.lineTo(90 * xScal, 60 * yScal);
            arrowPath.lineTo(150 * xScal, 120 * yScal);
            arrowPath.lineTo(30 * xScal, 120 * yScal);
        }

        double angle = calculateAngle(xs, xe, ys, ye);


        float x2e = 0 * 30 + 5 * 30;
        float y2e = 0 * 30 + 5 * 30;
        float x2s = 3 * 30 + 5 * 30;
        float y2s = 0 * 30 + 5 * 30;


        canvas.drawCircle(x2s * dp, y2s *dp, 5 * dp, linePaint);
        canvas.drawCircle(x2e *dp, y2e * dp, 5 * dp, linePaint);


        double sinX = Math.sin(90 * Math.PI /180);
        double cosX = Math.cos(90 * Math.PI / 180);


        Log.wtf("FUCK YOU", x2s + " " + y2s + " " + x2e + " " + y2e);
        x2s -= (x2s - x2e);
        y2s -= (y2s - y2e);
        Log.wtf("FUCK YOU", x2s + " " + y2s);


        float xnew = (float) (x2s * sinX - y2s * cosX);
        float ynew = (float) (x2s * sinX +  y2s * cosX);

        x2s = xnew + (x2e);
        y2s = ynew + y2e;

        canvas.drawCircle(x2s * dp, y2s * dp, 5 * dp, linePaint);





        //draw paths
        //canvas.drawPath(linePath, linePaint);
        //canvas.drawPath(arrowPath, arrowPaint);
        //canvas.drawCircle(x3, y3, 10 * dp, arrowPaint);
        //canvas.drawLine(xs, ys, xe, ye, linePaint);
        //canvas.drawRect(120 * dp, 450 * dp, (120 * dp) + (89.818184f * dp), (450 * dp) + (117.09091f * dp), linePaint);
        //canvas.drawRect(210 * dp, 90 * dp, (210 * dp) + (89.818184f * dp), (90 * dp) + (117.09091f * dp), linePaint);

    }

    /**
     * Work out the angle from the x horizontal winding anti-clockwise
     * in screen space.
     *
     * <pre>
     * x,y -------------
     *     |  1,1
     *     |    \
     *     |     \
     *     |     2,2
     * </pre>
     * @return - a double from 0 to 360
     */

    private double calculateAngle(float startX, float endX, float startY, float endY){
        float deltaX = startX - endX;
        float deltaY = startY - endY;
        double result = Math.toDegrees(Math.atan2(deltaY, deltaX));

        return result;
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
