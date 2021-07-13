package com.example.bugtracker.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.bugtracker.R;
import com.example.bugtracker.activities.ProjectCreateTable;

import java.util.ArrayList;

public class BasicDialogs {
    public static void BasicDialog(Context mcontext, String title, String description, String positiveButtonTxt){
        //this is regular dialog

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
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.darkGray);

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.YELLOW);
    }

    public static void BasicDialog(View v, Context mcontext, String title, String positiveButtonTxt, String negativeButtonTxt){
        //this is editext dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(mcontext).inflate(R.layout.dialog_edit_text, viewGroup, false);

        final String[] newColumnName = {null};

        EditText editText = dialogView.findViewById(R.id.editText);
        builder.setView(dialogView)
                .setTitle(title)
                .setNegativeButton(negativeButtonTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO add a way to get the data
                        Log.wtf("HELLOOOOO", "need to retrieve data");
                        newColumnName[0] = String.valueOf(editText.getText());
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.darkGray);

        alertDialog.show();
    }

    public static void EditTextDialog(Context mcontext, String title, String positiveButtonTxt, String negativeButtonTxt, String projectName, ProjectCreateTable projectCreateTableActivity, Intent intent){
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);

        final EditText editText = new EditText(mcontext);

        builder.setView(editText)
                .setTitle(title)
                .setNegativeButton(negativeButtonTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<String> titlesEmptyArr = new ArrayList<>();
                        ArrayList<Integer> imgsEmptyArr = new ArrayList<>();
                        String title = editText.getText().toString();

                        projectCreateTableActivity.SaveData(titlesEmptyArr, imgsEmptyArr, title, projectName);

                        //the data from the recyclerviews gets removed for some reason when editText
                        //is pressed, refreshing the activity is the only way that I can think of,
                        //notifying the adapters that the data is changed in any way doesn't work
                        projectCreateTableActivity.finish();
                        projectCreateTableActivity.overridePendingTransition(0, 0);
                        projectCreateTableActivity.startActivity(intent);
                        projectCreateTableActivity.overridePendingTransition(0, 0);
                    }

                });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.darkGray);

        alertDialog.show();
    }
}
