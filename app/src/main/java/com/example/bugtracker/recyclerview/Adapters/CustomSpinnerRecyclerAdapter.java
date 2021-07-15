package com.example.bugtracker.recyclerview.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.example.bugtracker.Message;
import com.example.bugtracker.recyclerview.CustomSpinnerCreator;

public class CustomSpinnerRecyclerAdapter implements OnItemClickListener {

    private final CustomSpinnerCreator customSpinnerCreator;
    private final PopupWindow popupWindow;

    public CustomSpinnerRecyclerAdapter(CustomSpinnerCreator customSpinnerCreator, PopupWindow popupWindow) {
        this.customSpinnerCreator = customSpinnerCreator;
        this.popupWindow = popupWindow;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // get the context and main activity to access variables
        Context mcontext = view.getContext();

        //add some animation when a list item was clicked
        Animation fadeInAnimation = AnimationUtils.loadAnimation(mcontext, android.R.anim.fade_in);
        fadeInAnimation.setDuration(10);
        view.startAnimation(fadeInAnimation);

        // get the text and set it as the button text
        String selectedItemText = ((TextView)view).getText().toString();
        //if (projectTableCreate_RecyclerAdapter != null) {
            //projectTableCreate_RecyclerAdapter.position = position;
        //}

        // get the position
        //String selectedItemPosition = ((TextView)view).getTag().toString();
        Message.message(mcontext, "Pressed: " + selectedItemText + " at " + position);
        popupWindow.dismiss();
    }
}