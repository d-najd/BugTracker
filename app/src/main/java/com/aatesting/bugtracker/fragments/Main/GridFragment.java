package com.aatesting.bugtracker.fragments.Main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.Projects.ProjectsMainActivity;
import com.aatesting.bugtracker.fragments.FragmentSettings;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;

public class GridFragment extends ModifiedFragment {


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
        layout.addView(drawView);

        return root;
    }

    class DrawView extends View {
        Paint paint = new Paint();

        public DrawView(Context context) {
            super(context);
            paint.setColor(Color.parseColor("#808080"));
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
            View topbar = root.findViewById(R.id.topBar);

            float yOff = topbar.getHeight();

            for (int x = 0; x < 30; x++){
                for (int y = 0; y < 30; y++){
                    float xVal = x * 40f * metrics.density + 40f;
                    float yVal = y * 40f * metrics.density + yOff + 40f;

                    canvas.drawCircle(xVal, yVal, 4, paint);
                }
            }
        }
    }

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
