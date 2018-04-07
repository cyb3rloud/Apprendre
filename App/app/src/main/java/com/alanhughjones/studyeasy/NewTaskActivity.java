package com.alanhughjones.studyeasy;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
//import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity {

    private static final String TAG = "NewTaskActivity";
    DatabaseHelper myDB;


    private Button datePick;
    private Button addTaskDone;
    int year_x,month_x,day_x;
    private String taskDate = "";
    private EditText taskName;
    static final int DIALOG_ID = 0;
    private int selectedID;
    private String selectedName;
    DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        myDB = new DatabaseHelper(this);
        addTaskDone = findViewById(R.id.add_task_done);
        taskName = findViewById(R.id.input_task);
        Button datePick = findViewById(R.id.datepick_btn);

        // get the intent extra from the SubjectAddActivity (the id of the subject)
        Intent receivedIntent = getIntent();
        selectedName = receivedIntent.getStringExtra("name");
        //now get the subjectID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1);

        addTaskDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTask = taskName.getText().toString();
                if(newTask.length() != 0 && taskDate.length() != 0){
                    addTask(taskDate,newTask,selectedID);
                    Intent showAllTasks = new Intent(NewTaskActivity.this,TaskOverviewActivity.class);
                    showAllTasks.putExtra("id",selectedID);
                    showAllTasks.putExtra("name",selectedName);
                    startActivity(showAllTasks);
                } else {
                    Toast.makeText(NewTaskActivity.this,"Please enter a date",Toast.LENGTH_LONG).show();
                }
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
                picker = new DatePickerDialog(NewTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener(){
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                                taskDate = dayOfMonth + "-" + (monthOfYear+1) + "-" + year;
                                //dueDateShow.setText(taskDate);
                            }
                        },year_x,month_x,day_x);
                picker.show();
            }
        });
    }


    public void addTask(String newTaskDate, String newTaskDesc, int subjectID){
        boolean insertData = myDB.insertTask(newTaskDate,newTaskDesc,subjectID);
        if(insertData)
            Toast.makeText(NewTaskActivity.this,"Task Inserted",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(NewTaskActivity.this,"Task not Inserted",Toast.LENGTH_LONG).show();
    }
}
