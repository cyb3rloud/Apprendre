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

    //Spinner spinner;
    //ArrayAdapter<CharSequence> adapter; //need to use cursorAdapter for database

    private Button datePick;
    private Button addTaskDone;
    int year_x,month_x,day_x;
    private String taskDate;
    private EditText taskName;
    static final int DIALOG_ID = 0;
    private int selectedID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        myDB = new DatabaseHelper(this);
        addTaskDone = findViewById(R.id.add_task_done);
        taskName = findViewById(R.id.input_task);


        // get the intent extra from the SubjectAddActivity (the id of the subject)
        Intent receivedIntent = getIntent();

        //now get the subjectID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1);

        addTaskDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTask = taskName.getText().toString();
                if(newTask.length() != 0 && taskDate.length() != 0){
                    addTask(newTask,taskDate,selectedID);
                    Intent intent = new Intent(NewTaskActivity.this,TaskOverviewActivity.class);
                    //Toast.makeText(NewTaskActivity.this,"Yes",Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(NewTaskActivity.this,"No",Toast.LENGTH_LONG).show();
                }
            }
        });

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        showDialogOnInputClick();
    }

    public void showDialogOnInputClick() {
       datePick = findViewById(R.id.datepick_btn);

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
            taskDate = year_x + "-" + month_x + "-" + day_x;
            Toast.makeText(NewTaskActivity.this, day_x + "/" + month_x + "/" + year_x, Toast.LENGTH_SHORT).show();
        }
    };

    public void addTask(String newTaskDate, String newTaskDesc, int subjectID){
        boolean insertData = myDB.insertTask(newTaskDate,newTaskDesc,subjectID);
        if(insertData)
            Toast.makeText(NewTaskActivity.this,"Task Inserted",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(NewTaskActivity.this,"Task not Inserted",Toast.LENGTH_LONG).show();
    }
}
