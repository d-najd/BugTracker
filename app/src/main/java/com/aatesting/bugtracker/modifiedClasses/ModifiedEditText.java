package com.aatesting.bugtracker.modifiedClasses;

import android.content.Context;
import android.view.DragEvent;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class ModifiedEditText extends androidx.appcompat.widget.AppCompatEditText {
    public ModifiedEditText(@NonNull Context context) {
        super(context);
    }

    /**
     * basically stops the the drag from the arrow or any other view to put the mime type text inside
     * the edit text instead of just stopping the drag and leaving the view like normal
     */
    @Override
    public boolean onDragEvent(DragEvent event) {
        return false;
    }
}
