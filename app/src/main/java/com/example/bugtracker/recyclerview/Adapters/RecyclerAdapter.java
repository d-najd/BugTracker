package com.example.bugtracker.recyclerview.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.bugtracker.activities.ProjectCreateTable;
import com.example.bugtracker.dialogs.BasicDialogs;
import com.example.bugtracker.dialogs.RadioGroupDialog;
import com.example.bugtracker.recyclerview.RecyclerData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{

    public ArrayList<RecyclerData> DataArrayList;
    private Context mcontext;
    private RecyclerViewHolder holder;
    private int activeHourBtn = 0; //needs to be integer because when the dialog is opened again new set of buttons is created and it fucks up everything
    private int activeMinuteBtn = 0;
    private boolean hourSelected = true; //to know whether the hours or minutes are selected so that activehour/minuteBtn can be selected
    private boolean am_pm_Selected = true; //true for am false for pm
    List<RecyclerViewHolder> holderArrayList = new ArrayList<RecyclerViewHolder>();

    private List<TextView> clockTexts = new ArrayList<TextView>();
    private List<ImageView> clockImages = new ArrayList<ImageView>();
    private List<String> clockHourStrings = new ArrayList<String>(); //strings for hours and minutes (1-12hours)...
    private List<String> clockMinuteStrings = new ArrayList<String>();

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

        if (recyclerData.getEditTextEnable()) {
            holder.editText.setVisibility(View.VISIBLE);
            holder.editText.setHint(recyclerData.getDescription());
            holder.description.setVisibility(View.GONE);
        } else if (recyclerData.getDescription() != null) {
            holder.description.setText(recyclerData.getDescription());
        } else
            holder.description.setVisibility(View.GONE);

        if (layout.equals(mcontext.getString(R.string.titleProjects))) {
            holder.secondaryBtn.setVisibility(View.VISIBLE);
        }

        if (recyclerData.getSecondImgId() != 0) {
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
        else if (holder.title.getText().equals(mcontext.getString(R.string.dueDate)))
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
                        BasicDialogs.BasicDialog(mcontext, "Reminder Warning",
                                "Tell me when. Set a date and time first", "OK, GOT IT");
                    }
                }
            });

            holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holderArrayList.get(position).description.getText().equals("Tap to add reminder")){
                        BasicDialogs.BasicDialog(mcontext, "Reminder Warning",
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
                    Intent intent = new Intent(mcontext, ProjectCreateTable.class);
                    intent.putExtra("projectName", holder.title.getText().toString());
                    mcontext.startActivity(intent);
                }
            });
            holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, ProjectCreateTable.class);
                    intent.putExtra("projectName", holder.title.getText().toString());
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

        //TODO FIX THIS
        else if (recyclerData.getTag().equals(R.string.createTask)){

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

            if (holder.title.getText().equals(mcontext.getString(R.string.newTask))) {
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

        else if (recyclerData.getTag().equals(R.string.createBoard))
            CreateBoardListeners();
    }

    private void CreateBoardListeners(){
        holder.mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void DateTime1(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_select_date, viewGroup, false);

        CalendarView calendarView = dialogView.findViewById(R.id.calendarView);
        TextView dayMonthTxt = dialogView.findViewById(R.id.dayMonth);
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
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.darkGray);

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
                            if (am_txt.getCurrentTextColor() != mcontext.getColor(R.color.lightGray))
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
            am_txt.setTextColor(mcontext.getColor(R.color.lightGray));
        }

        hours_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hours_txt.getCurrentTextColor() == mcontext.getColor(R.color.lightGray)) {
                    hourSelected = true;
                    hours_txt.setTextColor(mcontext.getColor(R.color.white));
                    minutes_txt.setTextColor(mcontext.getColor(R.color.lightGray));

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
                if (minutes_txt.getCurrentTextColor() == mcontext.getColor(R.color.lightGray)){
                    hourSelected = false;
                    minutes_txt.setTextColor(mcontext.getColor(R.color.white));
                    hours_txt.setTextColor(mcontext.getColor(R.color.lightGray));

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
                    pm_txt.setTextColor(mcontext.getColor(R.color.lightGray));
                }
            }
        });

        pm_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (am_pm_Selected){
                    am_pm_Selected = false;
                    pm_txt.setTextColor(mcontext.getColor(R.color.white));
                    am_txt.setTextColor(mcontext.getColor(R.color.lightGray));
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

    private void UpdateDateTime(String curDate, String curTime) {
        RecyclerAdapter adapter = this;
        RadioGroupDialog radioGroupDialog = new RadioGroupDialog();

        int reminderPos = 0;

        for (int i = 0; i < DataArrayList.size(); i++) {
            RecyclerViewHolder curHolder = holderArrayList.get(i);
            if (DataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.dueDate))) {

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

            else if (DataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.reminderType)) && !reminderAdded) {
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
            else if (DataArrayList.get(i).getTitle().equals(mcontext.getString(R.string.reminderType)) && dialogName.equals(DataArrayList.get(i).getTitle())) {
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

    //specialpass is to skip checking and if you are completly sure that it will work and no way to fail
    //also special pass wont hidebuttons bc both will be hidden once they are enabled by default
    //thus fucking everything up so they have to be manualy dissabled outside of here
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
            title = itemView.findViewById(R.id.adapterMainText);
            description = itemView.findViewById(R.id.adapterSecondaryTxt);
            editText = itemView.findViewById(R.id.adapterEditTxt);
            mainBtn = itemView.findViewById(R.id.adapterMainBtn);
            secondaryBtn = itemView.findViewById(R.id.adapterFavoriteBtn);
            id = itemView.findViewById(R.id.adapterSetID);
        }
    }
}