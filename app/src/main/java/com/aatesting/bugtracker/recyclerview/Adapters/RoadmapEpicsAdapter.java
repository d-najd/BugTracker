package com.aatesting.bugtracker.recyclerview.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.recyclerview.RecyclerData;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RoadmapEpicsAdapter extends RecyclerView.Adapter<RoadmapEpicsAdapter.RecyclerViewHolder> {

    public ArrayList<RecyclerData> recyclerDataArrayList;
    public Context mcontext;
    public RecyclerViewHolder holder;
    public RecyclerData recyclerData;

    private Date weeksStartDateTime;
    private Date startDateTime;
    private Date endDateTime;

    public RoadmapEpicsAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Date weeksStartDateTime, Context mcontext) {
        this.weeksStartDateTime = weeksStartDateTime;
        this.recyclerDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
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

        Calendar calendarWeeksStartDateTime = GregorianCalendar.getInstance();
        calendarWeeksStartDateTime.set(Calendar.MILLISECOND, 0);
        calendarWeeksStartDateTime.set(Calendar.SECOND, 0);
        calendarWeeksStartDateTime.set(Calendar.MINUTE, 0);
        calendarWeeksStartDateTime.set(Calendar.HOUR_OF_DAY, 0);
        weeksStartDateTime = calendarWeeksStartDateTime.getTime();

        startDateTime = recyclerData.getCalendarStartDate().getTime();
        endDateTime = recyclerData.getCalendarEndDate().getTime();

        SetEpicDimensions();
        SetEpicDescription();
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

    private void SetEpicDimensions(){
        //starting pos (the margin)

        long timeDifference = startDateTime.getTime() - weeksStartDateTime.getTime(); //value is milliseconds
        long daysDifference = timeDifference / (24 * 60 * 60 * 1000); //24 is hours, 60 and 60 are min and seconds and 1000 is milliseconds

        float weeksLen = holder.itemView.getContext().getResources().getDimension(R.dimen.activity_roadmap_weeks_width);

        RelativeLayout.LayoutParams marginStart = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
        marginStart.setMarginStart((int) (((weeksLen / 7) * daysDifference) - (weeksLen / 7)));

        //ending pos (the width)
        timeDifference = endDateTime.getTime() - startDateTime.getTime();
        daysDifference = timeDifference / (24 * 60 * 60 * 1000);

        RelativeLayout.LayoutParams cardViewWidth = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
        cardViewWidth.width = (int) ((weeksLen / 7) * daysDifference);
    }

    private void SetEpicDescription(){
        SimpleDateFormat df = new SimpleDateFormat("dd' 'MMM");
        String startDateStr = df.format(startDateTime.getTime());
        String endDateStr = df.format(endDateTime.getTime());

        holder.description.setText(startDateStr + " - " + endDateStr);

        //setting the tag because the description does not hold the year
        df = new SimpleDateFormat("dd'-'MM'-'yyyy");
        startDateStr = df.format(startDateTime.getTime());
        endDateStr = df.format(endDateTime.getTime());

        holder.description.setTag(startDateStr + endDateStr);
    }

    @Override
    public int getItemCount() {
        return recyclerDataArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public CardView cardView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.adapterMainTxt);
            description = itemView.findViewById(R.id.adapterSecondaryTxt);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}