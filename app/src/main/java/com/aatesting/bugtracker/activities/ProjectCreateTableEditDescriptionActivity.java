package com.aatesting.bugtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aatesting.bugtracker.R;

public class ProjectCreateTableEditDescriptionActivity extends AppCompatActivity {
    private EditText descriptionEdt;
    private ImageButton closeActivityBtn;
    private TextView topSave;
    private String descriptionText;
    private String oldDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_create_editdescription);

        oldDescription = getIntent().getExtras().getString("oldData");
        descriptionEdt = findViewById(R.id.descriptionEdt);
        closeActivityBtn = findViewById(R.id.closeActivityBtn);
        topSave = findViewById(R.id.topSave);

        descriptionEdt.setText(oldDescription);

        Listeners();
    }

    private void Listeners(){
        closeActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("newData", descriptionText);
                setResult(RESULT_OK, intent);
                finish();
            }
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
