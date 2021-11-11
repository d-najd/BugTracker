package com.aatesting.bugtracker.recyclerview.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.RoadmapEditEpicActivity;
import com.aatesting.bugtracker.recyclerview.RecyclerData;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class RoadmapEpicsAdapter extends RecyclerView.Adapter<RoadmapEpicsAdapter.RecyclerViewHolder> {

    private ArrayList<RecyclerData> recyclerDataArrayList;
    private Context mcontext;
    private RecyclerViewHolder holder;
    private RecyclerData recyclerData;

    private String projectName;
    private Date weeksStartDateTime;
    private Date startDateTime;
    private Date endDateTime;

    public RoadmapEpicsAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Date weeksStartDateTime, String projectName, Context mcontext) {
        this.weeksStartDateTime = weeksStartDateTime;
        this.recyclerDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
        this.projectName = projectName;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardlayout_epic, parent, false);
        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerViewHolder holder, int position) {
        recyclerData = recyclerDataArrayList.get(position);
        this.holder = holder;

        holder.title.setText(recyclerData.getTitle());

        startDateTime = recyclerData.getCalendarStartDate().getTime();
        endDateTime = recyclerData.getCalendarEndDate().getTime();

        SetEpicDimensions();
        SetEpicDescription();
        SetEpicBackgroundColor(position);
        Listeners(position);

        /*
                if (daysDifference <= 0){
                Message.message(mcontext, "um the days difference in the epics inside RoadmapEpicsAdapter.java shouldn't be a negative value," +
                        "the start date should be smaller than the end date");
                Log.wtf("ERROR", "um the days difference in the epics inside RoadmapEpicsAdapter.java shouldn't be a negative value," +
                        "the start date should be smaller than the end date");
            }


        Calendar calendarSelectedDate = GregorianCalendar.getInstance();
        calendarSelectedDate.set(year, month, dayOfMonth);
        SimpleDateFormat df = new SimpleDateFormat("EEEE', 'MMM' 'd");
        String curDate = df.format(calendarSelectedDate.getTime());

     */
    }

    private void Listeners(int position){
        holder.epicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EpicPressed(position);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EpicPressed(position);
            }
        });
    }

    private void EpicPressed(int position){
        Intent intent = new Intent(mcontext, RoadmapEditEpicActivity.class);
        intent.putExtra("projectName", projectName);
        intent.putExtra("epicId", position);
        mcontext.startActivity(intent);


    }

    private void SetEpicDimensions(){
        //region CardView
        //starting pos (the margin)

        long timeDifference = startDateTime.getTime() - weeksStartDateTime.getTime(); //value is milliseconds
        long daysDifference = timeDifference / (24 * 60 * 60 * 1000); //24 is hours, 60 and 60 are min and seconds and 1000 is milliseconds


        /*NOTE this may break in the future because dpi on every device is different which means the
         density of pixels (dpi) in every device should be different and if that's true then
         because there is no way to set the length in float or double the int might not be
         accurate enough
         */

        /*TODO sometimes breaks and sets the date by 1 day or 1 week apart from what it should
           be, try 11th nov 21 with 11th apr 22.
         */

        double weekLen = holder.itemView.getContext().getResources().getDimension(R.dimen.activity_roadmap_weeks_width);

        //ending pos (the width)

        ConstraintLayout.LayoutParams cardViewLayoutParams = (ConstraintLayout.LayoutParams) holder.cardView.getLayoutParams();
        cardViewLayoutParams.setMarginStart((int)Math.round(((weekLen / 7) * daysDifference) - weekLen));

        timeDifference = endDateTime.getTime() - startDateTime.getTime();
        daysDifference = timeDifference / (24 * 60 * 60 * 1000);

        cardViewLayoutParams.width = ((int)Math.round((weekLen / 7) * daysDifference));
        //endregion

        //region ButtonListener
        //if the cardview is pressed or the position left and right of it

        ConstraintLayout.LayoutParams epicBtnLayoutParams = (ConstraintLayout.LayoutParams) holder.epicBtn.getLayoutParams();
        epicBtnLayoutParams.width = (int) (weekLen * GlobalValues.weeksRoadmapLen - 12);

        ConstraintLayout.LayoutParams epicBtnBackgroundLayoutParams = (ConstraintLayout.LayoutParams) holder.epicBtnBackground.getLayoutParams();
        epicBtnBackgroundLayoutParams.width = (int) (weekLen * GlobalValues.weeksRoadmapLen - 12);

        //endregion
    }

    private void SetEpicDescription(){
        SimpleDateFormat df = new SimpleDateFormat("dd' 'MMM");
        String startDateStr = df.format(startDateTime.getTime());
        String endDateStr = df.format(endDateTime.getTime());

        holder.description.setText(startDateStr + " - " + endDateStr);

        //setting the tag because the description does not hold the year
        df = new SimpleDateFormat(mcontext.getString(R.string.storageDateFormat));
        startDateStr = df.format(startDateTime.getTime());
        endDateStr = df.format(endDateTime.getTime());

        holder.description.setTag(startDateStr + endDateStr);
    }

    private void SetEpicBackgroundColor(int position){
        /*NOTE every second epic starting from the second one gets background color which is nearly
        transparent but not quite, its purpose is to know where the epic should be, so you do not
        have to scroll all the way and press the button but instead press the background
         */

        if (position % 2 == 1){
            holder.epicBtnBackground.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return recyclerDataArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public CardView cardView;
        public ImageButton epicBtn;
        public ImageView epicBtnBackground;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.adapterMainTxt);
            description = itemView.findViewById(R.id.adapterSecondaryTxt);
            cardView = itemView.findViewById(R.id.cardView);
            epicBtn = itemView.findViewById(R.id.epicBtn);
            epicBtnBackground = itemView.findViewById(R.id.epicBtnBackground);
        }
    }
}