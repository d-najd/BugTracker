package com.aatesting.bugtracker.fragments.Main;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.Projects.ProjectsMainActivity;
import com.aatesting.bugtracker.fragments.FragmentSettings;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;

import org.jetbrains.annotations.NotNull;

public class GridFragment extends ModifiedFragment implements View.OnLongClickListener, View.OnDragListener {
    private View root;
    private GridView gridView;
    private ViewGroup layout;
    private DisplayMetrics metrics;
    private float dp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_grid, container, false);
        layout = (ViewGroup) root.findViewById(R.id.container);


        metrics = getContext().getResources().getDisplayMetrics();
        dp = metrics.density;

        ((ProjectsMainActivity)requireActivity()).thisFragment = this;
        ((ProjectsMainActivity)requireActivity()).listeners(root, FragmentSettings.GRID_FRAGMENT_ID, getParentFragmentManager());

        addGrid();


        gBoardView(120, 450);
        gBoardView(210, 90);
        //gEditTextView(90, 90);
        addLine();

        return root;
    }

    //region Views

    private void addLine(){
        ArrowTest arrowTest = new ArrowTest(root.getContext(), 90, 90);

        layout.addView(arrowTest);
    }

    /**
     * creates and sets boardView to the layout
     * @return the constraintLayout of the boardView
     */

    @NotNull
    private ConstraintLayout gBoardView(float xPos, float yPos) {
        //region Views

        ImageButton imgBtn = new ImageButton(root.getContext());
        imgBtn.setMinimumHeight((int) (90 * dp));
        imgBtn.setMinimumWidth((int) (90 * dp));
        imgBtn.setBackgroundColor(Color.argb(255, 40, 40, 40));
        imgBtn.setTag("ImgBtn1");
        imgBtn.setId(View.generateViewId());

        TextView textView = new TextView(root.getContext());
        textView.setText("fuck");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(5.5f * metrics.scaledDensity);
        textView.setTextColor(getResources().getColor(R.color.white87));
        textView.setId(View.generateViewId());
        //endregion

        //region Constraints

        ConstraintLayout conLayout = new ConstraintLayout(root.getContext());
        conLayout.setTag("ImgBtn1Layout");
        conLayout.addView(imgBtn);
        conLayout.addView(textView);
        imgBtn.setOnLongClickListener(this);

        ConstraintSet set = new ConstraintSet();
        set.clone(conLayout);
        set.connect(textView.getId(), ConstraintSet.TOP,
                imgBtn.getId(), ConstraintSet.BOTTOM, 0);
        set.connect(textView.getId(), ConstraintSet.START,
                imgBtn.getId(), ConstraintSet.END, 0);
        set.connect(textView.getId(), ConstraintSet.END,
                imgBtn.getId(), ConstraintSet.START, 0);
        set.applyTo(conLayout);

        textView.setPadding(0, (int) (7 * dp), 0, 0);
        //endregion

        conLayout.setX(xPos * dp);
        conLayout.setY(yPos * dp);

        ViewTreeObserver vto = conLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                conLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width  = layout.getMeasuredWidth();
                int height = layout.getMeasuredHeight();

            }
        });

        layout.addView(conLayout);
        return conLayout;
    }

    /**
     * creates and sets EdiTextView on the given coordinates
     * @return constraintLayout of the ediTextView
     */
    @NotNull
    private ConstraintLayout gEditTextView(float xPos, float yPos){
        EditText editText = new EditText(root.getContext());
        editText.setWidth((int) (300 * dp));
        editText.setMinHeight((int) (60 * dp));
        editText.setTextColor(getResources().getColor(R.color.white87));
        editText.setHintTextColor(getResources().getColor(R.color.white38));
        editText.setHint("Start typing...");
        editText.setTag("Edt1");
        editText.setTextColor(getResources().getColor(R.color.white));
        editText.setId(View.generateViewId());
        editText.setPadding((int) (10 * dp), (int) (10 * dp), (int) (10 * dp), (int) (10 * dp));
        editText.setBackgroundResource(android.R.color.transparent);

        ConstraintLayout conLayout = new ConstraintLayout(root.getContext());
        conLayout.setTag("Edt1Layout");
        conLayout.addView(editText);
        editText.setOnLongClickListener(this);
        conLayout.setBackgroundColor(Color.argb(150, 40, 40, 40));

        conLayout.setX(xPos * dp);
        conLayout.setY(yPos * dp);

        layout.addView(conLayout);
        return conLayout;
    }

    /**
     * adds the grid to the fragment
     */
    private void addGrid(){
        gridView = new GridView(root.getContext());
        gridView.setMinimumWidth(50000);
        gridView.setMinimumHeight(50000);
        layout.addView(gridView);

        ImageButton gridColliders = new ImageButton(root.getContext());
        gridColliders.setMinimumWidth(50000);
        gridColliders.setMinimumHeight(50000);
        gridColliders.setBackgroundColor(Color.parseColor("#00000000"));
        gridColliders.setTag("gridColliders");
        gridColliders.setOnDragListener(this);
        gridColliders.setPadding(150, 150, 0, 0);
        layout.addView(gridColliders);
    }

    //endregion

    /**
     * sets position of a given view
     */
    private void setPos(String tag, float x, float y){
        View tagView = root.findViewWithTag(tag + "Layout");

        //in short we are adjusting the position so it follows the dots on the screen
        float newX = Math.round((x - tagView.getWidth()/2f) / (gridView.spacing * dp)) * gridView.spacing * dp;
        float newY = Math.round((y - tagView.getHeight()/2f) / (gridView.spacing * dp)) * gridView.spacing * dp;

        tagView.setX(newX);
        tagView.setY(newY);
    }

    //region Listeners

    @Override
    public boolean onLongClick(View v) {
        View.DragShadowBuilder mShadow = new View.DragShadowBuilder(v);
        ClipData.Item item = new ClipData.Item(v.getTag().toString());
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);

        v.startDragAndDrop(data, mShadow, null, 0);
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event)
    {
        String clipData;
        View tagView;
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:

                ((ImageView) v).setColorFilter(Color.YELLOW);
                v.invalidate();

                clipData = event.getClipDescription().getLabel().toString();
                tagView = root.findViewWithTag(clipData + "Layout");
                tagView.setVisibility(View.INVISIBLE);

                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                ((ImageButton) v).setColorFilter(ContextCompat.getColor(root.getContext(), R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
                clipData = event.getClipDescription().getLabel().toString();
                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                //Log.wtf("dLoc", "event pos x: " + event.getX() + " y: " + event.getY());
                //Log.wtf("dLoc", "view pos x: " + v.getX() + " y: " + v.getY());

                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                ((ImageButton) v).clearColorFilter();
                ((ImageButton) v).setColorFilter(Color.YELLOW);

                v.invalidate();
                return true;

            case DragEvent.ACTION_DROP:
                clipData = event.getClipDescription().getLabel().toString();

                setPos(clipData, event.getX(), event.getY());
                v.invalidate();

                clipData = event.getClipDescription().getLabel().toString();
                tagView = root.findViewWithTag(clipData + "Layout");
                tagView.setVisibility(View.VISIBLE);
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                ((ImageButton) v).clearColorFilter();
                return true;
            default:
                return false;
        }
    }

    //endregion

    class ArrowTest extends View {
        //http://blogs.sitepointstatic.com/examples/tech/canvas-curves/bezier-curve.html

        private final int arrowColor = Color.argb(255, 150, 150, 150);

        public ArrowTest(Context context, float xOff, float yOff){
            super(context);

            this.setX(0 * dp);
            this.setY(0 * dp);
            this.setMinimumWidth(50000);
            this.setMinimumHeight(50000);
        }
        
        public ArrowTest(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Paint linePaint = linePaint();
            Paint arrowPaint = arrowPaint();

            //wid 90 * dp

            //gBoardView(120, 450); //bottom one
            //gBoardView(210, 90); //top one



            float x1 = (120 + 45) * dp, x2 = (210 + 45) * dp, x3 = 200 * dp;
            float y1 = (450) * dp, y2 = (210 + 45) * dp, y3 = 400 * dp;
            //define paths
            Path linePath;
            linePath = new Path();
            linePath.moveTo(x1, y1); //starting point
            linePath.cubicTo(x3, y3, x3, y3, x2, y2 - 90); //first 2 are the curves last one is ending pos




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
            canvas.drawLine((120 + 45) * dp, (450) * dp, (210 + 45) * dp, (90 + 90) * dp, linePaint);
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


    class GridView extends View {
        public float spacing = 30f;

        Paint paint = new Paint();

        public GridView(Context context) {
            super(context);
            paint.setColor(Color.parseColor("#606060"));
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
}
