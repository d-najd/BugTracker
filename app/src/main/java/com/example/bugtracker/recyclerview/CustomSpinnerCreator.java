package com.example.bugtracker.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.bugtracker.R;
import com.example.bugtracker.recyclerview.Adapters.CustomSpinnerAdapter;
import com.example.bugtracker.recyclerview.Adapters.ProjectTableCreateAdapter;

import java.util.List;

public class CustomSpinnerCreator<T> {
    private Context mcontext;
    private List<String> data;
    private View v;
    private int holderPosition;
    private String[] popUpContents;
    private ProjectTableCreateAdapter projectTableCreate_recyclerAdapter;

    public CustomSpinnerCreator(Context mcontext, List<String> data, int holderPosition,
                                     ProjectTableCreateAdapter projectTableCreate_recyclerAdapter,
                                     View v, int xoff, int yoff){
        this.mcontext = mcontext;
        this.data = data;
        this.holderPosition = holderPosition;
        this.projectTableCreate_recyclerAdapter = projectTableCreate_recyclerAdapter;
        popUpContents = new String[data.size()];
        data.toArray(popUpContents);

        PopupWindow().showAsDropDown(v, xoff, yoff);
    }

    private PopupWindow PopupWindow() {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(mcontext);

        // the drop down list is a list view
        ListView listView = new ListView(mcontext);

        // set our adapter and pass our pop up window contents
        listView.setAdapter(Adapter(popUpContents));

        // set the item click listener
        listView.setOnItemClickListener(new CustomSpinnerAdapter(this, popupWindow));

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(mcontext.getDrawable(R.color.darkGray));
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
                listItem.setTextSize(18);
                if (position != getCount() - 1)
                    listItem.setPadding(32, 32, 32, 26);
                else
                    listItem.setPadding(32, 26, 32, 50);
                listItem.setTextColor(Color.LTGRAY);

                return listItem;
            }
        };

        return adapter;
    }

    public void SendInfoBack(String itemText, int itemPosition){
        if (projectTableCreate_recyclerAdapter != null){
            projectTableCreate_recyclerAdapter.CustomSpinnerItemPressed(itemText, holderPosition, itemPosition);
        }
    }
}
