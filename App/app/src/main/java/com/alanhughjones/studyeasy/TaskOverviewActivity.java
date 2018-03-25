package com.alanhughjones.studyeasy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class TaskOverviewActivity extends AppCompatActivity {

    private static final String TAG = "TaskOverviewActivity";

    private TextView subjectTitle;

    DatabaseHelper myDB;

    private String selectedSubject;
    private int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_overview);
        subjectTitle = findViewById(R.id.overview_subj);
        myDB = new DatabaseHelper(this);

        // get the intent extra from the SubjectListActivity
        Intent receivedIntent = getIntent();

        //now get the subjectID we passed as and extra
        selectedID = receivedIntent.getIntExtra("id",-1);

        //now get the name we passed as an extra
        selectedSubject = receivedIntent.getStringExtra("name");

        //set the text to show the current selected subject
        subjectTitle.setText(selectedSubject);
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
