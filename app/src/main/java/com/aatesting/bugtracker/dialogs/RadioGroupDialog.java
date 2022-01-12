package com.aatesting.bugtracker.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.fragments.SignInFragment;
import com.aatesting.bugtracker.recyclerview.Adapters.CreateProjectsAdapter;

import java.util.ArrayList;

public class RadioGroupDialog {

    @SuppressLint("RestrictedApi")
    public void StartDialog(View v, Context mcontext, CreateProjectsAdapter adapter, ArrayList<String> titles, int selected, int color, String title){
        Handler h = new Handler() ;
        AppCompatRadioButton radioButton;
        int textSize = 16;
        int padding = 35;

        ArrayList<AppCompatRadioButton> radioButtons = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_radiogroup, viewGroup, false);

        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
        builder.setView(dialogView)
                .setTitle( Html.fromHtml("<font color='#FFFFFF'>"  + title + "</font>"))
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.dialogColor);

        for (int i = 0; i < titles.size(); i++) {
            radioButton = new AppCompatRadioButton(mcontext);
            radioButtons.add(radioButton);
            //used for setting the color of the button
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{
                             Color.DKGRAY
                            ,color,
                    }
            );
            radioButton.setSupportButtonTintList(colorStateList);

            radioButton.setText(titles.get(i));
            if(radioButton.getParent() != null) {
                ((ViewGroup)radioButton.getParent()).removeView(radioButton);
            }
            radioGroup.addView(radioButton);
            radioButton.setPadding(padding, padding, padding, padding);
            int finalI = i;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    h.postDelayed(new Runnable() {
                        public void run() {
                            alertDialog.cancel();
                            adapter.RadioButtonUpdateText(finalI, title, titles.get(finalI));
                        }
                    }, 350);
                }
            });
            radioButton.setTextSize(textSize);
        }

        if (selected != -1)
            CheckIfCheckingNeeded(mcontext, adapter, title, radioButtons, selected);

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.YELLOW);

    }

    //region forDemo
    public void StartDialog(View v, Context mcontext, SignInFragment adapter, ArrayList<String> titles, int selected, int color, String title){
        Handler h = new Handler() ;
        AppCompatRadioButton radioButton;
        int textSize = 16;
        int padding = 35;

        ArrayList<AppCompatRadioButton> radioButtons = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_radiogroup, viewGroup, false);

        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
        builder.setView(dialogView)
                .setTitle( Html.fromHtml("<font color='#FFFFFF'>"  + title + "</font>"))
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.dialogColor);

        for (int i = 0; i < titles.size(); i++) {
            radioButton = new AppCompatRadioButton(mcontext);
            radioButtons.add(radioButton);
            //used for setting the color of the button
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{
                            Color.DKGRAY
                            ,color,
                    }
            );
            radioButton.setSupportButtonTintList(colorStateList);

            radioButton.setText(titles.get(i));
            if(radioButton.getParent() != null) {
                ((ViewGroup)radioButton.getParent()).removeView(radioButton);
            }
            radioGroup.addView(radioButton);
            radioButton.setPadding(padding, padding, padding, padding);
            int finalI = i;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    h.postDelayed(new Runnable() {
                        public void run() {
                            alertDialog.cancel();
                        }
                    }, 350);
                }
            });
            radioButton.setTextSize(textSize);
        }

        if (selected != -1)
            CheckIfCheckingNeeded(mcontext, adapter, title, radioButtons, selected);

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.YELLOW);

    }

    private void CheckIfCheckingNeeded(Context mcontext, SignInFragment adapter, String dialogName, ArrayList<AppCompatRadioButton> radioButtons, int selected) {
        if (dialogName.equals(mcontext.getString(R.string.reminder))) {
            if (adapter.reminderSelected == -1)
                radioButtons.get(selected).setChecked(true);
            else
                radioButtons.get(adapter.reminderSelected).setChecked(true);
        }
        else if (dialogName.equals(mcontext.getString(R.string.reminderType))) {
            if (adapter.reminderTypeSelected == -1)
                radioButtons.get(selected).setChecked(true);
            else
                radioButtons.get(adapter.reminderTypeSelected).setChecked(true);
        }
        else if (dialogName.equals(mcontext.getString(R.string.repeat))) {
            if (adapter.repeatSelected == -1)
                radioButtons.get(selected).setChecked(true);
            else
                radioButtons.get(adapter.repeatSelected).setChecked(true);
        }
    }

    //endregion
    private void CheckIfCheckingNeeded(Context mcontext, CreateProjectsAdapter adapter, String dialogName, ArrayList<AppCompatRadioButton> radioButtons, int selected) {
        if (dialogName.equals(mcontext.getString(R.string.reminder))) {
            if (adapter.reminderSelected == -1)
                radioButtons.get(selected).setChecked(true);
            else
                radioButtons.get(adapter.reminderSelected).setChecked(true);
        }
        else if (dialogName.equals(mcontext.getString(R.string.reminderType))) {
            if (adapter.reminderTypeSelected == -1)
                radioButtons.get(selected).setChecked(true);
            else
                radioButtons.get(adapter.reminderTypeSelected).setChecked(true);
        }
        else if (dialogName.equals(mcontext.getString(R.string.repeat))) {
            if (adapter.repeatSelected == -1)
                radioButtons.get(selected).setChecked(true);
            else
                radioButtons.get(adapter.repeatSelected).setChecked(true);
        }
    }
}

