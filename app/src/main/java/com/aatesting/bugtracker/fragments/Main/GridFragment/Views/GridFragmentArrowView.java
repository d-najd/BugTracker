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

        float xs = (120 + 45) * dp, ys = (450) * dp - yOff;
        float xe = (210 + 45) * dp, ye = (210) * dp + yOff;
        float xb = 200 * dp,        yb = 400 * dp;

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

        double angle = calculateAngle(xs, xe, ys, ye) * Math.PI/180;
        Log.wtf("ERROR", angle + "");
        Point point = new Point(xs, ys);
        rotate_point(xe, ye, angle, point);

        canvas.drawCircle(xs, ys, 5 * dp, linePaint);
        canvas.drawCircle(xe, ye, 5 * dp, linePaint);

        canvas.drawCircle(point.x, point.y, 5 * dp, linePaint);

        //draw paths
        //canvas.drawPath(linePath, linePaint);
        //canvas.drawPath(arrowPath, arrowPaint);
        //canvas.drawCircle(x3, y3, 10 * dp, arrowPaint);
        //canvas.drawLine(xs, ys, xe, ye, linePaint);
        canvas.drawRect(120 * dp, 450 * dp, (120 * dp) + (89.818184f * dp), (450 * dp) + (117.09091f * dp), linePaint);
        canvas.drawRect(210 * dp, 90 * dp, (210 * dp) + (89.818184f * dp), (90 * dp) + (117.09091f * dp), linePaint);

    }

    static Point rotate_point(double cx, double cy, double angle, Point p)
    {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        // translate point back to origin:
        p.x -= cx;
        p.y -= cy;
        // rotate point
        double Xnew = p.x * c - p.y * s;
        double Ynew = p.x * s + p.y * c;
        // translate point back:
        p.x = (float) (Xnew + cx);
        p.y = (float) (Ynew + cy);
        return p;
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

        return Math.toDegrees(Math.atan2(deltaY, deltaX));
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

    /**
     * nothing else wants to work, I will go crazy if I spend another hour trying to figure out why it doesn't work
     */
    static class Point
    {
        public float x;
        public float y;

        public Point(float x, float y){
            this.x = x;
            this.y = y;
        }
    }
}
