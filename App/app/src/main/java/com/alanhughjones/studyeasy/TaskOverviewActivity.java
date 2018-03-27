package com.alanhughjones.studyeasy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TaskOverviewActivity extends AppCompatActivity {

    private static final String TAG = "TaskOverviewActivity";

    private TextView subjectTitle;
    private Button btnAddTask;

    DatabaseHelper myDB;
    // testing commit branch
    private String selectedSubject;
    private int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_overview);

        subjectTitle = findViewById(R.id.overview_subj);
        btnAddTask = findViewById(R.id.add_task_done);
        myDB = new DatabaseHelper(this);

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newTask = new Intent(TaskOverviewActivity.this,NewTaskActivity.class);
                newTask.putExtra("id",selectedID);
                startActivity(newTask);
            }
        });

        // get the intent extra from the SubjectListActivity
        Intent receivedIntent = getIntent();
        //now get the subjectID we passed as an extra
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
