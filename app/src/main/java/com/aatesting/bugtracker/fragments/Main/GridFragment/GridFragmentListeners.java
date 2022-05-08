package com.aatesting.bugtracker.fragments.Main.GridFragment;

import android.content.ClipData;
import android.content.ClipDescription;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.fragments.Main.GridFragment.Views.GridFragmentArrowView;
import com.aatesting.bugtracker.fragments.Main.GridFragment.Views.GridFragmentBackgroundView;

public class GridFragmentListeners implements View.OnLongClickListener, View.OnDragListener {
    public GridFragmentBackgroundView gridFragmentBackgroundView;

    private View root;
    private float dp;

    GridFragmentListeners (View root, GridFragmentBackgroundView gridFragmentBackgroundView){
        this.root = root;
        this.gridFragmentBackgroundView = gridFragmentBackgroundView;
        dp = root.getContext().getResources().getDisplayMetrics().density;
    }

    /**
     * sets position of a view, will not work for all views
     */
    private void GenericSetPosition(String tag, float x, float y) {
        try {
            View tagView = root.findViewWithTag(tag);

            //in short we are adjusting the position so it follows the dots on the screen
            float newX = Math.round((x - tagView.getWidth() / 2f) / (GridFragmentSettings.spacing * dp)) * GridFragmentSettings.spacing * dp;
            float newY = Math.round((y - tagView.getHeight() / 2f) / (GridFragmentSettings.spacing * dp)) * GridFragmentSettings.spacing * dp;

            tagView.setX(newX);
            tagView.setY(newY);
        } catch (Exception e){
            Message.defErrMessage(root.getContext());
            Log.wtf("ERROR", "Unable to get or set position for view with tag " + tag);
        }
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
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                /*
                clipData = event.getClipDescription().getLabel().toString();

                if (clipData.contains(GridFragmentSettings.BOARD_TAG)){
                    //root.findViewWithTag(clipData + "Layout").setVisibility(View.INVISIBLE);
                }  else {
                    Message.defErrMessage(v.getContext());
                    Log.wtf("ERROR", "Invalid view tag, " + clipData);
                    return false;
                }

                 */
                return true;
            case DragEvent.ACTION_DROP:
                clipData = event.getClipDescription().getLabel().toString();

                try {
                    if (clipData.contains(GridFragmentSettings.BOARD_TAG)) {
                        GenericSetPosition(clipData + "Layout", event.getX(), event.getY());
                    } else if (clipData.contains(GridFragmentSettings.ARROW_MAIN)) {
                        if (clipData.contains(GridFragmentSettings.ARROW_HEAD_TAG)) {
                        } else if (clipData.contains(GridFragmentSettings.ARROW_BODY_TAG)) {
                            //just getting the id since we don't need the name
                            String[] idArr = clipData.split(GridFragmentSettings.ARROW_BODY_TAG);
                            String id = idArr[1];

                            GridFragmentArrowView tagView = root.findViewWithTag(GridFragmentSettings.ARROW_VIEW_TAG + id);

                            tagView.moveFully(event.getX(), event.getY());
                        } else {
                            Message.defErrMessage(v.getContext());
                            Log.wtf("ERROR", "invalid arrow tag ");
                            return false;
                        }
                    } else {
                        Message.defErrMessage(v.getContext());
                        Log.wtf("ERROR", "Invalid view tag, " + clipData);
                        return false;
                    }
                } catch (Exception e) {
                    Log.wtf("ERROR", "unable to moveFully " + clipData);
                    Message.defErrMessage(v.getContext());
                    return false;
                }
                return true;
            default:
                return false;
        }
    }
}
