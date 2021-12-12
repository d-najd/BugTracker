package com.aatesting.bugtracker.modifiedClasses;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.aatesting.bugtracker.Message;

public class ModifiedFragment extends Fragment {
    /**
     * for getting rid of the need to pass a specific fragment and functions for all specific fragments
     * @param code unique code for each response
     */
    public void onResponse(String code){
        if (code.equals("Error")) {
            Log.wtf("ERROR", "Error code for onResponse");
            Message.message(getContext(), "Something went wrong");
            return;
        }
        Log.wtf("ERROR", "onResponse method does not exist in the current fragment");
        Message.message(getContext(), "Something went wrong");
    }
}
