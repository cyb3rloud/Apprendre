package com.alanhughjones.studyeasy;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity {

    DatabaseHelper myDB;

    int year_x,month_x,day_x;
    private String fakeTime = ":00.000";
    private String taskDate = "";
    private String showDate = "";
    private String fullTime = "";
    private EditText taskName;
    private int selectedID;
    private String selectedName;
    DatePickerDialog picker;
    private EditText showNewDate;
    private Button timePick;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Name = "idKey";
    //public static final String Email = "actualID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        myDB = new DatabaseHelper(this);
        Button addTaskDone = findViewById(R.id.add_task_done);
        taskName = findViewById(R.id.input_task);
        Button datePick = findViewById(R.id.datepick_btn);
        showNewDate = findViewById(R.id.show_new_date);
        timePick = findViewById(R.id.timepick_btn);
        final EditText showTime = findViewById(R.id.show_new_time);

        // get the intent extra from the SubjectAddActivity (the id of the subject)
        Intent receivedIntent = getIntent();
        selectedName = receivedIntent.getStringExtra("name");
        //now get the subjectID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1);

        addTaskDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTask = taskName.getText().toString();
                if(newTask.length() != 0 && taskDate.length() != 0 && fullTime.length() !=0){
                    taskDate = taskDate + " " + fullTime + fakeTime;
                    addTask(taskDate,newTask,selectedID);
                    Intent showAllTasks = new Intent(NewTaskActivity.this,TaskOverviewActivity.class);
                    showAllTasks.putExtra("id",selectedID);
                    showAllTasks.putExtra("name",selectedName);
                    createReminder(selectedName);
                    increaseNotifID();
                    startActivity(showAllTasks);
                } else {
                    Toast.makeText(NewTaskActivity.this,"Please fill all fields",Toast.LENGTH_LONG).show();
                }
            }
        });

        timePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(NewTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String sSelectedHour;
                        String sSelectedMinute;

                        if (selectedHour<10){
                            sSelectedHour = "0" + selectedHour;
                        } else {
                            sSelectedHour = Integer.toString(selectedHour);
                        }

                        if (selectedMinute<10){
                            sSelectedMinute = "0" + selectedMinute;
                        } else {
                            sSelectedMinute = Integer.toString(selectedMinute);
                        }
                        fullTime = sSelectedHour + ":" + sSelectedMinute;
                        showTime.setText(fullTime);
                    }
                }, hour, minute, true);
                timePicker.show();
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
                                showNewDate.setHint(showDate);
                            }
                        },year_x,month_x,day_x);
                picker.show();
            }
        });

    }

    private void increaseNotifID() {

    }

    public void createReminder(String subjectName){
        // alarmService (at this particular time, do this for me...)
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra("subject",subjectName);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //TODO get time set from Picker and use for notification
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,30);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),broadcast);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Name)){
            notificationIntent.putExtra("notifID",sharedpreferences.getString(Name,""));
        }
    }

    public void addTask(String newTaskDate, String newTaskDesc, int subjectID){
        boolean insertData = myDB.insertTask(newTaskDate,newTaskDesc,subjectID);
        if(insertData)
            Toast.makeText(NewTaskActivity.this,"Task Inserted",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(NewTaskActivity.this,"Task not Inserted",Toast.LENGTH_LONG).show();
    }
}
