package com.example.bugtracker.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.activities.ProjectCreateTable;
import com.example.bugtracker.recyclerview.Adapters.CustomSpinnerRecyclerAdapter;
import com.example.bugtracker.recyclerview.Adapters.ProjectTableCreate_RecyclerAdapter;

import java.util.List;

public class CustomSpinnerCreator {
    private Context mcontext;
    private List<String> data;
    private View v;
    String[] popUpContents;
    private ProjectTableCreate_RecyclerAdapter projectTableCreate_recyclerAdapter;

    //TODO find a way to not use class names and need to create constructor for every class
    //instead use something like object? android studio says smtn about type paramaters, that may
    //be of use
    public CustomSpinnerCreator(Context mcontext, List<String> data,
                                     ProjectTableCreate_RecyclerAdapter projectTableCreate_recyclerAdapter,
                                     View v, int xoff, int yoff){
        this.mcontext = mcontext;
        this.data = data;
        this.projectTableCreate_recyclerAdapter = projectTableCreate_recyclerAdapter;
        popUpContents = new String[data.size()];
        data.toArray(popUpContents);

        PopupWindow().showAsDropDown(v, xoff, yoff);
    }

    public PopupWindow PopupWindow() {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(mcontext);

        // the drop down list is a list view
        ListView listView = new ListView(mcontext);

        // set our adapter and pass our pop up window contents
        listView.setAdapter(Adapter(popUpContents));

        // set the item click listener
        listView.setOnItemClickListener(new CustomSpinnerRecyclerAdapter(this, popupWindow));

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(500);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(listView);

        return popupWindow;
    }


    private ArrayAdapter<String> Adapter(String data[]) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_list_item_1, data) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                //setting stuff for every item in the list
                String text = getItem(position);
                TextView listItem = new TextView(mcontext);

                listItem.setText(text);
                listItem.setTextSize(22);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);

                return listItem;
            }
        };

        return adapter;
    }

}
