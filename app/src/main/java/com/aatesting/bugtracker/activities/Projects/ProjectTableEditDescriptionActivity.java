package com.aatesting.bugtracker.activities.Projects;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;

public class ProjectTableEditDescriptionActivity extends AppCompatActivity {
    private EditText descriptionEdt;
    private ImageButton closeActivityBtn;
    private TextView topSave;
    private String descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectcreate_editdescription);

        String oldDescription;
        try{
            oldDescription = getIntent().getExtras().getString("oldData");
        } catch (Exception e){
            Log.wtf("ERROR", "failed to get oldDescription");
            Message.defErrMessage(this);
            oldDescription = "ERROR";
        }
        descriptionEdt = findViewById(R.id.descriptionEdt);
        closeActivityBtn = findViewById(R.id.closeActivityBtn);
        topSave = findViewById(R.id.topSave);

        descriptionEdt.setText(oldDescription);

        Listeners();
    }

    private void Listeners(){
        closeActivityBtn.setOnClickListener(v -> finish());
        topSave.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("newData", descriptionText);
            setResult(RESULT_OK, intent);
            finish();
        });
        descriptionEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                descriptionText = s.toString();
            }
        });
    }
}
