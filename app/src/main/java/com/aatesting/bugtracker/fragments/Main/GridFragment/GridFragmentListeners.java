package com.aatesting.bugtracker.fragments.Main.GridFragment;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.fragments.Main.GridFragment.Views.GridFragmentBackgroundView;

public class GridFragmentListeners implements View.OnLongClickListener, View.OnDragListener {
    private View root;
    public GridFragmentBackgroundView gridFragmentBackgroundView;
    private float dp;

    GridFragmentListeners (View root, GridFragmentBackgroundView gridFragmentBackgroundView){
        this.root = root;
        this.gridFragmentBackgroundView = gridFragmentBackgroundView;
        dp = root.getContext().getResources().getDisplayMetrics().density;
    }

    /**
     * sets position of a given view
     * @throws exception when its not able to set the position
     */
    private void setPos(String tag, float x, float y) throws Exception {
        View tagView = root.findViewWithTag(tag);

        //in short we are adjusting the position so it follows the dots on the screen
        float newX = Math.round((x - tagView.getWidth()/2f) / (GridFragmentBackgroundView.spacing * dp)) * GridFragmentBackgroundView.spacing * dp;
        float newY = Math.round((y - tagView.getHeight()/2f) / (GridFragmentBackgroundView.spacing * dp)) * GridFragmentBackgroundView.spacing * dp;

        tagView.setX(newX);
        tagView.setY(newY);
    }

    @Override
    public boolean onLongClick(View v) {
        View.DragShadowBuilder mShadow = new View.DragShadowBuilder(v);
        ClipData.Item item = new ClipData.Item(v.getTag().toString());
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);

        v.startDragAndDrop(data, mShadow, null, 0);
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event)
    {
        String clipData;
        View tagView;
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                try {
                    ((ImageView) v).setColorFilter(Color.YELLOW);
                    v.invalidate();

                    clipData = event.getClipDescription().getLabel().toString();

                    tagView = root.findViewWithTag(clipData + "Layout");
                    tagView.setVisibility(View.VISIBLE);
                } catch (Exception e){
                    //e.printStackTrace();
                    Log.wtf("ERROR", "Unable to get tag of view");
                    Message.defErrMessage(v.getContext());
                }

                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                ((ImageButton) v).setColorFilter(ContextCompat.getColor(v.getContext(), R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
                //clipData = event.getClipDescription().getLabel().toString();
                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                //Log.wtf("dLoc", "event pos x: " + event.getX() + " y: " + event.getY());
                //Log.wtf("dLoc", "view pos x: " + v.getX() + " y: " + v.getY());

                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                ((ImageButton) v).clearColorFilter();
                ((ImageButton) v).setColorFilter(Color.YELLOW);

                v.invalidate();
                return true;

            case DragEvent.ACTION_DROP:
                try {
                    v.invalidate();

                    clipData = event.getClipDescription().getLabel().toString();
                    tagView = root.findViewWithTag(clipData + "Layout");

                    setPos(clipData + "Layout", event.getX(), event.getY());

                    tagView.setVisibility(View.VISIBLE);
                } catch (Exception e){
                    //e.printStackTrace();
                    Log.wtf("ERROR", "Unable to move view");
                    Message.defErrMessage(v.getContext());
                }

                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                ((ImageButton) v).clearColorFilter();
                return true;
            default:
                return false;
        }
    }
}
