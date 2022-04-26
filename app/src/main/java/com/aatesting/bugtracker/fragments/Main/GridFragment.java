package com.aatesting.bugtracker.fragments.Main;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.Projects.ProjectsMainActivity;
import com.aatesting.bugtracker.fragments.FragmentSettings;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;

public class GridFragment extends ModifiedFragment implements View.OnTouchListener, View.OnDragListener {


    /*
        TODO fix this, seems like because the dots are child of the main constraint layout that they are
            not affected by the constraint layout of the horizontalScrollView
     */

    private View root;
    private GridView gridView;
    private ViewGroup layout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_grid, container, false);
        layout = (ViewGroup) root.findViewById(R.id.container);

        ((ProjectsMainActivity)requireActivity()).thisFragment = this;
        ((ProjectsMainActivity)requireActivity()).listeners(root, FragmentSettings.GRID_FRAGMENT_ID, getParentFragmentManager());

        addGrid();

        ImageButton test = new ImageButton(root.getContext());
        test.setMinimumHeight(250);
        test.setMinimumWidth(250);
        test.setBackgroundColor(Color.RED);
        test.setTag("ImgBtn1");

        TextView textView = new TextView(root.getContext());
        textView.setText("I hate this");
        textView.setPadding(0, 300, 0, 0);

        ConstraintLayout testCon = new ConstraintLayout(root.getContext());
        testCon.setTag("ImgBtn1Layout");
        testCon.addView(test);
        testCon.addView(textView);
        test.setOnTouchListener(this);

        layout.addView(testCon);
        return root;
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

    /**
     * sets position of a given view
     */
    private void setPos(String tag, float x, float y){
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        View tagView = root.findViewWithTag(tag + "Layout");

        //in short we are adjusting the position so it follows the dots on the screen
        float newX = Math.round((x - tagView.getWidth()/2f) / (gridView.spacing * metrics.density)) * gridView.spacing * metrics.density;
        float newY = Math.round((y - tagView.getHeight()/2f) / (gridView.spacing * metrics.density)) * gridView.spacing * metrics.density;

        tagView.setX(newX);
        tagView.setY(newY);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
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
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:

                ((ImageView) v).setColorFilter(Color.YELLOW);

                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                ((ImageButton) v).setColorFilter(ContextCompat.getColor(root.getContext(), R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
                String clipData = event.getClipDescription().getLabel().toString();
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
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                ((ImageButton) v).clearColorFilter();
                return true;
            default:
                return false;
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
            View topbar = root.findViewById(R.id.topBar);

            float spacing = 30f;

            //float yOff = topbar.getHeight();
            float yOff = 0;
            for (int x = 0; x < 30; x++){
                for (int y = 0; y < 30; y++){
                    float xVal = x * spacing * metrics.density;
                    float yVal = y * spacing * metrics.density + yOff;

                    canvas.drawCircle(xVal, yVal, 2.5f, paint);
                }
            }
        }
    }
}
