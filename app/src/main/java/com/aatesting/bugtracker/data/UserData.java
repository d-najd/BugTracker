package com.aatesting.bugtracker.data;

import android.content.Context;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.restApi.ApiJSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class UserData {
    private static final String FILE_NAME = "UserData";
    public static final String SEPARATOR = ":";

    public static void saveUser(Context context, ApiJSONObject user){
        String data =  user.getUsername() + SEPARATOR + user.getPassword();

        String encodedData = Base64.encodeToString(data.getBytes(), Base64.NO_WRAP);

        BufferedWriter writer = null;
        File f = new File(context.getFilesDir() + File.separator, FILE_NAME + ".txt");

        try {
            writer = new BufferedWriter(new FileWriter(f, false));
            writer.write(encodedData);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * returns the user saved in memory
     * @return the user data if there is user saved in the device if not it doesn't return anything
     */
    public static ApiJSONObject getLastUser(Context context){
        String dataOld = getData(context);

        if (dataOld == null)
            return null;

        byte[] decodedBytes = Base64.decode(dataOld, Base64.NO_WRAP);
        String decodedString = new String(decodedBytes);

        String[] parts = decodedString.split(SEPARATOR);

        String username = parts[0];
        String password = parts[1];

        return new ApiJSONObject(username, password);
    }

    /**
     * @apiNote only to be used if the data is encripted with base64
     * @return base64 string of the last user in the format username:password NOTE the : is the seperator
     */
    public static String getLastUserRaw(Context context){
        return getData(context);
    }

    private static String getData(Context context){
        String data = null;

        File f = new File(context.getFilesDir() + File.separator, FILE_NAME + ".txt");
        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            int read = -1;
            StringBuffer buffer = new StringBuffer();
            while((read = fileInputStream.read())!= -1){
                buffer.append((char)read);
            }
            data = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * @apiNote sets the checkboxes in the ProjectSettingsFragment
     */
    public static void setRolesCheckboxes(View view, Boolean manageProject, Boolean manageUsers, Boolean create, Boolean edit, Boolean delete){
        CheckBox manageProjectCheckbox = view.findViewById(R.id.manageProjectCheckbox);
        CheckBox manageUsersCheckbox = view.findViewById(R.id.manageUsersCheckbox);
        CheckBox createCheckbox = view.findViewById(R.id.createCheckbox);
        CheckBox editCheckbox = view.findViewById(R.id.editCheckbox);
        CheckBox deleteCheckbox = view.findViewById(R.id.deleteCheckbox);

        manageProjectCheckbox.setChecked(manageProject);
        manageUsersCheckbox.setChecked(manageUsers);
        createCheckbox.setChecked(create);
        editCheckbox.setChecked(edit);
        deleteCheckbox.setChecked(delete);
    }
}
