package com.aatesting.bugtracker.fragments.Main.GridFragment.Views;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.fragments.Main.GridFragment.GridFragment;
import com.aatesting.bugtracker.fragments.Main.GridFragment.GridFragmentSettings;

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
        GridFragmentSettings.curId++;
        long curId = GridFragmentSettings.curId;
        Context context = gridFragment.requireContext();
        ViewGroup layout = gridFragment.viewGroup;
        float dp = GridFragment.dp;

        EditText editText = new EditText(context);
        editText.setMinWidth((int) (GridFragmentSettings.spacing * 10 * dp));
        editText.setMaxWidth((int) (GridFragmentSettings.spacing * 14 * dp));
        editText.setMinHeight((int) (GridFragmentSettings.spacing * 3 * dp));
        editText.setTextColor(getResources().getColor(R.color.white87));
        editText.setHintTextColor(getResources().getColor(R.color.white38));
        editText.setHint("Start typing...");
        editText.setTag(GridFragmentSettings.EDIT_TEXT_TAG + curId);
        editText.setTextColor(getResources().getColor(R.color.white));
        editText.setId(View.generateViewId());
        editText.setPadding((int) (10 * dp), (int) (10 * dp), (int) (10 * dp), (int) (10 * dp));
        editText.setBackgroundResource(android.R.color.transparent);

        ConstraintLayout conLayout = new ConstraintLayout(context);
        conLayout.setTag(GridFragmentSettings.EDIT_TEXT_TAG + curId + "Layout");
        GridFragmentSettings.allExistingViewTags.add(GridFragmentSettings.EDIT_TEXT_TAG + curId + "Layout");
        conLayout.addView(editText);
        editText.setOnLongClickListener(gridFragment.gridFragmentListeners);
        conLayout.setBackgroundColor(Color.argb(150, 40, 40, 40));

        conLayout.setX(xPos);
        conLayout.setY(yPos);

        layout.addView(conLayout);
    }
}
