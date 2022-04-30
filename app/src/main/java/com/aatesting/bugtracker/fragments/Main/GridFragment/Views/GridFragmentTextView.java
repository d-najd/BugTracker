package com.aatesting.bugtracker.fragments.Main.GridFragment.Views;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.fragments.Main.GridFragment.GridFragment;

import org.jetbrains.annotations.NotNull;

public class GridFragmentTextView extends ConstraintLayout {
    /**
     *
     * @param gridFragment creates and sets textView to the gridFragment viewGroup
     * @param xPos x offset
     * @param yPos y offset
     */
    public GridFragmentTextView(@NotNull GridFragment gridFragment, float xPos, float yPos){
        super(gridFragment.requireContext());
        Context context = gridFragment.requireContext();
        ViewGroup layout = gridFragment.layout;
        float dp = gridFragment.dp;

        EditText editText = new EditText(context);
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

        ConstraintLayout conLayout = new ConstraintLayout(context);
        conLayout.setTag("Edt1Layout");
        conLayout.addView(editText);
        editText.setOnLongClickListener(gridFragment.gridFragmentListeners);
        conLayout.setBackgroundColor(Color.argb(150, 40, 40, 40));

        conLayout.setX(xPos * dp);
        conLayout.setY(yPos * dp);

        layout.addView(conLayout);
    }
}
