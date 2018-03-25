package com.alanhughjones.studyeasy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SubjectAddActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    EditText editSubject;
    Button btnAddSubj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_add);
        myDB = new DatabaseHelper(this);

    }
}
