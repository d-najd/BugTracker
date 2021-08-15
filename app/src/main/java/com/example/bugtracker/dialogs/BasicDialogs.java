package com.example.bugtracker.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.bugtracker.ProjectCreateTableData;
import com.example.bugtracker.R;
import com.example.bugtracker.activities.ProjectCreateTable;

import java.util.ArrayList;

public class BasicDialogs {
    public static void BasicDialog(Context mcontext, String title, String description, String positiveButtonTxt){
        //this is regular dialog

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder
                .setTitle( Html.fromHtml("<font color='#FFFFFF'>"  + title + "</font>"))
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
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mcontext.getColor(R.color.purple_200));
    }

    //TODO optimize these so that it checks the creation of the dialog doesnt need to be repeated
    // 100 times, make a switch and pass the data from onswitch and check... and put the other stuff
    // inside a function

    //for adding new column
    public static void EditTextDialog(Context mcontext, String title, String positiveButtonTxt, String negativeButtonTxt, String projectName, ProjectCreateTable projectCreateTableActivity, Intent intent){
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);

        final EditText editText = new EditText(mcontext);
        editText.setTextColor(mcontext.getColor(R.color.white87));
        editText.setHintTextColor(mcontext.getColor(R.color.white60));

        builder.setView(editText)
                .setTitle( Html.fromHtml("<font color='#FFFFFF'>"  + title + "</font>"))
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
                        ArrayList<String> descriptionsEmptyArr = new ArrayList<>();

                        //titlesEmptyArr.add("HOHOHO");
                        //imgsEmptyArr.add(R.drawable.ic_account_24dp);
                        String title = editText.getText().toString();

                        ProjectCreateTableData.SaveNewColumn(titlesEmptyArr, imgsEmptyArr, descriptionsEmptyArr, title, projectName, mcontext);

                        //TODO seems like refreshing the activity doesnt solve all problems,
                        // if the first element is empty while adding new element it sets the data
                        // to the first instead of last or some other problems

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

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mcontext.getColor(R.color.purple_200));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mcontext.getColor(R.color.purple_200));
    }

    //TODO refreshing the data to fix problems doesnt seem to work out, need to fidn other way to
    // refresh the activity

    //for adding new item inside the column
    public static void EditTextDialog(Context mcontext, String title, String positiveButtonTxt, String negativeButtonTxt, int position, String projectName, ProjectCreateTable projectCreateTableActivity, Intent intent){
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);

        final EditText editText = new EditText(mcontext);
        editText.setTextColor(mcontext.getColor(R.color.white87));
        editText.setHintTextColor(mcontext.getColor(R.color.white60));

        builder.setView(editText)
                .setTitle( Html.fromHtml("<font color='#FFFFFF'>"  + title + "</font>"))
                .setNegativeButton(negativeButtonTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = editText.getText().toString();

                        ProjectCreateTableData.SaveNewItem(title, projectName, position, true, mcontext);

                        projectCreateTableActivity.finish();
                        projectCreateTableActivity.overridePendingTransition(0, 0);
                        projectCreateTableActivity.startActivity(intent);
                        projectCreateTableActivity.overridePendingTransition(0, 0);
                    }

                });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.darkGray);

        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mcontext.getColor(R.color.purple_200));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mcontext.getColor(R.color.purple_200));
    }
}
