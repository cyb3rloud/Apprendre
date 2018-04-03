package com.alanhughjones.studyeasy;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditTaskActivity extends AppCompatActivity {

    private static final String TAG = "EditTaskActivity";
    DatabaseHelper myDB;

    private Button datePick;
    private Button editTaskDone;
    int year_x,month_x,day_x;
    private String taskDate = "";
    private EditText taskName;
    static final int DIALOG_ID = 0;
    private int selectedSubID;
    private String selectedTaskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        myDB = new DatabaseHelper(this);
        editTaskDone = findViewById(R.id.edit_task_done);
        taskName = findViewById(R.id.edit_task_input);

        // get the intent extra from the SubjectAddActivity (the id of the subject)
        Intent receivedIntent = getIntent();
        // get taskId passed as a String
        selectedTaskId = receivedIntent.getStringExtra("taskID");
        //now get the subjectID we passed as an extra
        selectedSubID = receivedIntent.getIntExtra("subID",-1);

        // Set task input field to contain task title
        //taskName.setText(selectedTaskId);

        Cursor editTask = myDB.getSingleTask(selectedTaskId);

        if (editTask.moveToFirst()){
            taskName.setText(editTask.getString(0));
            taskDate = editTask.getString(1);
        }
    }
}
