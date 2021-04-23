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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.R;
import com.example.bugtracker.activities.CreateTaskActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{

    private ArrayList<RecyclerData> DataArrayList;
    private Context mcontext;
    private ImageView activeTimeBtn;

    ArrayList<RecyclerData> arrayList = new ArrayList<>();

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

        if (layout.equals(mcontext.getString(R.string.title_projects)))
            holder.secondaryBtn.setVisibility(View.VISIBLE);
        
        if (recyclerData.getSecondImgId() != 0){
            holder.secondaryBtn.setImageResource(recyclerData.getSecondImgId());
            holder.secondaryBtn.setVisibility(View.VISIBLE);
        }

        if (holder.title.getText().equals("Tasks")) {
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

        Listeners(holder, position);
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return DataArrayList.size();
    }

    private void Listeners(RecyclerViewHolder holder, int position){
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
            }
        });

        if (holder.title.getText().equals("Highlight"))
        {

            holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean favorite = recyclerData.getFavorite();

                    if (favorite) {
                        recyclerData.setFavorite(false);
                        holder.mainBtn.setImageResource(R.drawable.ic_empty_star_24dp);
                    } else {
                        recyclerData.setFavorite(true);
                        holder.mainBtn.setImageResource(R.drawable.ic_star_24dp);
                    }
                }
            });
        } else if (holder.title.getText().equals("Due Date"))
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

        if (recyclerData.getTag().equals(mcontext.getString(R.string.title_projects))) {
            holder.secondaryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean favorite = recyclerData.getFavorite();
                    if (favorite) {
                        recyclerData.setFavorite(false);
                        holder.secondaryBtn.setImageResource(R.drawable.ic_empty_star_24dp);
                        holder.secondaryBtn.setColorFilter(Color.WHITE);
                    } else {
                        recyclerData.setFavorite(true);
                        holder.secondaryBtn.setImageResource(R.drawable.ic_star_24dp);
                        holder.secondaryBtn.setColorFilter(Color.YELLOW);
                    }
                }
            });
        }

        //CreateProjects
        else if (recyclerData.getTag().equals(mcontext.getString(R.string.Create_Project)))
        {
            if (holder.title.getText().equals("Tasks")) {
                holder.title.setOnClickListener(new View.OnClickListener() {
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

        //TODO FIX THIS SHIT
        else if (recyclerData.getTag().equals(R.string.Create_Task)){
            Toast.makeText(mcontext, position, Toast.LENGTH_SHORT).show();

            if (position == 0){
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
        String curTime = cutTimeSplitted[0] + ", " + cutTimeSplitted[1] + " " + cutTimeSplitted[2];

        yearTxt.setText(Calendar.getInstance().getWeekYear() + "");
        dayMonthTxt.setText(curTime);

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
                        dayStr = "Sun, ";
                        break;
                    case 2:
                        dayStr = "Mon, ";
                        break;
                    case 3:
                        dayStr = "Tue, ";
                        break;
                    case 4:
                        dayStr = "Wed, ";
                        break;
                    case 5:
                        dayStr = "Thu, ";
                        break;
                    case 6:
                        dayStr = "Fri, ";
                        break;
                    case 7:
                        dayStr = "Sat, ";
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
                //TODO start the activity to set time and set the date
                DateTime2(v);
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


        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.YELLOW);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.YELLOW);


    }

    private void DateTime2(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_select_hour, viewGroup, false);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.dark_gray);
        alertDialog.show();

        //region clockClickListeners

        ImageView clock00 = dialogView.findViewById(R.id.clock_00);
        ImageView clock01 = dialogView.findViewById(R.id.clock_01);
        ImageView clock02 = dialogView.findViewById(R.id.clock_02);
        ImageView clock03 = dialogView.findViewById(R.id.clock_03);
        ImageView clock04 = dialogView.findViewById(R.id.clock_04);
        ImageView clock05 = dialogView.findViewById(R.id.clock_05);
        ImageView clock06 = dialogView.findViewById(R.id.clock_06);
        ImageView clock07 = dialogView.findViewById(R.id.clock_07);
        ImageView clock08 = dialogView.findViewById(R.id.clock_08);
        ImageView clock09 = dialogView.findViewById(R.id.clock_09);
        ImageView clock10 = dialogView.findViewById(R.id.clock_10);
        ImageView clock11 = dialogView.findViewById(R.id.clock_11);

        TextView clock00_txt = dialogView.findViewById(R.id.clock_00_txt);
        TextView clock01_txt = dialogView.findViewById(R.id.clock_01_txt);
        TextView clock02_txt = dialogView.findViewById(R.id.clock_02_txt);
        TextView clock03_txt = dialogView.findViewById(R.id.clock_03_txt);
        TextView clock04_txt = dialogView.findViewById(R.id.clock_04_txt);
        TextView clock05_txt = dialogView.findViewById(R.id.clock_05_txt);
        TextView clock06_txt = dialogView.findViewById(R.id.clock_06_txt);
        TextView clock07_txt = dialogView.findViewById(R.id.clock_07_txt);
        TextView clock08_txt = dialogView.findViewById(R.id.clock_08_txt);
        TextView clock09_txt = dialogView.findViewById(R.id.clock_09_txt);
        TextView clock10_txt = dialogView.findViewById(R.id.clock_10_txt);
        TextView clock11_txt = dialogView.findViewById(R.id.clock_11_txt);

        TextView hours_txt = dialogView.findViewById(R.id.hours);
        TextView minutes_txt = dialogView.findViewById(R.id.minutes);
        TextView am_txt = dialogView.findViewById(R.id.am);
        TextView pm_txt = dialogView.findViewById(R.id.pm);

        hours_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hours_txt.getCurrentTextColor() == mcontext.getColor(R.color.light_gray)){
                    hours_txt.setTextColor(mcontext.getColor(R.color.white));
                    minutes_txt.setTextColor(mcontext.getColor(R.color.light_gray));
                }
            }
        });

        minutes_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minutes_txt.getCurrentTextColor() == mcontext.getColor(R.color.light_gray)){
                    minutes_txt.setTextColor(mcontext.getColor(R.color.white));
                    hours_txt.setTextColor(mcontext.getColor(R.color.light_gray));
                }
            }
        });

        am_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        pm_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (activeTimeBtn == null)
            activeTimeBtn = clock00;

        clock00_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealFAB(clock00);
            }
        });
        clock01_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealFAB(clock01);
            }
        });
        clock02_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealFAB(clock02);
            }
        });
        clock03_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealFAB(clock03);
            }
        });
        clock04_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealFAB(clock04);
            }
        });
        clock05_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealFAB(clock05);
            }
        });
        clock06_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealFAB(clock06);
            }
        });
        clock07_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealFAB(clock07);
            }
        });
        clock08_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealFAB(clock08);
            }
        });
        clock09_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealFAB(clock09);
            }
        });
        clock10_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealFAB(clock10);
            }
        });
        clock11_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealFAB(clock11);
            }
        });


        //endregion
    }

    private void revealFAB(ImageView imageView) {
        //Check if any button is pressed

        if (imageView != activeTimeBtn) {
            hideFAB(activeTimeBtn);
            activeTimeBtn = imageView;
            int cx = imageView.getWidth() / 2;
            int cy = imageView.getHeight() / 2;
            float finalRadius = (float) Math.hypot(cx, cy);
            Animator anim = ViewAnimationUtils.createCircularReveal(imageView, cx, cy, 0, finalRadius);
            imageView.setVisibility(View.VISIBLE);
            //imageView.setImageResource(R.drawable.ic_circle_blue_42dp);
            anim.start();

        }
    }

    private void hideFAB(ImageView imageView) {
        int cx = imageView.getWidth() / 2;
        int cy = imageView.getHeight() / 2;
        float initialRadius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(imageView, cx, cy, initialRadius, 0);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imageView.setVisibility(View.INVISIBLE);
                //imageView.setImageResource(R.drawable.transparent);
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


                // set the custom layout
                //final View customLayout =
                //        getLayoutInflater().inflate(R.layout.custom_layout, null);
                //builder.setView(customLayout)


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


        // the ok button for exiting

                /*
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // send data from the
                        // AlertDialog to the Activity
                        //EditText editText = customLayout.findViewById(R.id.ctmAct_1stTxt);
                        //sendDialogDataToActivity(editText.getText().toString());
                        checkListBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "pressed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                */
        // create and show
        // the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // Do something with the data
    // coming from the AlertDialog


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
