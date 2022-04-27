package com.aatesting.bugtracker.fragments.Main;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_grid, container, false);
        layout = (ViewGroup) root.findViewById(R.id.container);


        metrics = getContext().getResources().getDisplayMetrics();

        ((ProjectsMainActivity)requireActivity()).thisFragment = this;
        ((ProjectsMainActivity)requireActivity()).listeners(root, FragmentSettings.GRID_FRAGMENT_ID, getParentFragmentManager());

        addGrid();

        ConstraintLayout boardView = gBoardView();
        ConstraintLayout editTextView = gEditTextView();

        layout.addView(boardView);
        layout.addView(editTextView);
        return root;
    }

    //region Views

    @NotNull
    private ConstraintLayout gBoardView() {
        //region Views

        ImageButton imgBtn = new ImageButton(root.getContext());
        imgBtn.setMinimumHeight((int) (90 * metrics.density));
        imgBtn.setMinimumWidth((int) (90 * metrics.density));
        imgBtn.setBackgroundColor(Color.argb(255, 40, 40, 40));
        imgBtn.setTag("ImgBtn1");
        imgBtn.setId(View.generateViewId());

        TextView textView = new TextView(root.getContext());
        textView.setText("I hate this");
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

        textView.setPadding(0, (int) (7 * metrics.density), 0, 0);
        //endregion

        conLayout.setX(120 * metrics.density);
        conLayout.setY(450 * metrics.density);

        return conLayout;
    }

    @NotNull
    private ConstraintLayout gEditTextView(){
        EditText editText = new EditText(root.getContext());
        editText.setWidth((int) (300 * metrics.density));
        editText.setMinHeight((int) (60 * metrics.density));
        editText.setTextColor(getResources().getColor(R.color.white87));
        editText.setHint("Start typing...");
        editText.setTag("Edt1");
        editText.setId(View.generateViewId());
        editText.setPadding((int) (10 * metrics.density), (int) (10 * metrics.density), (int) (10 * metrics.density), (int) (10 * metrics.density));
        editText.setBackgroundResource(android.R.color.transparent);

        ConstraintLayout conLayout = new ConstraintLayout(root.getContext());
        conLayout.setTag("Edt1Layout");
        conLayout.addView(editText);
        editText.setOnLongClickListener(this);
        conLayout.setBackgroundColor(Color.argb(150, 40, 40, 40));

        conLayout.setX(60 * metrics.density);
        conLayout.setY(60 * metrics.density);

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
        float newX = Math.round((x - tagView.getWidth()/2f) / (gridView.spacing * metrics.density)) * gridView.spacing * metrics.density;
        float newY = Math.round((y - tagView.getHeight()/2f) / (gridView.spacing * metrics.density)) * gridView.spacing * metrics.density;

        tagView.setX(newX);
        tagView.setY(newY);
    }

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
                    float xVal = x * spacing * metrics.density;
                    float yVal = y * spacing * metrics.density + yOff;

                    canvas.drawCircle(xVal, yVal, 2.5f, paint);
                }
            }
        }
    }
}
