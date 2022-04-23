package com.aatesting.bugtracker.fragments.Main;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_grid, container, false);

        ((ProjectsMainActivity)requireActivity()).thisFragment = this;
        ((ProjectsMainActivity)requireActivity()).listeners(root, FragmentSettings.GRID_FRAGMENT_ID, getParentFragmentManager());

        ViewGroup layout = (ViewGroup) root.findViewById(R.id.container);
        DrawView drawView = new DrawView(root.getContext());
        drawView.setMinimumWidth(50000);
        drawView.setMinimumHeight(50000);
        layout.addView(drawView);

        ImageView test = new ImageView(root.getContext());
        test.setMinimumHeight(500);
        test.setMinimumWidth(500);
        test.setBackgroundColor(Color.RED);
        test.setTag("test");
        test.setOnTouchListener(this);
        //test.setText("Testing");

        ImageView test1 = new ImageView(root.getContext());
        test1.setMinimumWidth(200);
        test1.setMinimumHeight(200);
        test1.setBackgroundColor(Color.YELLOW);
        test1.setTag("test1");
        test1.setOnDragListener(this);
        test1.setPadding(150, 150, 0, 0);
        layout.addView(test);
        layout.addView(test1);
        return root;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        View.DragShadowBuilder mShadow = new View.DragShadowBuilder(v);
        ClipData.Item item = new ClipData.Item(v.getTag().toString());
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);


        //  switch (v.getId())

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
                ((ImageView) v).setColorFilter(ContextCompat.getColor(root.getContext(), R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
                String clipData = event.getClipDescription().getLabel().toString();
                Log.wtf("clipdata on entered ", clipData.toString());
                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                ((ImageView) v).clearColorFilter();
                ((ImageView) v).setColorFilter(Color.YELLOW);

                v.invalidate();
                return true;

            case DragEvent.ACTION_DROP:
                clipData = event.getClipDescription().getLabel().toString();
                Log.wtf("clipdata on success ", clipData.toString());

                Toast.makeText(root.getContext(), "dropped at right coordinates", Toast.LENGTH_SHORT).show();

                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                ((ImageView) v).clearColorFilter();

                Toast.makeText(root.getContext(), "try dropping in the blackish cube", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    class DrawView extends View {
        Paint paint = new Paint();

        public DrawView(Context context) {
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
                    float xVal = x * spacing * metrics.density + spacing;
                    float yVal = y * spacing * metrics.density + yOff + spacing;

                    canvas.drawCircle(xVal, yVal, 2.5f, paint);
                }
            }
        }
    }

    private void Testing(){}

    /*

                <ImageView
        android:id="@+id/createImg"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginTop="12dp"
        android:layout_marginBottom="8dp"
        android:background="@drawable/transparent"
        android:src="@drawable/ic_add_24dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/weeksRecyclerView" />

     */
}
