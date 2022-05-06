package com.aatesting.bugtracker.fragments.Main.GridFragment.Views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.view.ViewGroup;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.fragments.Main.GridFragment.GridFragment;

public class GridFragmentArrowView extends View {
    //http://blogs.sitepointstatic.com/examples/tech/canvas-curves/bezier-curve.html

    private final int color = Color.argb(255, 150, 150, 150);
    private final float dp;
    private ViewGroup viewGroup;
    private GridFragment gridFragment;

    private final float xStart;
    private final float yStart;
    private final float xEnd;
    private final float yEnd;

    //the smaller of the *start and *end values (Math.min(*start, *end) + edgesPadding * dp)
    private final float xMinM;
    private final float yMinM;

    //edges padding, so that the arrow displays fully and for easier grabbing
    private final float edgesPadding;

    /**
     * sets up a line between 2 views or just flying around
     * <pre>
     *    __        __
     *   |  | ---> |  |
     *   |__|      |__|
     * </pre>
     */
    /*
        TODO make it so the arrow follows views when moved around and ability to curve
     */
    public GridFragmentArrowView(GridFragment gridFragment, ViewGroup viewGroup, float xStart, float yStart, float xEnd, float yEnd){
        super(gridFragment.getContext());

        dp = gridFragment.getContext().getResources().getDisplayMetrics().density;
        this.viewGroup = viewGroup;
        this.gridFragment = gridFragment;

        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;

        edgesPadding = 24 * dp;

        xMinM = Math.min(xStart, xEnd) - edgesPadding;
        yMinM = Math.min(yStart, yEnd) - edgesPadding;

        this.setX(xMinM);
        this.setY(yMinM);
        //gets multiplied by 2 to compensate for dividing in xMin
        this.setMinimumWidth((int) (Math.max(xStart, xEnd) - Math.min(xStart, xEnd) + edgesPadding*2));
        this.setMinimumHeight((int) (Math.max(yStart, yEnd) - Math.min(yStart, yEnd) + edgesPadding*2));
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLine(canvas);
        drawArrow(canvas);
        setCollision();
    }


    /**
     * sets collision, one collider on the arrow head, one on back and 1 for body
     *
     * <pre>
     *     1   2   3
     *     _ _____ __
     *    | |     |  |
     *     <----------
     *
     *   1 is head, 2 is body, 3 is back
     * </pre>
     */

    private void setCollision(){
        View headCollider = new View(getContext());
        View backCollider = new View(getContext());
        View bodyCollider = new View(getContext());

        double angle = calculateAngle(xStart, yStart, xEnd, yEnd);

        float size = 52 * dp;
        double width = getBodyColliderWidth(angle);

        headCollider.setMinimumWidth((int) size);
        headCollider.setMinimumHeight((int) size);
        backCollider.setMinimumWidth((int) size);
        backCollider.setMinimumHeight((int) size);
        bodyCollider.setMinimumWidth((int) size);
        bodyCollider.setMinimumHeight((int) width);

        headCollider.setX(xEnd - size/2);
        headCollider.setY(yEnd - size/2);
        headCollider.setBackgroundColor(R.color.red);
        headCollider.setRotation((float) (angle + 180));

        backCollider.setX(xStart - size/2);
        backCollider.setY(yStart - size/2);
        backCollider.setBackgroundColor(R.color.red);
        backCollider.setRotation((float) (angle + 180));


        bodyCollider.setX(xStart - size/2);
        bodyCollider.setY(yStart - size/2);
        bodyCollider.setPivotY(size/2);
        bodyCollider.setPivotX(size/2);

        bodyCollider.setBackgroundColor(R.color.red);
        bodyCollider.setRotation((float) angle + 90);

        viewGroup.addView(bodyCollider);
        viewGroup.addView(headCollider);
        viewGroup.addView(backCollider);

        headCollider.setOnLongClickListener(gridFragment.gridFragmentListeners);
        headCollider.setTag("arrowView");
    }

    /**
     * calculates the width that the arrowBody has, the reason why this is used is because the width
     * seems to be different from the biggest position value, thus needing to use this cursed code,
     * first, we get value between, 0-90, we don't get a value below that because it will screw up
     * how the closeness is calculated.
     *
     * the closer that we are to 45 the smaller the value is, ranging from 0-45
     * @param angle without angle how would we calculate closeness to angle?
     * @return the width of the arrow view's body collider
     * @see #setCollision()
     */
    private double getBodyColliderWidth(double angle){
        double distFrom45d = angle % 90;

        if (distFrom45d >= 45){
            distFrom45d = distFrom45d - 45;
        } else
            distFrom45d = 45 - distFrom45d;

        distFrom45d = distFrom45d * 2.222;

        final float smallestVal = Math.min(Math.min(xStart, xEnd), Math.min(yStart, yEnd));

        /*TODO                 READ
                                |
                                |
                                |
                               \_/

         TODO I think this can work but I think the max value has to be used and a percentage
            taken from it depending on the closeness from 45 degree
         */

        float test = (Math.max(Math.max(xStart, xEnd), Math.max(yStart, yEnd)));

        double secondVal = test * (distFrom45d / 100) + test / 2 * ((100 - distFrom45d) / 100 - 0.06);

        return test - secondVal;
    }

    private void drawLine(Canvas canvas){
        Paint linePaint = linePaint();

        /*
        Path linePath = new Path();
        linePath.moveTo(xs, ys); //starting point
        linePath.cubicTo(xb, yb, xb, yb, xe, ye); //first 2 are the curves last one is ending pos
        canvas.drawPath(linePath, linePaint);

         */

        //canvas.drawLine(xStart, yStart, xEnd, yEnd, linePaint);
        canvas.drawLine(xStart - xMinM, yStart - yMinM, xEnd - xMinM, yEnd - yMinM, linePaint);
    }

    private void drawArrow(Canvas canvas){
        Paint arrowPaint = arrowPaint();

        float[][] arrowPaths = getArrowPaths();
        Path arrowPath = new Path(); {
            arrowPath.moveTo(arrowPaths[0][0] - xMinM, arrowPaths[0][1] - yMinM);
            arrowPath.lineTo(arrowPaths[1][0] - xMinM, arrowPaths[1][1] - yMinM);
            arrowPath.lineTo(arrowPaths[2][0] - xMinM, arrowPaths[2][1] - yMinM);
            arrowPath.lineTo(arrowPaths[0][0] - xMinM, arrowPaths[0][1] - yMinM);
        }

        canvas.drawPath(arrowPath, arrowPaint);
    }

    /**
     * @apiNote they are rotated along the arrow line so that the arrow follows its rotation
     * @return gets the arrow head point locations
     */
    private float[][] getArrowPaths(){
        float xScaling = .6f, yScaling = .7f;
        float spacing = GridFragmentBackgroundView.spacing;

        float[][] arrowPaths = {
                {-1 * spacing * dp * xScaling + xEnd , .5f * spacing * dp * yScaling + yEnd}, //left
                {0 * spacing * dp * xScaling + xEnd , -.5f * spacing * dp * yScaling + yEnd}, //top
                {1 * spacing * dp * xScaling + xEnd, .5f * spacing * dp * yScaling + yEnd} //right
        };

        double angle = calculateAngle(xStart, yStart, xEnd, yEnd);

        for (int i = 0; i < arrowPaths.length; i++){
            Point rotatedPoint = rotate_point(xEnd, yEnd, (angle - 90) * Math.PI/180, new Point(arrowPaths[i][0], arrowPaths[i][1]));
            arrowPaths[i][0] = rotatedPoint.x;
            arrowPaths[i][1] = rotatedPoint.y;
        }

        return arrowPaths;
    }

    /**
     * rotates a point along a given axis axis and an angle
     *
     * <pre>
     *     ___________________________
     *     |
     *     |      (pointEnd)
     *     |          |
     *     |          |  angle (90 in this case)
     *     |          |
     *     |        cx,cy-------(pointStart)
     *     |
     * </pre>
     * @param cx the x position of where we want the point to be rotated around
     * @param cy the y position of where we want the point to be rotated around
     * @param angle the angle that we want to rotate
     * @param p the starting point
     * @return rotated {@link GridFragmentArrowView.Point} object
     * @see #calculateAngle(float, float, float, float) 
     */
    public static Point rotate_point(double cx, double cy, double angle, Point p)
    {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        // translate point back to origin:
        p.x -= cx;
        p.y -= cy;
        // rotate point
        double xNew = p.x * c - p.y * s;
        double yNew = p.x * s + p.y * c;
        // translate point back:
        p.x = (float) (xNew + cx);
        p.y = (float) (yNew + cy);
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
     * @see #rotate_point(double, double, double, Point)
     */

    public static double calculateAngle(float xStart, float yStart, float xEnd, float yEnd){
        float deltaX = xStart - xEnd;
        float deltaY = yStart - yEnd;

        double result = Math.toDegrees(Math.atan2(deltaY, deltaX));
        return (result < 0) ? (360d + result) : result;
    }

    private Paint linePaint(){
        Paint linePaint = new Paint();
        linePaint.setColor(color);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(20);

        return linePaint;
    }

    private Paint arrowPaint(){
        Paint arrowPaint = new Paint();
        arrowPaint.setColor(color);
        arrowPaint.setAntiAlias(true);
        arrowPaint.setStrokeCap(Paint.Cap.ROUND);
        arrowPaint.setStrokeJoin(Paint.Join.ROUND);
        arrowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //the smoothness of the edges, the bigger the smoother they are and the bigger the arrow is
        arrowPaint.setStrokeWidth(3);

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
