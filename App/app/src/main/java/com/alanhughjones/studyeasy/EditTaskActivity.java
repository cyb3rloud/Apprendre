package com.alanhughjones.studyeasy;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private String selectedSubName;
    private Button deleteTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        myDB = new DatabaseHelper(this);
        editTaskDone = findViewById(R.id.edit_task_done);
        taskName = findViewById(R.id.edit_task_input);
        deleteTask = findViewById(R.id.delete_task);

        // get the intent extra from the SubjectAddActivity (the id of the subject)
        Intent receivedIntent = getIntent();
        // get taskId passed as a String
        selectedTaskId = receivedIntent.getStringExtra("taskID");
        //now get the subjectID we passed as an extra
        selectedSubID = receivedIntent.getIntExtra("subID", -1);
        //get name of subject for when moving back to overview
        selectedSubName = receivedIntent.getStringExtra("name");

        // Set task input field to contain task title and date to task date
        Cursor editTask = myDB.getSingleTask(selectedTaskId);
        if (editTask.moveToFirst()) {
            taskName.setText(editTask.getString(0));
            taskDate = editTask.getString(1);
        }

        // Set onclick listener to delete button
        deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDB.deleteTask(selectedTaskId);
                Toast.makeText(EditTaskActivity.this, "Task Deleted", Toast.LENGTH_LONG).show();
                Intent backAllTasks = new Intent(EditTaskActivity.this, TaskOverviewActivity.class);
                backAllTasks.putExtra("id", selectedSubID);
                backAllTasks.putExtra("name", selectedSubName);
                startActivity(backAllTasks);
            }
        });


        //Set up calendar to show date of task
        final Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        if (taskDate.length() > 0) {
            try {
                date = sdf.parse(taskDate);
                cal.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        showDialogOnInputClick(taskDate);
    }

    public void showDialogOnInputClick(String taskDate) {
        datePick = findViewById(R.id.edit_datepick_btn);

        datePick.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_ID);
                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DIALOG_ID)
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear+1;
            day_x = dayOfMonth;
            taskDate = day_x + "-" + month_x + "-" + year_x;
            Toast.makeText(EditTaskActivity.this, day_x + "/" + month_x + "/" + year_x, Toast.LENGTH_SHORT).show();
        }
    };
}
