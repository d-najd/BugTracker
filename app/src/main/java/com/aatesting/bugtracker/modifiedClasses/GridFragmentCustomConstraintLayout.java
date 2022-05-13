package com.aatesting.bugtracker.modifiedClasses;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.aatesting.bugtracker.Message;

import java.util.ArrayList;

/**
 * this class should be used instead of constraint layout for all views
 */
public class GridFragmentCustomConstraintLayout extends ConstraintLayout {
    /**
     * list of all the arrow head/backs pointing at the current view, this is used for moving
     * arrow along with the view
     */
    public ArrayList<View> arrowsPointed = new ArrayList<>();

    public GridFragmentCustomConstraintLayout(@NonNull Context context) {
        super(context);
    }

    public GridFragmentCustomConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GridFragmentCustomConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void moveFully(float x, float y){
        Message.defErrMessage(getContext());
        Log.wtf("WARNING", "Override the moveFully method in " + this.getClass().getSimpleName());
    }
}
