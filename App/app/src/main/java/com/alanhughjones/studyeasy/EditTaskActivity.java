package com.alanhughjones.studyeasy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {

    DatabaseHelper myDB;

    private Button editTaskDone;
    int year_x,month_x,day_x;
    private String taskDate = "";
    private String showDate = "";
    private EditText taskName;
    private int selectedSubID;
    private String selectedTaskId;
    private String selectedSubName;
    DatePickerDialog picker;
    private String fakeTime = " 00:00:00.000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        myDB = new DatabaseHelper(this);
        editTaskDone = findViewById(R.id.edit_task_done);
        final EditText dueDateShow = findViewById(R.id.show_due_date);
        taskName = findViewById(R.id.edit_task_input);
        Button deleteTask = findViewById(R.id.delete_task);
        Button datePick = findViewById(R.id.edit_datepick_btn);

        UpdateTask();
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
            // TODO split task date (format YYYY-MM-DD HH:MM:SS.SSS) into year month and day to arrange as DD-MM-YYYY
            dueDateShow.setText(taskDate);
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

        datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                day_x = cldr.get(Calendar.DAY_OF_MONTH);
                month_x = cldr.get(Calendar.MONTH);
                year_x = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(EditTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener(){
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                                String sDayOfMonth;
                                String sMonthOfYear;
                                if (dayOfMonth<10){
                                    sDayOfMonth = "0" + dayOfMonth;
                                } else {
                                    sDayOfMonth = Integer.toString(dayOfMonth);
                                }
                                if (monthOfYear<10){
                                    sMonthOfYear = "0" + (monthOfYear + 1);
                                } else {
                                    sMonthOfYear = Integer.toString(monthOfYear+1);
                                }
                                showDate = sDayOfMonth + "-" + sMonthOfYear + "-" + year;
                                taskDate = year + "-" + sMonthOfYear + "-" + sDayOfMonth;
                                dueDateShow.setText(showDate);
                            }
                        },year_x,month_x,day_x);
                picker.show();
            }
        });


    }

    public void UpdateTask(){
        editTaskDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedDesc = taskName.getText().toString();
                String sSubId = Integer.toString(selectedSubID);
                if(updatedDesc.length() != 0 && taskDate.length() != 0) {
                    boolean isUpdated = myDB.updateTask(sSubId, selectedTaskId, updatedDesc, taskDate);
                    if(isUpdated){
                        Toast.makeText(EditTaskActivity.this, "Task Updated", Toast.LENGTH_LONG).show();
                        Intent showAllTasks = new Intent(EditTaskActivity.this,TaskOverviewActivity.class);
                        showAllTasks.putExtra("id",selectedSubID);
                        showAllTasks.putExtra("name",selectedSubName);
                        startActivity(showAllTasks);
                    } else {
                        Toast.makeText(EditTaskActivity.this, "Task not updated", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
