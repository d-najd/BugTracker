package com.aatesting.bugtracker.fragments.Main.GridFragment.Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.fragments.Main.GridFragment.GridFragment;
import com.aatesting.bugtracker.fragments.Main.GridFragment.GridFragmentSettings;

import org.jetbrains.annotations.NotNull;

public class GridFragmentBoardView extends ConstraintLayout {
    final String BOARD_TAG = GridFragmentSettings.BOARD_TAG;

    /**
     * creates and sets boardView to the gridFragment viewGroup
     * @param gridFragment the grid fragment that we are adding boardView to
     * @param xPos X offset
     * @param yPos Y offset
     */
    public GridFragmentBoardView(@NotNull GridFragment gridFragment, float xPos, float yPos) {
        super(gridFragment.requireContext());
        GridFragmentSettings.curId++;
        long curId = GridFragmentSettings.curId;

        Context context = gridFragment.requireContext();
        ViewGroup layout = gridFragment.viewGroup;
        float dp = GridFragment.dp;

        ImageButton imgBtn = new ImageButton(context);
        imgBtn.setMinimumHeight((int) (GridFragmentSettings.spacing * 3 * dp));
        imgBtn.setMinimumWidth((int) (GridFragmentSettings.spacing * 3 * dp));
        imgBtn.setBackgroundColor(Color.argb(255, 40, 40, 40));
        imgBtn.setTag(BOARD_TAG + curId);
        imgBtn.setId(View.generateViewId());

        TextView textView = new TextView(context);
        textView.setText("1234567890");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(5.5f * dp);
        textView.setTextColor(getResources().getColor(R.color.white87));
        textView.setId(View.generateViewId());

        ConstraintLayout conLayout = new ConstraintLayout(context);
        conLayout.setTag(BOARD_TAG + curId + "Layout");
        conLayout.addView(imgBtn);
        conLayout.addView(textView);
        //conLayout.setBackgroundColor(R.color.red);
        imgBtn.setOnLongClickListener(gridFragment.gridFragmentListeners);

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

        conLayout.setX(xPos);
        conLayout.setY(yPos);

        ViewTreeObserver vto = conLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                conLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width  = conLayout.getMeasuredWidth();
                int height = conLayout.getMeasuredHeight();

                Log.wtf("Debug", width/dp + " " + height/dp);
            }
        });

        layout.addView(conLayout);
    }
}
