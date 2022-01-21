package com.aatesting.bugtracker.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.MainActivity;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiJSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignInFragment extends ModifiedFragment {
    public int reminderSelected = -1;
    public int reminderTypeSelected = -1;
    public int repeatSelected = -1;

    private ApiJSONObject user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_signin, container, false);

        ((MainActivity)getActivity()).fixBottomMenu();

        signInListeners(root);
        return root;
    }

    private void signInListeners(View root) {
        Handler handler = new Handler();
        View mainBtn = ((MainActivity)getActivity()).mainBtn;

        mainBtn.setVisibility(View.GONE);

        TextView signUpText = root.findViewById(R.id.signInText);
        EditText usernameEdt = root.findViewById(R.id.usernameEdt);
        EditText passwordEdt = root.findViewById(R.id.passwordEdt);
        Button submit = root.findViewById(R.id.submit);

        ModifiedFragment fragment = this;

        usernameEdt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            passwordEdt.requestFocus();
                        }
                    }, 50);

                    passwordEdt.requestFocus();
                    return true;
                }
                return false;
            }
        });

        passwordEdt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            signIn(usernameEdt, passwordEdt, fragment);
                        }
                    }, 50);
                    return true;
                }
                return false;
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new fragment and transaction
                Bundle bundle = new Bundle();
                SignUpFragment signUpFragment = new SignUpFragment();
                signUpFragment.setArguments(bundle);

                FragmentManager myFragmentManager = getParentFragmentManager();

                FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.navHostFragment, signUpFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(usernameEdt, passwordEdt, fragment);
            }
        });

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(usernameEdt, passwordEdt, fragment);
            }
        });
    }

    private void signIn(EditText usernameEdt, EditText passwordEdt, ModifiedFragment fragment) {
        String username = usernameEdt.getText().toString();
        String password = passwordEdt.getText().toString();

        //username or password less than 3 is not allowed
        if (username.length() < 3 || password.length() < 3 ){
            Message.message(getContext(), "Username and password longer than 3 characters is required");
            return;
        }

        user = new ApiJSONObject(
                username,
                password
        );

        ApiController.getField(user, true, null,
                requireContext(), GlobalValues.USERS_URL, fragment);
    }

    @Override
    public void onResponse(String code) {
        if (getContext() == null) {
            Log.wtf("DEBUG", "the current fragment on the screen and the fragment where the onResponse request is called from are not the same, skipping all fields inside onResponse");
            return;
        }

        if (code.equals("gotUser")){
            swapFragment();
        }
        else {
            Log.wtf("ERROR", "onResponse crashed at DashboardFragment with code " + code);
            super.onResponse("Error");
        }
    }

    /**
     * @apiNote if the user login is success swap the fragment with the project fragment
     */
    private void swapFragment(){
        if (user == null){
            Message.message(requireContext(), "something went wrong with saving the user data to the device");
            return;
        }

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