package com.aatesting.bugtracker.fragments.Authentication;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.MainActivity;
import com.aatesting.bugtracker.data.UserData;
import com.aatesting.bugtracker.fragments.Main.ProjectsFragment;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiJSONObject;

public class SignUpFragment extends ModifiedFragment {
    ApiJSONObject user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_signup, container, false);

        ((MainActivity)getActivity()).fixBottomMenu();

        listeners(root);
        return root;
    }

    private void listeners(View root) {
        Handler handler = new Handler();
        View mainBtn = ((MainActivity)getActivity()).mainBtn;
        mainBtn.setVisibility(View.GONE);

        TextView signInText = root.findViewById(R.id.signInText);
        EditText usernameEdt = root.findViewById(R.id.usernameEdt);
        EditText passwordEdt = root.findViewById(R.id.passwordEdt);
        EditText reEnterPasswordEdt = root.findViewById(R.id.reEnterPasswordEdt);
        Button submit = root.findViewById(R.id.submit);

        ModifiedFragment fragment = this;

        usernameEdt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            passwordEdt.requestFocus();
                        }
                    }, 50);
                    return true;
                }
                return false;
            }
        });

        passwordEdt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            reEnterPasswordEdt.requestFocus();
                        }
                    }, 50);
                    return true;
                }
                return false;
            }
        });

        reEnterPasswordEdt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            signUp(usernameEdt, passwordEdt, reEnterPasswordEdt, fragment);
                        }
                    }, 50);
                    return true;
                }
                return false;
            }
        });

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new fragment and transaction
                Bundle bundle = new Bundle();
                SignInFragment signInFragment = new SignInFragment();
                signInFragment.setArguments(bundle);

                FragmentManager myFragmentManager = getParentFragmentManager();

                FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.navHostFragment, signInFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(usernameEdt, passwordEdt, reEnterPasswordEdt, fragment);
            }
        });

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(usernameEdt, passwordEdt, reEnterPasswordEdt, fragment);
            }
        });
    }

    private void signUp(EditText usernameEdt, EditText passwordEdt, EditText reEnterPasswordEdt, ModifiedFragment fragment) {
        String username = usernameEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        String password1 = reEnterPasswordEdt.getText().toString();

        //username or password less than 3 is not allowed
        if (username.length() <= 3 || password.length() <= 3 || password1.length() <= 3){
            Message.message(getContext(), "Username and password longer than 3 characters is required");
            return;
        }

        if (!password.equals(password1)){
            Message.message(getContext(), "The passwords don't match, re-enter the passwords and try again");
            return;
        }

        user = new ApiJSONObject(
                username,
                password
        );

        ApiController.createField(user, GlobalValues.USERS_URL, fragment, null);
    }

    @Override
    public void onResponse(String code) {
        if (getContext() == null) {
            Log.wtf("DEBUG", "the current fragment on the screen and the fragment where the onResponse request is called from are not the same, skipping all fields inside onResponse");
            return;
        }

        if (code.equals(this.getString(R.string.getData))){
            swapFragment();
        }
        else {
            Log.wtf("ERROR", "onResponse crashed at DashboardFragment with code " + code);
            super.onResponse("Error");
        }
    }

    /**
     * @apiNote when the account is created this method gets called thus sending the user and the projects menu
     */
    private void swapFragment(){
        if (user == null){
            Message.message(requireContext(), "something went wrong with saving the user data to the device");
            return;
        }
        UserData.saveUser(requireContext(), user);

        Bundle bundle = new Bundle();
        ProjectsFragment projectsFragment = new ProjectsFragment();
        projectsFragment.setArguments(bundle);

        FragmentManager myFragmentManager = getParentFragmentManager();

        FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.navHostFragment, projectsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
