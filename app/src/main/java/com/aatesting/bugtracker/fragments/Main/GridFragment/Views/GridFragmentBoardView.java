package com.aatesting.bugtracker.fragments.Main.GridFragment.Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.fragments.Main.GridFragment.GridFragment;
import com.aatesting.bugtracker.modifiedClasses.GridFragmentCustomConstraintLayout;
import com.aatesting.bugtracker.fragments.Main.GridFragment.GridFragmentSettings;
import com.google.android.material.imageview.ShapeableImageView;

import org.jetbrains.annotations.NotNull;

public class GridFragmentBoardView extends GridFragmentCustomConstraintLayout {
    final String BOARD_TAG = GridFragmentSettings.BOARD_TAG;
    float dp;

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
        dp = GridFragment.dp;

        ShapeableImageView imgBtn = new ShapeableImageView(context);
        imgBtn.setMinimumHeight((int) (GridFragmentSettings.spacing * 4 * dp));
        imgBtn.setMinimumWidth((int) (GridFragmentSettings.spacing * 4 * dp));
        imgBtn.setBackgroundColor(Color.argb(255, 40, 40, 40));
        imgBtn.setShapeAppearanceModel(imgBtn.getShapeAppearanceModel().toBuilder()
                .setAllCornerSizes(imgBtn.getMinimumWidth() * .175f).build()); //rounded edges

        imgBtn.setTag(BOARD_TAG + curId);
        imgBtn.setId(View.generateViewId());

        TextView textView = new TextView(context);
        textView.setText("boardView");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        textView.setWidth((int) (imgBtn.getMinimumWidth() * 1.2f));
        textView.setTextColor(getResources().getColor(R.color.white87));
        textView.setId(View.generateViewId());

        ConstraintLayout conLayout = new ConstraintLayout(context);
        conLayout.setTag(BOARD_TAG + curId + "Layout");
        GridFragmentSettings.allExistingViewTags.add(this);
        conLayout.addView(imgBtn);
        conLayout.addView(textView);
        //conLayout.setBackgroundColor(R.color.red);
        imgBtn.setOnTouchListener(gridFragment.gridFragmentListeners);

        ConstraintSet set = new ConstraintSet();
        set.clone(conLayout);
        set.connect(textView.getId(), ConstraintSet.TOP,
                imgBtn.getId(), ConstraintSet.BOTTOM, 0);
        set.connect(textView.getId(), ConstraintSet.START,
                imgBtn.getId(), ConstraintSet.END, 0);
        set.connect(textView.getId(), ConstraintSet.END,
                imgBtn.getId(), ConstraintSet.START, 0);
        set.applyTo(conLayout);

        textView.setPadding((int) (7 * dp), (int) (7 * dp), (int) (5 * dp), 0);

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

    /*
        TODO finish this, this is supposed to move along with the arrow if there is 1
     */

    @Override
    public void moveFully(float x, float y){
        try {
            //in short we are adjusting the position so it follows the dots on the screen
            float newX = Math.round((x - this.getWidth() / 2f) / (GridFragmentSettings.spacing * dp)) * GridFragmentSettings.spacing * dp;
            float newY = Math.round((y - this.getHeight() / 2f) / (GridFragmentSettings.spacing * dp)) * GridFragmentSettings.spacing * dp;

            this.setX(newX);
            this.setY(newY);
        } catch (Exception e){
            Message.defErrMessage(this.getContext());
            Log.wtf("ERROR", "Unable to get or set position for view with tag " + this.getTag());
        }
    }
}
