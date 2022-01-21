package com.aatesting.bugtracker.modifiedClasses;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.aatesting.bugtracker.Message;

public class ModifiedFragment extends Fragment {
    /**the method is used for getting rid of the need to call the specific fragment and creating 100's of copies of same method but with different fragment, so instead onResponse is called whenever you want to send a request to the specific fragment
     * @param code if nothing is wrong the code should be ok or Ok, if there is error it should be Error
     * @apiNote please check if the context is not null before proceeding
     */
    public void onResponse(String code){
        if (code.equals("Error") || code.equals("ERROR")) {
            Log.wtf("ERROR", "Error code for onResponse");
            Message.defErrMessage(getContext());
        }
    }

    /**the method is used for getting rid of the need to call the specific fragment and creating 100's of copies of same method but with different fragment, so instead onResponse is called whenever you want to send a request to the specific fragment
     * @param code if nothing is wrong the code should be ok or Ok, if there is error it should be Error
     * @param data use this field for passing data of type string
     * @apiNote please check if the context is not null before proceeding
     */
    public void onResponse(String code, String data) {
        if (code.equals("Error") || code.equals("ERROR")) {
            Log.wtf("ERROR", "Error code for onResponse");
            Message.defErrMessage(getContext());
        }
    }


}
