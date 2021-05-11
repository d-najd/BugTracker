package com.example.bugtracker.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.bugtracker.R;
import com.example.bugtracker.recyclerview.RecyclerAdapter;

public class BasicDialog {
    public void StartDialog(Context mcontext, String title, String description, String positiveButtonTxt){

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle(title)
                .setMessage(description)
                .setNegativeButton(positiveButtonTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.dark_gray);

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.YELLOW);
    }
}
