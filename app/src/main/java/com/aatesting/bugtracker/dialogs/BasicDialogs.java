package com.aatesting.bugtracker.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.data.ProjectCreateTableData;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.ProjectCreateTableActivity;
import com.aatesting.bugtracker.activities.RoadmapCreateEpicActivity;
import com.aatesting.bugtracker.recyclerview.Adapters.CreateProjectsAdapter;
import com.aatesting.bugtracker.recyclerview.Adapters.MainRecyclerAdapter;
import com.aatesting.bugtracker.recyclerview.Adapters.ProjectTableCreateAdapter;
import com.aatesting.bugtracker.recyclerview.RecyclerData;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import kotlin.Triple;

public class BasicDialogs {
    public static void BasicDialog(Context mcontext, String title, String description, String negativeButtonTxt){
        Pair<AlertDialog.Builder, EditText> data = BasicEdittextDialogConstructor(mcontext, title, description, negativeButtonTxt, false);
        AlertDialog.Builder builder = data.first;
        
        DialogBuilder(mcontext, builder);
    }

    public static void RenameColumnDialog(Context mcontext, String title, String description, String negativeButtonTxt,
                                          String positiveButtonTxt, int holderPos, ProjectCreateTableActivity projectCreateTableActivity,
                                            ProjectTableCreateAdapter activity) {
        Pair<AlertDialog.Builder, EditText> data = BasicEdittextDialogConstructor(mcontext, title, description, negativeButtonTxt, true);
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
                activity.RenameTitle(holderPos, newTitle);
            }
        });

        DialogBuilder(mcontext, builder);
    }

    //for adding new column
    public static void NewColumnDialog(Context mcontext, String title, String positiveButtonTxt,
                                       String negativeButtonTxt, String projectName,
                                       ProjectCreateTableActivity activity, Intent intent){
        Pair<AlertDialog.Builder, EditText> data = BasicEdittextDialogConstructor(mcontext, title, null, negativeButtonTxt, true);
        AlertDialog.Builder builder = data.first;
        EditText editText = data.second;

        builder.setNegativeButton(negativeButtonTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.RefreshActivity();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                activity.RefreshActivity();
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
                        activity.RefreshActivity();
                    }

                });
        DialogBuilder(mcontext, builder);
    }

    //TODO refreshing the data to fix problems doesnt seem to work out, need to fiND other way to
    // refresh the activity

    //for adding new item inside the column
    public static void NewColumnItemDialog(Context mcontext, String title, String positiveButtonTxt,
                                           String negativeButtonTxt, int position, String projectName,
                                           ProjectCreateTableActivity activity){
        Pair<AlertDialog.Builder, EditText> data = BasicEdittextDialogConstructor(mcontext, title, null, negativeButtonTxt, true);
        AlertDialog.Builder builder = data.first;
        EditText editText = data.second;

        builder.setNegativeButton(negativeButtonTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.RefreshActivity();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                activity.RefreshActivity();
            }
        });

        builder.setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = editText.getText().toString();

                ProjectCreateTableData.SaveNewItem(title, projectName, position, true, mcontext);
                activity.RefreshActivity();
            }
        });

       DialogBuilder(mcontext, builder);
    }


    public static void CalendarDateSetterDialog(Context mcontext, View v, CreateProjectsAdapter activity){
        Triple<AlertDialog.Builder, CalendarView, View> data = BasicCalendarDialogConstructor(mcontext, v);
        AlertDialog.Builder builder = data.getFirst();
        CalendarView calendarView = data.getSecond();
        View dialogView = data.getThird();

        TextView dayMonthTxt = dialogView.findViewById(R.id.dayMonth);
        TextView yearTxt = dialogView.findViewById(R.id.year);

        final Calendar[] calendarSelectedDate = {GregorianCalendar.getInstance()};

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendarSelectedDate[0] = GregorianCalendar.getInstance();
                calendarSelectedDate[0].set(year, month, dayOfMonth);
                SimpleDateFormat df = new SimpleDateFormat("EEEE', 'MMM' 'd");
                String curDate = df.format(calendarSelectedDate[0].getTime());

                yearTxt.setText(year + "");
                dayMonthTxt.setText(curDate);
            }
        });

        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //use calendarSelectedDate[0] to pass calendar data
                        activity.AllowReminders("null");
                        activity.DateTime2(v);
                    }
                })

                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        DialogBuilder(mcontext, builder);
    }

    public static void CalendarDateSetterDialog(Context mcontext, View v, RoadmapCreateEpicActivity activity, boolean startDate){
        //TODO add a button on the leftBottom called REMOVE, for when a date has been already set but
        // you want to remove it and return it to default (empty)
        //true for startDate, false for dueDate

        Triple<AlertDialog.Builder, CalendarView, View> data = BasicCalendarDialogConstructor(mcontext, v);
        AlertDialog.Builder builder = data.getFirst();
        CalendarView calendarView = data.getSecond();
        View dialogView = data.getThird();

        TextView dayMonthTxt = dialogView.findViewById(R.id.dayMonth);
        TextView yearTxt = dialogView.findViewById(R.id.year);

        final Calendar[] calendarSelectedDate = {GregorianCalendar.getInstance()};

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendarSelectedDate[0] = GregorianCalendar.getInstance();
                calendarSelectedDate[0].set(year, month, dayOfMonth);
                SimpleDateFormat df = new SimpleDateFormat("EEEE', 'MMM' 'd");
                String curDate = df.format(calendarSelectedDate[0].getTime());

                yearTxt.setText(year + "");
                dayMonthTxt.setText(curDate);
            }
        });

        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (startDate) {
                            activity.UpdateStartDateDescription(calendarSelectedDate[0]);
                        }
                        else
                            activity.UpdateDueDateDescription(calendarSelectedDate[0]);
                    }
                })

                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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







    private static Pair<AlertDialog.Builder, EditText> BasicEdittextDialogConstructor(Context mcontext, String title, String description,
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

    @SuppressLint("SetTextI18n")
    private static Triple<AlertDialog.Builder, CalendarView, View> BasicCalendarDialogConstructor(Context mcontext, View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_select_date, viewGroup, false);

        CalendarView calendarView = dialogView.findViewById(R.id.calendarView);
        //calendarView.
        TextView dayMonthTxt = dialogView.findViewById(R.id.dayMonth);
        TextView yearTxt = dialogView.findViewById(R.id.year);
        calendarView.getDate();

        Calendar c = GregorianCalendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEEE', 'MMM' 'd");
        String curDate = df.format(c.getTime());

        yearTxt.setText(Calendar.getInstance().getWeekYear() + "");
        dayMonthTxt.setText(curDate);

        return new Triple<>(builder, calendarView, dialogView);
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


/*
     private void HourSetter(View v, String curDate){
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        AnimationHandler animationHandler = new AnimationHandler();
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_select_hour, viewGroup, false);

        TextView hours_txt = dialogView.findViewById(R.id.hours);
        TextView minutes_txt = dialogView.findViewById(R.id.minutes);
        TextView am_txt = dialogView.findViewById(R.id.am);
        TextView pm_txt = dialogView.findViewById(R.id.pm);

        hourSelected = true;

        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String hour = "null";
                        String minutes = "null";

                        for (int i = 0; i < clockHourStrings.size(); i++){
                            if (hours_txt.getText().toString().equals(clockHourStrings.get(i)))
                                hour = hours_txt.getText().toString();
                            if (minutes_txt.getText().toString().equals(clockMinuteStrings.get(i)))
                                minutes = minutes_txt.getText().toString();
                        }

                        if (!hour.equals("null") && !minutes.equals("null")){
                            if (am_txt.getCurrentTextColor() != mcontext.getColor(R.color.darkerWhite))
                                curTime = hour + ":" + minutes + "am";
                            else
                                curTime = hour + ":" + minutes + "pm";
                        }
                        else
                            Toast.makeText(mcontext, "something went wrong with setting getting" +
                                    "the hours_txt or minutes_txt", Toast.LENGTH_SHORT).show();

                        UpdateDateTime(curDate, curTime);
                    }
                })

                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });;

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.darkGray);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.YELLOW);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.YELLOW);

        animationHandler.DateTimeListsToAdd();

        //region clockClickListeners

        //Toast.makeText(mcontext, clock_images.size() + "", Toast.LENGTH_SHORT).show();
        //put it under clockimages line
        clockImages.clear();
        clockTexts.clear();

        clockImages.add(dialogView.findViewById(R.id.clock00));
        clockImages.add(dialogView.findViewById(R.id.clock01));
        clockImages.add(dialogView.findViewById(R.id.clock02));
        clockImages.add(dialogView.findViewById(R.id.clock03));
        clockImages.add(dialogView.findViewById(R.id.clock04));
        clockImages.add(dialogView.findViewById(R.id.clock05));
        clockImages.add(dialogView.findViewById(R.id.clock06));
        clockImages.add(dialogView.findViewById(R.id.clock07));
        clockImages.add(dialogView.findViewById(R.id.clock08));
        clockImages.add(dialogView.findViewById(R.id.clock09));
        clockImages.add(dialogView.findViewById(R.id.clock10));
        clockImages.add(dialogView.findViewById(R.id.clock11));

        clockTexts.add(dialogView.findViewById(R.id.clock00Text));
        clockTexts.add(dialogView.findViewById(R.id.clock01Text));
        clockTexts.add(dialogView.findViewById(R.id.clock02Text));
        clockTexts.add(dialogView.findViewById(R.id.clock03Text));
        clockTexts.add(dialogView.findViewById(R.id.clock04Text));
        clockTexts.add(dialogView.findViewById(R.id.clock05Text));
        clockTexts.add(dialogView.findViewById(R.id.clock06Text));
        clockTexts.add(dialogView.findViewById(R.id.clock07Text));
        clockTexts.add(dialogView.findViewById(R.id.clock08Text));
        clockTexts.add(dialogView.findViewById(R.id.clock09Text));
        clockTexts.add(dialogView.findViewById(R.id.clock10Text));
        clockTexts.add(dialogView.findViewById(R.id.clock11Text));

        animationHandler.clockImages = clockImages;
        animationHandler.clockTexts = clockTexts;

        clockMinuteStrings = animationHandler.clockMinuteStrings;
        clockHourStrings = animationHandler.clockHourStrings;

        hours_txt.setText(clockHourStrings.get(activeHourBtn));
        minutes_txt.setText(clockMinuteStrings.get(activeMinuteBtn));

        if (!am_pm_Selected)
        {
            pm_txt.setTextColor(mcontext.getColor(R.color.white));
            am_txt.setTextColor(mcontext.getColor(R.color.darkerWhite));
        }

        hours_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hours_txt.getCurrentTextColor() == mcontext.getColor(R.color.darkerWhite)) {
                    hourSelected = true;
                    hours_txt.setTextColor(mcontext.getColor(R.color.white));
                    minutes_txt.setTextColor(mcontext.getColor(R.color.darkerWhite));

                    animationHandler.DateTimeHours();
                    animationHandler.PulseAnim(hours_txt);

                    RevealFAB(clockImages.get(activeHourBtn), true);
                    HideFAB(clockImages.get(activeMinuteBtn));
                }
            }
        });

        minutes_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minutes_txt.getCurrentTextColor() == mcontext.getColor(R.color.darkerWhite)){
                    hourSelected = false;
                    minutes_txt.setTextColor(mcontext.getColor(R.color.white));
                    hours_txt.setTextColor(mcontext.getColor(R.color.darkerWhite));

                    animationHandler.DateTimeMinutes();
                    animationHandler.PulseAnim(minutes_txt);

                    RevealFAB(clockImages.get(activeMinuteBtn), true);
                    HideFAB(clockImages.get(activeHourBtn));
                }
            }
        });

        am_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!am_pm_Selected){
                    am_pm_Selected = true;
                    am_txt.setTextColor(mcontext.getColor(R.color.white));
                    pm_txt.setTextColor(mcontext.getColor(R.color.darkerWhite));
                }
            }
        });

        pm_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (am_pm_Selected){
                    am_pm_Selected = false;
                    pm_txt.setTextColor(mcontext.getColor(R.color.white));
                    am_txt.setTextColor(mcontext.getColor(R.color.darkerWhite));
                }
            }
        });

        for (int i = 0; i < clockTexts.size(); i++){
            int finalI = i;
            if (finalI == activeHourBtn) {
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        int cx = clockImages.get(finalI).getWidth() / 2;
                        int cy = clockImages.get(finalI).getHeight() / 2;
                        float finalRadius = (float) Math.hypot(cx, cy);
                        Animator anim = ViewAnimationUtils.createCircularReveal(clockImages.get(finalI), cx, cy, 0, finalRadius);
                        clockImages.get(finalI).setVisibility(View.VISIBLE);
                        anim.start();
                    }
                });
            }
            clockTexts.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hours_txt.getCurrentTextColor() == mcontext.getColor(R.color.white))
                        hours_txt.setText(clockHourStrings.get(finalI));
                    else
                        minutes_txt.setText(clockMinuteStrings.get(finalI));
                    RevealFAB(clockImages.get(finalI), false);
                }
            });
        }

        //endregion
    }
 */
}