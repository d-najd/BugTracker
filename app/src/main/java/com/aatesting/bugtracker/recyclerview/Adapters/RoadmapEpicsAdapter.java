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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RoadmapEpicsAdapter extends RecyclerView.Adapter<RoadmapEpicsAdapter.RecyclerViewHolder> {

    public ArrayList<RecyclerData> recyclerDataArrayList;
    public Context mcontext;
    public RecyclerViewHolder holder;
    public RecyclerData recyclerData;

    public RoadmapEpicsAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context mcontext) {
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
        holder.description.setText(recyclerData.getDescription());

        Calendar calendarWeeksStartDate = GregorianCalendar.getInstance();
        Calendar calendarStartDate = recyclerData.getCalendarStartDate();
        Calendar calendarEndDate = recyclerData.getCalendarEndDate();

        Date weeksStartDateTime = calendarWeeksStartDate.getTime();
        Date startDateTime = calendarStartDate.getTime();
        Date endDateTime = calendarEndDate.getTime();

        //region startDateTime
            long timeDifference = Math.abs(startDateTime.getTime() - weeksStartDateTime.getTime()); //value is milliseconds
            long daysDifference = timeDifference / (24 * 60 * 60 * 1000); //24 is hours, 60 and 60 are min and seconds and 1000 is milliseconds

            RelativeLayout.LayoutParams marginStart = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
            marginStart.setMarginStart((int) ((holder.itemView.getContext().getResources().getDimension(R.dimen.activity_roadmap_weeks_width) / 7) * daysDifference));
        //endregion
        //region endDateTime
            timeDifference = Math.abs(endDateTime.getTime() - startDateTime.getTime());
            daysDifference = timeDifference / (24 * 60 * 60 * 1000);

            RelativeLayout.LayoutParams cardViewWidth = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
            cardViewWidth.width = (int) ((holder.itemView.getContext().getResources().getDimension(R.dimen.activity_roadmap_weeks_width) / 7) * daysDifference);
        //endregion

        /*
                //TODO make it to check if endDateTime is bigger than startDateTime (the projects
                //TODO to end before it has even started which doesn't make sense)
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