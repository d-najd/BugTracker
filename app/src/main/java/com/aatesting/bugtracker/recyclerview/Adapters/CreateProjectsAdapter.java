package com.aatesting.bugtracker.recyclerview.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.aatesting.bugtracker.AnimationHandler;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.fragments.ProjectCreateTableFragment;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.dialogs.RadioGroupDialog;
import com.aatesting.bugtracker.recyclerview.RecyclerData;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class CreateProjectsAdapter extends MainRecyclerAdapter {

    private List<TextView> clockTexts = new ArrayList<TextView>();
    private List<ImageView> clockImages = new ArrayList<ImageView>();
    private List<String> clockHourStrings = new ArrayList<String>(); //strings for hours and minutes (1-12hours)...
    private List<String> clockMinuteStrings = new ArrayList<String>();

    private int activeHourBtn = 0; //needs to be integer because when the dialog is opened again new set of buttons is created and it fucks up everything
    private int activeMinuteBtn = 0;
    private boolean hourSelected = true; //to know whether the hours or minutes are selected so that activehour/minuteBtn can be selected
    private boolean am_pm_Selected = true; //true for am false for pm

    private String curTime = "null";
    private boolean reminderAdded = false;

    public int reminderSelected = -1;
    public int reminderTypeSelected = -1;
    public int repeatSelected = -1;

    public CreateProjectsAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context mcontext) {
        super(recyclerDataArrayList, mcontext);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Listeners(position);

        if (holder.title.getText().equals(mcontext.getString(R.string.reminderType))){
            holder.itemView.setVisibility(View.GONE);
        }
        else if (holder.title.getText().equals(mcontext.getString(R.string.repeat))){
            holder.itemView.setVisibility(View.GONE);
        }
    }

    private void Listeners(int position){


        if (holder.title.getText().toString().equals(mcontext.getString(R.string.highlight)))
        {
            holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean favorite = recyclerData.getFavorite();
                    if (favorite) {
                        recyclerData.setFavorite(false);
                        holderArrayList.get(position).mainBtn.setImageResource(R.drawable.ic_empty_star_24dp);
                    } else {
                        recyclerData.setFavorite(true);
                        holderArrayList.get(position).mainBtn.setImageResource(R.drawable.ic_star_24dp);
                    }
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean favorite = recyclerData.getFavorite();
                    if (favorite) {
                        recyclerData.setFavorite(false);
                        holderArrayList.get(position).mainBtn.setImageResource(R.drawable.ic_empty_star_24dp);
                    } else {
                        recyclerData.setFavorite(true);
                        holderArrayList.get(position).mainBtn.setImageResource(R.drawable.ic_star_24dp);
                    }
                }
            });
        }

        else if (holder.title.getText().equals(mcontext.getString(R.string.dueDate)))
        {
            CreateProjectsAdapter createProjectsAdapter = this;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //BasicDialogs.CalendarDateSetterDialog(mcontext, v, createProjectsAdapter);
                }
            });
            holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //BasicDialogs.CalendarDateSetterDialog(mcontext, v, createProjectsAdapter);
                }
            });
        }

        else if (holder.title.getText().equals(mcontext.getString(R.string.reminder)))
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holderArrayList.get(position).description.getText().equals("Tap to add reminder")){
                        Dialogs.BasicDialog(mcontext, "Reminder Warning",
                                "Tell me when. Set a date and time first", "OK, GOT IT");
                    }
                }
            });

            holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holderArrayList.get(position).description.getText().equals("Tap to add reminder")){
                        Dialogs.BasicDialog(mcontext, "Reminder Warning",
                                "Tell me when. Set a date and time first", "OK, GOT IT");
                    }
                }
            });
        }

        if (recyclerData.getTag().equals(mcontext.getString(R.string.titleProjects))) {
            holder.secondaryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean favorite = recyclerData.getFavorite();
                    if (favorite) {
                        recyclerData.setFavorite(false);
                        holderArrayList.get(position).secondaryBtn.setImageResource(R.drawable.ic_empty_star_24dp);
                    } else {
                        recyclerData.setFavorite(true);
                        holderArrayList.get(position).secondaryBtn.setImageResource(R.drawable.ic_star_24dp);
                    }
                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, ProjectCreateTableFragment.class);
                    int test = position;
                    intent.putExtra("projectName", holderArrayList.get(position).title.getText().toString());
                    mcontext.startActivity(intent);
                }
            });
            holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, ProjectCreateTableFragment.class);
                    int test = position;
                    intent.putExtra("projectName", holderArrayList.get(position).title.getText().toString());
                    mcontext.startActivity(intent);
                }
            });
        }

        else if (recyclerData.getTag().equals(mcontext.getString(R.string.createProject))){
            if (holder.title.getText().equals(mcontext.getString(R.string.reminderType))){
                holder.itemView.setVisibility(View.GONE);
            }
            else if (holder.title.getText().equals(mcontext.getString(R.string.repeat))){
                holder.itemView.setVisibility(View.GONE);
            }
        }
    }


    public void DateTime2(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        AnimationHandler animationHandler = new AnimationHandler();
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_select_hour, viewGroup, false);

        TextView hours_txt = dialogView.findViewById(R.id.hours);
        TextView minutes_txt = dialogView.findViewById(R.id.minutes);
        TextView am_txt = dialogView.findViewById(R.id.am);
        TextView pm_txt = dialogView.findViewById(R.id.pm);

        Calendar c = GregorianCalendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEEE', 'MMM' 'd");
        String curDate = df.format(c.getTime());

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

                        AllowReminders(curTime);
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

    //UPDATES THE REST OF THE RECYCLERVIEW ITEMS, FOR EXAMPLE MAKING YOU ABLE TO SELECT A REMINDER
    public void AllowReminders(String curTime) {
        CreateProjectsAdapter adapter = this;
        RadioGroupDialog radioGroupDialog = new RadioGroupDialog();

        Calendar c = GregorianCalendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEEE', 'MMM' 'd");
        String curDate = df.format(c.getTime());

        int reminderPos = 0;

        for (int i = 0; i < recyclerDataArrayList.size(); i++) {
            RecyclerViewHolder curHolder = holderArrayList.get(i);
            if (recyclerDataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.dueDate))) {

                if (curTime.equals("null")) {
                    curHolder.description.setText(curDate + " 12am");
                    recyclerDataArrayList.get(i).setDescription(curDate + "12am");
                } else {
                    curHolder.description.setText(curDate + " " + curTime);
                    recyclerDataArrayList.get(i).setDescription(curDate + " " + curTime);
                }
                curHolder.mainBtn.setColorFilter(Color.YELLOW);
            }

            //this part is for the placement of the rest of the items
            else if (recyclerDataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.reminder)) && !reminderAdded)
            {
                curHolder.description.setText("Remind me when due");
                recyclerDataArrayList.get(i).setDescription("Remind me when due");
                reminderPos = i;

                ArrayList<String> values = new ArrayList<String>();
                values.add("Don't remind me");
                values.add("Remind me when due");
                values.add("Remind me in advance");

                curHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radioGroupDialog.StartDialog(v, mcontext, adapter, values, 1, Color.YELLOW, mcontext.getString(R.string.reminder));
                    }
                });
                curHolder.mainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radioGroupDialog.StartDialog(v, mcontext, adapter, values, 1, Color.YELLOW, mcontext.getString(R.string.reminder));
                    }

                });
                curHolder.mainBtn.setColorFilter(Color.YELLOW);
            }

            else if (recyclerDataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.reminderType)) && !reminderAdded) {
                if (reminderPos == 0)
                    Toast.makeText(mcontext, "Congragulations you managed to break the reminderpos stuff", Toast.LENGTH_SHORT).show();
                notifyItemMoved(i, reminderPos + 1);

                ArrayList<String> values = new ArrayList<String>();
                values.add("Notification");
                values.add("Alarm");
                values.add("None");

                curHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radioGroupDialog.StartDialog(v, mcontext, adapter, values, 0, Color.YELLOW, mcontext.getString(R.string.reminderType));
                    }
                });
                curHolder.mainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radioGroupDialog.StartDialog(v, mcontext, adapter, values,0, Color.YELLOW, mcontext.getString(R.string.reminderType));
                    }
                });
                curHolder.mainBtn.setColorFilter(Color.YELLOW);

                curHolder.itemView.setVisibility(View.VISIBLE);
            }
            else if (recyclerDataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.repeat)) && !reminderAdded) {
                notifyItemMoved(i, reminderPos + 2);
                holderArrayList.get(i).itemView.setVisibility(View.VISIBLE);

                ArrayList<String> values = new ArrayList<String>();
                values.add("Does not repeat");
                values.add("Daily");
                values.add("Weekly");
                values.add("Monthly");
                values.add("Yearly");
                values.add("Specific days of week"); //TODO ADD THE ACTIVITIES FOR THESE 2 AND SWITCH COLOR WHEN PRESSING ANYTHING WHICH ISNT Does not repeat
                values.add("Advanced");

                curHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radioGroupDialog.StartDialog(v, mcontext, adapter, values,0, Color.YELLOW, mcontext.getString(R.string.repeat));
                    }
                });
                curHolder.mainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radioGroupDialog.StartDialog(v, mcontext, adapter, values,0, Color.YELLOW, mcontext.getString(R.string.repeat));
                    }
                });
                curHolder.itemView.setVisibility(View.VISIBLE);
            }
        }
        reminderAdded = true;
    }

    @SuppressLint("SetTextI18n")
    public void RadioButtonUpdateText(int selected, String dialogName, String description){
        for (int i = 0; i < recyclerDataArrayList.size(); i++) {
            if (recyclerDataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.reminder)) && dialogName.equals(recyclerDataArrayList.get(i).getTitle())) {
                reminderSelected = selected;
                holderArrayList.get(i).description.setText(description);
                recyclerDataArrayList.get(i).setDescription(description);
            }
            else if (recyclerDataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.reminderType)) && dialogName.equals(recyclerDataArrayList.get(i).getTitle())) {
                reminderTypeSelected = selected;
                holderArrayList.get(i).description.setText(description);
                recyclerDataArrayList.get(i).setDescription(description);
            }
            else if (recyclerDataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.repeat)) && dialogName.equals(recyclerDataArrayList.get(i).getTitle())){
                repeatSelected = selected;
                holderArrayList.get(i).description.setText(description);
                recyclerDataArrayList.get(i).setDescription(description);
            }
        }
    }

    private void RevealFAB(ImageView imageView, boolean specialPass) {
        if ((imageView != clockImages.get(activeHourBtn) && hourSelected) ||
                (imageView != clockImages.get(activeMinuteBtn) && !hourSelected) || specialPass) {

            if (!specialPass) {
                HideFAB(clockImages.get(activeMinuteBtn));
                HideFAB(clockImages.get(activeHourBtn));
            }

            int imageViewId = Integer.parseInt(imageView.toString().substring
                    (imageView.toString().length() - 3, imageView.toString().length() - 1));
            if (hourSelected) {
                activeHourBtn = imageViewId;
            }
            else {
                activeMinuteBtn = imageViewId;
            }
            int cx = imageView.getWidth() / 2;
            int cy = imageView.getHeight() / 2;
            float finalRadius = (float) Math.hypot(cx, cy);
            Animator anim = ViewAnimationUtils.createCircularReveal(imageView, cx, cy, 0, finalRadius);
            imageView.setVisibility(View.VISIBLE);
            anim.start();
        }
    }

    private void HideFAB(ImageView imageView) {
        int cx = imageView.getWidth() / 2;
        int cy = imageView.getHeight() / 2;
        float initialRadius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(imageView, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imageView.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }
}
