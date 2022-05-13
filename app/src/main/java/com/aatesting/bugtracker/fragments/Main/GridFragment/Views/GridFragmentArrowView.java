package com.aatesting.bugtracker.fragments.Main.GridFragment.Views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.fragments.Main.GridFragment.GridFragment;
import com.aatesting.bugtracker.fragments.Main.GridFragment.GridFragmentSettings;
import com.aatesting.bugtracker.modifiedClasses.GridFragmentCustomConstraintLayout;

public class GridFragmentArrowView extends View {
    //http://blogs.sitepointstatic.com/examples/tech/canvas-curves/bezier-curve.html

    private final int color = Color.argb(255, 150, 150, 150);
    private final float dp;
    private ViewGroup viewGroup;
    private GridFragment gridFragment;
    private Canvas canvas;

    //these 6 should only be used when creating the view, since the arrow position can change with moving it n stuff but these don't
    private final float xStart;
    private final float yStart;
    private final float xEnd;
    private final float yEnd;

    //the smaller of the *start and *end values (Math.min(*start, *end) + edgesPadding * dp)
    private final float xMinM;
    private final float yMinM;

    //edges padding, so that the arrow displays fully and for easier grabbing
    private final float edgesPadding;

    private View headCollider = new View(getContext());
    private View backCollider = new View(getContext());
    private View bodyCollider = new View(getContext());
    private final float colliderSize;

    //the view that the head/back is following
    private GridFragmentCustomConstraintLayout headFollowView;
    private GridFragmentCustomConstraintLayout backFollowView;

    /**
     * sets a arrow which is following view/layout's
     * <pre>
     *    __        __
     *   |  | ---> |  |
     *   |__|      |__|
     * </pre>
     * @param gridFragment this view is made for the {@link GridFragment} specifically so we need one
     * @param xStart starting x position of the arrow
     * @param yStart starting y position of the arrow
     * @param xEnd ending x position of the arrow, the arrow head is located here
     * @param yEnd ending y position of the arrow, the arrow head is located here
     * @param backFollowView the constraint layout that the back of the arrow is following, if not null xStart and yStart will be overridden
     * @param headFollowView the constraint layout that the arrow head is following, if not null xEnd and yEnd will be overridden
     */
    public GridFragmentArrowView(GridFragment gridFragment, ViewGroup viewGroup,
                                 float xStart, float yStart, float xEnd, float yEnd,
                                 GridFragmentCustomConstraintLayout backFollowView,
                                 GridFragmentCustomConstraintLayout headFollowView){
        super(gridFragment.getContext());
        GridFragmentSettings.curId++;

        dp = gridFragment.getContext().getResources().getDisplayMetrics().density;
        colliderSize = 36 * dp;
        this.viewGroup = viewGroup;
        this.gridFragment = gridFragment;

        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;

        edgesPadding = 24 * dp;

        xMinM = Math.min(xStart, xEnd) - edgesPadding;
        yMinM = Math.min(yStart, yEnd) - edgesPadding;

        this.setTag(GridFragmentSettings.ARROW_VIEW_TAG + GridFragmentSettings.curId);
        GridFragmentSettings.allExistingArrowViews.add(this);
        this.setX(xMinM);
        this.setY(yMinM);
        //gets multiplied by 2 to compensate for dividing in xMin
        this.setMinimumWidth((int) (Math.max(xStart, xEnd) - Math.min(xStart, xEnd) + edgesPadding*2));
        this.setMinimumHeight((int) (Math.max(yStart, yEnd) - Math.min(yStart, yEnd) + edgesPadding*2));
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.canvas = canvas;

        drawLine();
        drawArrow();
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
        headCollider = new View(getContext());
        backCollider = new View(getContext());
        bodyCollider = new View(getContext());

        double angle = calculateAngle(xStart, yStart, xEnd, yEnd);

        headCollider.setMinimumWidth((int) colliderSize);
        headCollider.setMinimumHeight((int) colliderSize);
        headCollider.setElevation(GridFragmentSettings.ARROW_HEAD_LAYER);
        backCollider.setMinimumWidth((int) colliderSize);
        backCollider.setMinimumHeight((int) colliderSize);
        backCollider.setElevation(GridFragmentSettings.ARROW_BACK_LAYER);
        bodyCollider.setMinimumWidth((int) colliderSize);
        bodyCollider.setMinimumHeight((int) ((Math.sqrt(Math.pow(xStart - xEnd, 2) + Math.pow(yStart - yEnd, 2))) + colliderSize));
        bodyCollider.setElevation(GridFragmentSettings.ARROW_BODY_LAYER);

        headCollider.setX(xEnd - colliderSize/2);
        headCollider.setY(yEnd - colliderSize/2);
        headCollider.setRotation((float) (angle + 180));

        backCollider.setX(xStart - colliderSize/2);
        backCollider.setY(yStart - colliderSize/2);
        backCollider.setRotation((float) (angle + 180));

        bodyCollider.setX(xStart - colliderSize/2);
        bodyCollider.setY(yStart - colliderSize/2);
        bodyCollider.setPivotY(colliderSize/2);
        bodyCollider.setPivotX(colliderSize/2);
        bodyCollider.setRotation((float) angle + 90);

        viewGroup.addView(bodyCollider);
        viewGroup.addView(headCollider);
        viewGroup.addView(backCollider);

        headCollider.setTag(GridFragmentSettings.ARROW_HEAD_TAG + GridFragmentSettings.curId);
        bodyCollider.setTag(GridFragmentSettings.ARROW_BODY_TAG + GridFragmentSettings.curId);
        backCollider.setTag(GridFragmentSettings.ARROW_BACK_TAG + GridFragmentSettings.curId);

        headCollider.setOnTouchListener(gridFragment.gridFragmentListeners);
        bodyCollider.setOnTouchListener(gridFragment.gridFragmentListeners);
        backCollider.setOnTouchListener(gridFragment.gridFragmentListeners);

        boolean debug = false;
        if (debug){
            headCollider.setBackgroundColor(getResources().getColor(R.color.green));
            backCollider.setBackgroundColor(getResources().getColor(R.color.red));
            bodyCollider.setBackgroundColor(getResources().getColor(R.color.blue));
        }
    }

    private void drawLine(){
        Paint linePaint = linePaint();

        /*
        curved line code

        Path linePath = new Path();
        linePath.moveTo(xs, ys); //starting point
        linePath.cubicTo(xb, yb, xb, yb, xe, ye); //first 2 are the curves last one is ending pos
        canvas.drawPath(linePath, linePaint);
         */

        canvas.drawLine(xStart - xMinM, yStart - yMinM, xEnd - xMinM, yEnd - yMinM, linePaint);
    }

    private void drawArrow(){
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
        float xScaling = .45f, yScaling = .6f;
        float spacing = GridFragmentSettings.spacing;

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
     * rotates a point along a a given angle and a point of rotation
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
     * @param cx the x position of where we want the starting point to be rotated around
     * @param cy the y position of where we want the starting point to be rotated around
     * @param angle the number of degrees we want to rotate
     * @param p the starting point
     * @return rotated {@link GridFragmentArrowView.Point} object
     * @see #calculateAngle(float, float, float, float) 
     */
    public static Point rotate_point(double cx, double cy, double angle, Point p) {
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
        double deltaX = xStart - xEnd;
        double deltaY = yStart - yEnd;

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
        linePaint.setStrokeWidth(15);

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
     * moves the view and its colliders to a given position
     * @param xPos the x pos we want the view to be located at
     * @param yPos the y pos we want the view to be located at
     */
    public void moveFully(float xPos, float yPos){
        //new positions of the arrow
        float newX = Math.round((xPos - this.getWidth() / 2f) / (GridFragmentSettings.spacing * dp)) * GridFragmentSettings.spacing * dp;
        float newY = Math.round((yPos - this.getHeight() / 2f) / (GridFragmentSettings.spacing * dp)) * GridFragmentSettings.spacing * dp;

        //difference between the starting positions and new positions, for ex arrow starts at 100
        //and new is 150, difference is 50
        float difX = newX - this.getX();
        float difY = newY - this.getY();

        this.setX(this.getX() + difX);
        this.setY(this.getY() + difY);
        headCollider.setX(headCollider.getX() + difX);
        headCollider.setY(headCollider.getY() + difY);
        bodyCollider.setX(bodyCollider.getX() + difX);
        bodyCollider.setY(bodyCollider.getY() + difY);
        backCollider.setX(backCollider.getX() + difX);
        backCollider.setY(backCollider.getY() + difY);

        headFollowView.arrowsPointed.remove(headCollider);
        backFollowView.arrowsPointed.remove(backCollider);

        headFollowView = null;
        backFollowView = null;

        //movePart(1, 1, true);
    }

    /**
     * moves the head or back to a given position, for example
     *
     * <pre>
     *     |          /
     *     |   to    /
     *    \_/      \_/
     * </pre>
     *
     * and also sets a view to follow if its over one
     *
     * @param newXPos final x position of the arrow head/back
     * @param newYPos final y position of the arrow head/back
     * @param head if true the head gets moved, if false the back gets moved
     * @apiNote <p>moving the head also affects the position and rotation of the view and its colliders</p>
     * @implNote <h2>moving the head also removes and redraws the view and its colliders</h2>
     * @see #checkIfOverView(float, float, boolean)
     * @return the view created from the new positions
     */
    public View movePart(float newXPos, float newYPos, boolean head){
        float spacing = GridFragmentSettings.spacing;

        //newXPos = spacing * (8 + 1.5f) * dp;
        //newYPos = spacing * (15) * dp;
        float startX = backCollider.getX() + colliderSize/2;
        float startY = backCollider.getY() + colliderSize/2;
        float endX = headCollider.getX() + colliderSize/2;
        float endY = headCollider.getY() + colliderSize/2;

        ((ViewGroup) headCollider.getParent()).removeView(headCollider);
        ((ViewGroup) bodyCollider.getParent()).removeView(bodyCollider);
        ((ViewGroup) backCollider.getParent()).removeView(backCollider);
        ((ViewGroup) this.getParent()).removeView(this);

        checkIfOverView(newXPos, newYPos, head);

        GridFragmentArrowView newView;
        if (head) {
            newView = new GridFragmentArrowView(gridFragment, viewGroup,
                    startX, startY,
                    newXPos, newYPos, backFollowView, headFollowView);
        } else {
            newView = new GridFragmentArrowView(gridFragment, viewGroup,
                    newXPos, newYPos,
                    endX, endY, backFollowView, headFollowView);
        }

        viewGroup.addView(newView);
        return newView;
    }

    /**
     * checks if the current position is over some view and sets it to be followed n stuff if it does
     * @param head if true checks for head if false for the back
     * @return the the constraint layout of the view
     */
    private GridFragmentCustomConstraintLayout checkIfOverView(float xPos, float yPos, boolean head){
        String packageName = viewGroup.getContext().getPackageName();

        for (GridFragmentCustomConstraintLayout curView : GridFragmentSettings.allExistingViewTags) {
            if (    //basically checking if the arrow is in bounds of the current view
                    (xPos >= curView.getX() && xPos <= curView.getX() + curView.getWidth()) &&
                    (yPos >= curView.getY() && yPos <= curView.getY() + curView.getHeight())){
                if (head) {
                    curView.arrowsPointed.add(headCollider);
                    headFollowView = curView;
                }
                else {
                    curView.arrowsPointed.add(backCollider);
                    backFollowView = curView;
                }
                return curView;
            }
        }
        if (head && headFollowView != null){
            headFollowView.arrowsPointed.remove(headCollider);
        } else if (headFollowView != null){
            backFollowView.arrowsPointed.remove(backCollider);
        }
        return null;
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