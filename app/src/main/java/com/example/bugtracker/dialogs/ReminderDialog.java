package com.example.bugtracker.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.bugtracker.R;
import com.example.bugtracker.recyclerview.RecyclerAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;

import java.util.ArrayList;

public class ReminderDialog {

    private static int test = 1;

    public void startDialog(View v, Context mcontext){
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_datetime_remindertype, viewGroup, false);

        ArrayList<RadioButton> radioButtonArrayList = new ArrayList<>();
        RadioButton radioButton1 = dialogView.findViewById(R.id.radiobtn1);
        RadioButton radioButton2 = dialogView.findViewById(R.id.radiobtn2);
        RadioButton radioButton3 = dialogView.findViewById(R.id.radiobtn3);
        radioButtonArrayList.add(radioButton1);
        radioButtonArrayList.add(radioButton2);
        radioButtonArrayList.add(radioButton3);

        builder.setView(dialogView)
        .setTitle("Reminder")
        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.dark_gray);

        Handler h = new Handler() ;

        for (int i = 0; i < radioButtonArrayList.size(); i++){
            int finalI = i;
            radioButtonArrayList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    h.postDelayed(new Runnable() {
                        public void run() {
                            //check which buttons is pressed
                        }
                    }, 2000);
                }
            });
        }
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.YELLOW);
    }
}
