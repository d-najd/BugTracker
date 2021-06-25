package com.example.bugtracker.recyclerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.AnimationHandler;
import com.example.bugtracker.R;
import com.example.bugtracker.activities.CreateTaskActivity;
import com.example.bugtracker.dialogs.BasicDialog;
import com.example.bugtracker.dialogs.RadioGroupDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{

    private ArrayList<RecyclerData> DataArrayList;
    private Context mcontext;
    private int activeTimeBtn = 0; //needs to be integer because when the dialog is opened again new set of buttons is created and it fucks up everything
    private RecyclerViewHolder holder;
    List<RecyclerViewHolder> holderArrayList = new ArrayList<RecyclerViewHolder>();

    private List<TextView> clock_texts = new ArrayList<TextView>();
    private List<ImageView> clock_images = new ArrayList<ImageView>();

    private List<String> clockHour_strings = new ArrayList<String>();
    private List<String> clockMinute_strings = new ArrayList<String>();

    public int reminderSelected = -1;
    public int reminderTypeSelected = -1;
    public int repeatSelected = -1;

    private String curTime = "null";

    private boolean reminderAdded = false;

    public RecyclerAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context mcontext) {
        this.DataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout_checklist, parent, false);
        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RecyclerData recyclerData = DataArrayList.get(position);
        String layout = recyclerData.getTag();
        this.holder = holder;
        holderArrayList.add(holder);

        holder.title.setText(recyclerData.getTitle());
        holder.mainBtn.setImageResource(recyclerData.getImgId());
        holder.id.setText(recyclerData.getId());

        if (recyclerData.getEditTextEnable())
        {
            holder.editText.setVisibility(View.VISIBLE);
            holder.editText.setHint(recyclerData.getDescription());
            holder.description.setVisibility(View.GONE);
        } else if (recyclerData.getDescription() != null){
            holder.description.setText(recyclerData.getDescription());
        }
        else
            holder.description.setVisibility(View.GONE);

        if (layout.equals(mcontext.getString(R.string.title_projects))){
            holder.secondaryBtn.setVisibility(View.VISIBLE);
            }

        if (recyclerData.getSecondImgId() != 0){
            holder.secondaryBtn.setImageResource(recyclerData.getSecondImgId());
            holder.secondaryBtn.setVisibility(View.VISIBLE);
        }

        if (holder.title.getText().equals(mcontext.getString(R.string.tasks))) {
            /* I fucking hate this shit right here
            ArrayList<RecyclerData> recyclerDataArrayList;

            recyclerDataArrayList = new ArrayList<>();

            recyclerDataArrayList.add(new RecyclerData("efefe",  R.drawable.ic_sublist_24dp, "tag"));
            recyclerDataArrayList.add(new RecyclerData("efefe",  R.drawable.ic_sublist_24dp, "tag"));
            recyclerDataArrayList.add(new RecyclerData("efefe",  R.drawable.ic_sublist_24dp, "tag"));
            recyclerDataArrayList.add(new RecyclerData("efefe",  R.drawable.ic_sublist_24dp, "tag"));


            // added data from arraylist to adapter class.
            RecyclerAdapter adapter = new RecyclerAdapter(recyclerDataArrayList, mcontext);

            // setting grid layout manager to implement grid view.
            // in this method '1' represents number of columns to be displayed in grid view.
            LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);

            // at last set adapter to recycler view.
            RecyclerView recyclerView = holder.recyclerView;

            Toast.makeText(mcontext,  recyclerView + "", Toast.LENGTH_SHORT).show();

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
             */
        }
        Listeners(position);
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return DataArrayList.size();
    }

    private void Listeners(int position){
        RecyclerData recyclerData = DataArrayList.get(position);
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                recyclerData.setDescription(s + "");
                DataArrayList.get(position).setDescription(s + "");
            }
        });


        if (holder.title.getText().equals(mcontext.getString(R.string.highlight)))
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
        else if (holder.title.getText().equals(mcontext.getString(R.string.due_date)))
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateTime1(v);
                }
            });
            holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateTime1(v);
                }
            });
        }

        else if (holder.title.getText().equals(mcontext.getString(R.string.reminder)))
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holderArrayList.get(position).description.getText().equals("Tap to add reminder")){
                        BasicDialog basicDialog = new BasicDialog();
                        basicDialog.StartDialog(mcontext, "Reminder Warning",
                                "Tell me when. Set a date and time first", "OK, GOT IT");
                    }
                }
            });

            holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holderArrayList.get(position).description.getText().equals("Tap to add reminder")){
                        BasicDialog basicDialog = new BasicDialog();
                        basicDialog.StartDialog(mcontext, "Reminder Warning",
                                "Tell me when. Set a date and time first", "OK, GOT IT");
                    }
                }
            });
        }

        if (recyclerData.getTag().equals(mcontext.getString(R.string.title_projects))) { //TODO FIX THIS IT SEEMS TO BE BROKEN SETTING STAR ON WRONG ITEM
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
        }

        else if (recyclerData.getTag().equals(mcontext.getString(R.string.create_project))){
            if (holder.title.getText().equals(mcontext.getString(R.string.reminder_type))){
                holder.itemView.setVisibility(View.GONE);
            }
            else if (holder.title.getText().equals(mcontext.getString(R.string.repeat))){
                holder.itemView.setVisibility(View.GONE);
            }
        }


        //TODO FIX THIS SHIT
        else if (recyclerData.getTag().equals(R.string.create_task)){

            if (position == 0){ //TODO this should be changed with the way of updatedatetime because its more reliable
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SelectBetweenTaskEpic();
                        Toast.makeText(mcontext, "test", Toast.LENGTH_SHORT).show();
                    }
                });
                /* TODO CHECK IF THIS NEEDS TO STAY HERE
                holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SelectBetweenTaskEpic();
                        Toast.makeText(mcontext, "test", Toast.LENGTH_SHORT).show();

                    }
                });

                 */
            }

            if (holder.title.getText().equals(mcontext.getString(R.string.new_task))) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mcontext, CreateTaskActivity.class);
                        mcontext.startActivity(intent);
                    }
                });
                holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mcontext, CreateTaskActivity.class);
                        mcontext.startActivity(intent);
                    }
                });
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void DateTime1(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_select_date, viewGroup, false);

        CalendarView calendarView = dialogView.findViewById(R.id.calendarView);
        TextView dayMonthTxt = dialogView.findViewById(R.id.day_month);
        TextView yearTxt = dialogView.findViewById(R.id.year);
        calendarView.getDate();

        String curTimeRaw = Calendar.getInstance().getTime().toString();
        String[] cutTimeSplitted = curTimeRaw.split("\\s+", 4);
        String curDate = cutTimeSplitted[0] + ", " + cutTimeSplitted[1] + " " + cutTimeSplitted[2];

        yearTxt.setText(Calendar.getInstance().getWeekYear() + "");
        dayMonthTxt.setText(curDate);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                String dayStr;
                String monthStr;

                switch (dayOfWeek){
                    case 1:
                        dayStr = "Sunday, ";
                        break;
                    case 2:
                        dayStr = "Monday, ";
                        break;
                    case 3:
                        dayStr = "Tuesday, ";
                        break;
                    case 4:
                        dayStr = "Wednesday, ";
                        break;
                    case 5:
                        dayStr = "Thursday, ";
                        break;
                    case 6:
                        dayStr = "Friday, ";
                        break;
                    case 7:
                        dayStr = "Saturday, ";
                        break;

                    default:
                        Toast.makeText(mcontext, "HOW THE FUCK DID YOU MANAGE TO BREAK THIS?", Toast.LENGTH_SHORT).show();
                        throw new IllegalStateException("Unexpected value: " + dayOfWeek);
                }

                switch (month){
                    case 0:
                        monthStr = "Jan ";
                        break;
                    case 1:
                        monthStr = "Feb ";
                        break;
                    case 2:
                        monthStr = "Mar ";
                        break;
                    case 3:
                        monthStr = "Apr ";
                        break;
                    case 4:
                        monthStr = "May ";
                        break;
                    case 5:
                        monthStr = "Jun ";
                        break;
                    case 6:
                        monthStr = "Jul ";
                        break;
                    case 7:
                        monthStr = "Aug ";
                        break;
                    case 8:
                        monthStr = "Sep ";
                        break;
                    case 9:
                        monthStr = "Oct ";
                        break;
                    case 10:
                        monthStr = "Nov ";
                        break;
                    case 11:
                        monthStr = "Dec ";
                        break;
                    default:
                        Toast.makeText(mcontext, "HOW THE FUCK DID YOU MANAGE TO BREAK THIS?", Toast.LENGTH_SHORT).show();
                        throw new IllegalStateException("Unexpected value: " + month);
                }

                yearTxt.setText(year + "");
                dayMonthTxt.setText(dayStr + monthStr + dayOfMonth);
            }
        });

        builder.setView(dialogView)

        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UpdateDateTime(curDate, "null");
                DateTime2(v, curDate);
            }
        })

        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.dark_gray);

        alertDialog.show();

        Window window = alertDialog.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.YELLOW);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.YELLOW);
    }

    private void DateTime2(View v, String curDate){
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        AnimationHandler animationHandler = new AnimationHandler();
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_select_hour, viewGroup, false);

        TextView hours_txt = dialogView.findViewById(R.id.hours);
        TextView minutes_txt = dialogView.findViewById(R.id.minutes);
        TextView am_txt = dialogView.findViewById(R.id.am);
        TextView pm_txt = dialogView.findViewById(R.id.pm);

        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String hour = "null";
                        String minutes = "null";

                        for (int i = 0; i < clockHour_strings.size(); i++){
                            if (hours_txt.getText().toString().equals(clockHour_strings.get(i)))
                                hour = hours_txt.getText().toString();
                            if (minutes_txt.getText().toString().equals(clockMinute_strings.get(i)))
                                minutes = minutes_txt.getText().toString();
                        }

                        if (!hour.equals("null") && !minutes.equals("null")){
                            if (am_txt.getCurrentTextColor() != mcontext.getColor(R.color.light_gray))
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
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.dark_gray);
        alertDialog.show();

        animationHandler.DateTimeListsToAdd();

        //region clockClickListeners

            //Toast.makeText(mcontext, clock_images.size() + "", Toast.LENGTH_SHORT).show();
            //put it under clockimages line
            clock_images.clear();
            clock_texts.clear();

                clock_images.add(dialogView.findViewById(R.id.clock_00));
                clock_images.add(dialogView.findViewById(R.id.clock_01));
                clock_images.add(dialogView.findViewById(R.id.clock_02));
                clock_images.add(dialogView.findViewById(R.id.clock_03));
                clock_images.add(dialogView.findViewById(R.id.clock_04));
                clock_images.add(dialogView.findViewById(R.id.clock_05));
                clock_images.add(dialogView.findViewById(R.id.clock_06));
                clock_images.add(dialogView.findViewById(R.id.clock_07));
                clock_images.add(dialogView.findViewById(R.id.clock_08));
                clock_images.add(dialogView.findViewById(R.id.clock_09));
                clock_images.add(dialogView.findViewById(R.id.clock_10));
                clock_images.add(dialogView.findViewById(R.id.clock_11));

                clock_texts.add(dialogView.findViewById(R.id.clock_00_txt));
                clock_texts.add(dialogView.findViewById(R.id.clock_01_txt));
                clock_texts.add(dialogView.findViewById(R.id.clock_02_txt));
                clock_texts.add(dialogView.findViewById(R.id.clock_03_txt));
                clock_texts.add(dialogView.findViewById(R.id.clock_04_txt));
                clock_texts.add(dialogView.findViewById(R.id.clock_05_txt));
                clock_texts.add(dialogView.findViewById(R.id.clock_06_txt));
                clock_texts.add(dialogView.findViewById(R.id.clock_07_txt));
                clock_texts.add(dialogView.findViewById(R.id.clock_08_txt));
                clock_texts.add(dialogView.findViewById(R.id.clock_09_txt));
                clock_texts.add(dialogView.findViewById(R.id.clock_10_txt));
                clock_texts.add(dialogView.findViewById(R.id.clock_11_txt));

            animationHandler.clock_images = clock_images;
            animationHandler.clock_texts = clock_texts;

            clockMinute_strings = animationHandler.clockMinute_strings;
            clockHour_strings = animationHandler.clockHour_strings;

        hours_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hours_txt.getCurrentTextColor() == mcontext.getColor(R.color.light_gray)) {
                    hours_txt.setTextColor(mcontext.getColor(R.color.white));
                    minutes_txt.setTextColor(mcontext.getColor(R.color.light_gray));

                    animationHandler.DateTimeHours();
                    animationHandler.PulseAnim(hours_txt);


                    //TODO FIXME I am guessing this is for when you have selected the hour to be 2
                    //and you change it to minutes it will be to 10, so if you change the minutes to
                    //30 for example when you switch time if it stays it will be 6 instead of 2 (the
                    //hour) so it needs to be changed back, but for some reason this doesnt seem to,
                    //work and for some reason I seem to be using a for loop which is going to
                    //reveal every single clock_image wtf? also its connected with the crashing when
                    //you try to set time for second time.
                    /*
                    outerloop:
                    for (int i = 0; i < clock_images.size(); i++) {
                        if (hours_txt.getText().toString().equals(clockHour_strings.get(i)) && activeTimeBtn != clock_images.get(i)) {
                            HideFAB(activeTimeBtn, v);
                            RevealFAB(clock_images.get(i), v);
                            break outerloop;
                        }
                    }
                     */
                }
            }
        });

        minutes_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minutes_txt.getCurrentTextColor() == mcontext.getColor(R.color.light_gray)){
                    minutes_txt.setTextColor(mcontext.getColor(R.color.white));
                    hours_txt.setTextColor(mcontext.getColor(R.color.light_gray));

                    animationHandler.DateTimeMinutes();
                    animationHandler.PulseAnim(minutes_txt);

                    //TODO FIXME for explanation check above like 10 lines up.
                    /*
                    outerloop:
                    for (int i = 0; i < clock_images.size(); i++){
                        if (minutes_txt.getText().toString().equals(clockMinute_strings.get(i)) && activeTimeBtn != clock_images.get(i)){
                            HideFAB(activeTimeBtn, v);
                            RevealFAB(clock_images.get(i), v);
                            break outerloop;
                        }
                    }
                     */
                }
            }
        });

        am_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (am_txt.getCurrentTextColor() == mcontext.getColor(R.color.light_gray)){
                    am_txt.setTextColor(mcontext.getColor(R.color.white));
                    pm_txt.setTextColor(mcontext.getColor(R.color.light_gray));
                }
            }
        });

        pm_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pm_txt.getCurrentTextColor() == mcontext.getColor(R.color.light_gray)){
                    pm_txt.setTextColor(mcontext.getColor(R.color.white));
                    am_txt.setTextColor(mcontext.getColor(R.color.light_gray));
                }
            }
        });

        for (int i = 0; i < clock_texts.size(); i++){
            int finalI = i;
            clock_texts.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hours_txt.getCurrentTextColor() == mcontext.getColor(R.color.white))
                        hours_txt.setText(clockHour_strings.get(finalI));
                    else
                        minutes_txt.setText(clockMinute_strings.get(finalI));
                    RevealFAB(clock_images.get(finalI), v);
                }
            });
        }

        //endregion
    }

    private void UpdateDateTime(String curDate, String curTime) {
        RecyclerAdapter adapter = this;
        RadioGroupDialog radioGroupDialog = new RadioGroupDialog();

        int reminderPos = 0;

        for (int i = 0; i < DataArrayList.size(); i++) {
            RecyclerViewHolder curHolder = holderArrayList.get(i);
            if (DataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.due_date))) {

                if (curTime.equals("null")) {
                    curHolder.description.setText(curDate + " 12am");
                    DataArrayList.get(i).setDescription(curDate + "12am");
                } else {
                    curHolder.description.setText(curDate + " " + curTime);
                    DataArrayList.get(i).setDescription(curDate + " " + curTime);
                }
                curHolder.mainBtn.setColorFilter(Color.YELLOW);
            }

            //this part is for the placement of the rest of the items
            else if (DataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.reminder)) && !reminderAdded)
            {
                    curHolder.description.setText("Remind me when due");
                    DataArrayList.get(i).setDescription("Remind me when due");
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

            else if (DataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.reminder_type)) && !reminderAdded) {
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
                        radioGroupDialog.StartDialog(v, mcontext, adapter, values, 0, Color.YELLOW, mcontext.getString(R.string.reminder_type));
                    }
                });
                curHolder.mainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radioGroupDialog.StartDialog(v, mcontext, adapter, values,0, Color.YELLOW, mcontext.getString(R.string.reminder_type));
                    }
                });
                curHolder.mainBtn.setColorFilter(Color.YELLOW);

                curHolder.itemView.setVisibility(View.VISIBLE);
            }
            else if (DataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.repeat)) && !reminderAdded) {
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
        for (int i = 0; i < DataArrayList.size(); i++) {
            if (DataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.reminder)) && dialogName.equals(DataArrayList.get(i).getTitle())) {
                reminderSelected = selected;
                holderArrayList.get(i).description.setText(description);
                DataArrayList.get(i).setDescription(description);
            }
            else if (DataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.reminder_type)) && dialogName.equals(DataArrayList.get(i).getTitle())) {
                reminderTypeSelected = selected;
                holderArrayList.get(i).description.setText(description);
                DataArrayList.get(i).setDescription(description);
            }
            else if (DataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.repeat)) && dialogName.equals(DataArrayList.get(i).getTitle())){
                repeatSelected = selected;
                holderArrayList.get(i).description.setText(description);
                DataArrayList.get(i).setDescription(description);
            }
        }
    }

    private void RevealFAB(ImageView imageView, View v) {
        if (imageView != clock_images.get(activeTimeBtn)) {
            HideFAB(clock_images.get(activeTimeBtn), v);
            v.post(new Runnable() {
                @Override
                public void run() {
                    //THE ACTIVETIMEBTN NEEDS TO BE INT BECAUSE WHEN THE DIALOG IS RELOADED TO
                    //CHANGE THE TIME AGAIN IT REPLACES ALL ITEMS THUS REMOVING THE ACTIVETIMEBTN
                    //SO THATS WHY IT HAS TO BE INTEGER AND CHECK FOR CURRENT ITEM
                    activeTimeBtn = imageView;
                    int cx = imageView.getWidth() / 2;
                    int cy = imageView.getHeight() / 2;
                    float finalRadius = (float) Math.hypot(cx, cy);
                    Animator anim = ViewAnimationUtils.createCircularReveal(imageView, cx, cy, 0, finalRadius);
                    imageView.setVisibility(View.VISIBLE);
                    anim.start();
                }
            });
        }
    }

    private void HideFAB(ImageView imageView, View v) {
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

        v.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private void SelectBetweenTaskEpic(){
        // Create an alert builder
        String [] test = new String[2];
        test[0] = "Text";

        test[1] = "Checklist";
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Add")
                .setItems(test, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            Toast.makeText(mcontext, "Need to add Activity for this", Toast.LENGTH_SHORT).show();
                        }
                        else if (which == 1){
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // View Holder Class to handle Recycler View.
    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private TextView id;
        public EditText editText;
        private ImageButton mainBtn;
        private ImageButton secondaryBtn;
        public RecyclerView recyclerView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.adapter_main_text);
            description = itemView.findViewById(R.id.adapter_secondary_text);
            editText = itemView.findViewById(R.id.adapter_editText);
            mainBtn = itemView.findViewById(R.id.adapter_mainImgBtn);
            secondaryBtn = itemView.findViewById(R.id.adapter_favorite_button);
            id = itemView.findViewById(R.id.adapter_setId);
            recyclerView = itemView.findViewById(R.id.adapter_recyclerView);
        }
    }
}