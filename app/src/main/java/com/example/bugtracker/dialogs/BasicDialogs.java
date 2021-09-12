package com.example.bugtracker.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.Message;
import com.example.bugtracker.ProjectCreateTableData;
import com.example.bugtracker.R;
import com.example.bugtracker.activities.ProjectCreateTable;
import com.example.bugtracker.recyclerview.Adapters.MainRecyclerAdapter;
import com.example.bugtracker.recyclerview.Adapters.ProjectTableCreateAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class BasicDialogs {
    public static void BasicDialog(Context mcontext, String title, String description, String negativeButtonTxt){
        Pair<AlertDialog.Builder, EditText> data = BasicDialogConstructor(mcontext, title, description, negativeButtonTxt, false);
        AlertDialog.Builder builder = data.first;
        
        DialogBuilder(mcontext, builder);
    }

    public static void RenameColumnDialog(Context mcontext, String title, String description, String negativeButtonTxt,
                                          String positiveButtonTxt, int holderPos, ProjectCreateTable projectCreateTableActivity,
                                            ProjectTableCreateAdapter projectTableCreateAdapter) {
        Pair<AlertDialog.Builder, EditText> data = BasicDialogConstructor(mcontext, title, description, negativeButtonTxt, true);
        AlertDialog.Builder builder = data.first;

        builder.setNegativeButton(negativeButtonTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                projectCreateTableActivity.RefreshActivity();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                projectCreateTableActivity.RefreshActivity();
            }
        });

        builder.setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTitle = data.second.getText().toString();

                projectCreateTableActivity.RefreshActivity();
                projectTableCreateAdapter.RenameTitle(holderPos, newTitle);
            }
        });

        DialogBuilder(mcontext, builder);
    }

    //for adding new column
    public static void NewColumnDialog(Context mcontext, String title, String positiveButtonTxt,
                                       String negativeButtonTxt, String projectName,
                                       ProjectCreateTable projectCreateTableActivity, Intent intent){
        Pair<AlertDialog.Builder, EditText> data = BasicDialogConstructor(mcontext, title, null, negativeButtonTxt, true);
        AlertDialog.Builder builder = data.first;
        EditText editText = data.second;

        builder.setNegativeButton(negativeButtonTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                projectCreateTableActivity.RefreshActivity();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                projectCreateTableActivity.RefreshActivity();
            }
        });

        builder.setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<String> titlesEmptyArr = new ArrayList<>();
                        ArrayList<String> descriptionsEmptyArr = new ArrayList<>();
                        ArrayList<Integer> imgsEmptyArr = new ArrayList<>();


                        //titlesEmptyArr.add("HOHOHO");
                        //imgsEmptyArr.add(R.drawable.ic_account_24dp);
                        String title = editText.getText().toString();

                        ProjectCreateTableData.SaveNewColumn(projectName, title, titlesEmptyArr, imgsEmptyArr, descriptionsEmptyArr, mcontext);

                        //TODO seems like refreshing the activity doesnt solve all problems,
                        // if the first element is empty while adding new element it sets the data
                        // to the first instead of last or some other problems

                        //the data from the recyclerviews gets removed for some reason when editText
                        //is pressed, refreshing the activity is the only way that I can think of,
                        //notifying the adapters that the data is changed in any way doesn't work
                        projectCreateTableActivity.RefreshActivity();
                    }

                });
        DialogBuilder(mcontext, builder);
    }

    //TODO refreshing the data to fix problems doesnt seem to work out, need to fidn other way to
    // refresh the activity

    //for adding new item inside the column
    public static void NewColumnItemDialog(Context mcontext, String title, String positiveButtonTxt,
                                           String negativeButtonTxt, int position, String projectName,
                                           ProjectCreateTable projectCreateTableActivity){
        Pair<AlertDialog.Builder, EditText> data = BasicDialogConstructor(mcontext, title, null, negativeButtonTxt, true);
        AlertDialog.Builder builder = data.first;
        EditText editText = data.second;

        builder.setNegativeButton(negativeButtonTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                projectCreateTableActivity.RefreshActivity();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                projectCreateTableActivity.RefreshActivity();
            }
        });

        builder.setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = editText.getText().toString();

                ProjectCreateTableData.SaveNewItem(title, projectName, position, true, mcontext);
                projectCreateTableActivity.RefreshActivity();
            }
        });

       DialogBuilder(mcontext, builder);
    }

    public static Pair<MainRecyclerAdapter, BottomSheetDialog> BottomDialogCreator(Context context, View v, ViewGroup viewGroup,
                                                                                   String titleTxt, String descriptionTxt, ArrayList<String> titles,
                                                                                   ArrayList<String> descriptions, ArrayList<Integer> images, String tag) {
        ArrayList<RecyclerData> recyclerDataArrayList = new ArrayList<>();

        //for making a recyclerDataList of all the items
        if (titles != null && images != null && descriptions != null){
            for (int i = 0; i < titles.size(); i++)
                recyclerDataArrayList.add(new RecyclerData(titles.get(i), descriptions.get(i), images.get(i), tag));
        } else if (titles != null && images != null) {
            for (int i = 0; i < titles.size(); i++)
                recyclerDataArrayList.add(new RecyclerData(titles.get(i), images.get(i), tag));
        } else {
            Message.message(context, "titles, images or both haven't been set while creating a bottomdialog, the dialog will not be created");
            Log.wtf("Debug", "titles, images or both haven't been set while creating a bottomdialog, the dialog will not be created");
            return new Pair<>(null, null);
        }

        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.bottomdialog_complex_recyclerview, viewGroup, false);
        BottomSheetDialog bottomDialog = new BottomSheetDialog(context);
        bottomDialog.getWindow().setBackgroundDrawableResource(R.color.darkGray);
        bottomDialog.setContentView(dialogView);

        //for changing the size
        //View bottomSheet = bottomDialog.findViewById(R.id.design_bottom_sheet);
        //bottomSheet.getLayoutParams().height = 500;

        bottomDialog.show();

        RecyclerView bottomDialogRecyclerView = dialogView.findViewById(R.id.BtmDialogRecyclerview);
        TextView titleTextView = dialogView.findViewById(R.id.BtmDialogTitle);
        TextView descriptionTextView = dialogView.findViewById(R.id.BtmDialogDescription);

        if (titleTextView != null){
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(titleTxt);
        }
        if (descriptionTxt != null && titleTextView != null){
            descriptionTextView.setVisibility(View.VISIBLE);
            descriptionTextView.setText(descriptionTxt);
        } else if (descriptionTxt != null){
            Message.message(context, "title for the bottomtext should be set before setting a description, the bottomdialog wont be created");
            Log.wtf("Debug", "title for the bottomtext much be set before the description is set");
            return new Pair<>(null, null);
        }

        MainRecyclerAdapter adapter = new MainRecyclerAdapter(recyclerDataArrayList, context);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        bottomDialogRecyclerView.setLayoutManager(layoutManager);
        bottomDialogRecyclerView.setAdapter(adapter);

        return new Pair<>(adapter, bottomDialog);
    }

    private static Pair<AlertDialog.Builder, EditText> BasicDialogConstructor(Context mcontext, String title, String description,
                                                                              String negativeButtonTxt, boolean isEditTextDialog){
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);

        final EditText editText = new EditText(mcontext);
        if (isEditTextDialog) {
            editText.setTextColor(mcontext.getColor(R.color.white87));
            editText.setHintTextColor(mcontext.getColor(R.color.white60));
            builder.setView(editText);
        }

        if (title != null)
            builder.setTitle( Html.fromHtml("<font color='#FFFFFF'>"  + title + "</font>"));
        else
            Log.wtf("Debug", "there is no title for creating dialog, there might be a problem");

        if (description != null && !isEditTextDialog)
            builder.setMessage(Html.fromHtml("<font color='#FFFFFF'>"  + description + "</font>"));
        else if (description != null && isEditTextDialog){
            editText.setText(description);
        }

        if (negativeButtonTxt != null)
            builder.setNegativeButton(negativeButtonTxt, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        return new Pair<>(builder, editText);
    }

    private static void DialogBuilder(Context mcontext, AlertDialog.Builder builder){
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.darkGray);

        alertDialog.show();

        try{
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mcontext.getColor(R.color.purple_200));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mcontext.getColor(R.color.purple_200));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}