package com.alanhughjones.studyeasy;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TaskOverviewActivity extends Activity {

    private TextView subjectTitle;
    private ListView taskListView;
    private TaskListAdapter adapter;
    private List<Task> mProductList;
    private Button btnAddTask;
    private TextView overDue;
    private TextView notOverdue;
    DatabaseHelper myDB;
    public int taskCnt;
    public int taskCntO;

    private String selectedSubject;
    private int selectedID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_overview);
        subjectTitle = findViewById(R.id.overview_subj);
        btnAddTask = findViewById(R.id.add_task_done);
        overDue = findViewById(R.id.overdue_count);
        notOverdue = findViewById(R.id.task_count);
        myDB = new DatabaseHelper(this);
        taskCnt = 0;
        taskCntO = 0;

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newTask = new Intent(TaskOverviewActivity.this,NewTaskActivity.class);
                newTask.putExtra("id",selectedID);
                newTask.putExtra("name",selectedSubject);
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

        taskListView = (ListView)findViewById(R.id.task_list);

        mProductList = new ArrayList<>();


        Cursor tasks = myDB.getTaskData(selectedID);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        if (tasks.getCount() == 0){
            Toast.makeText(this, "No data", Toast.LENGTH_LONG).show();
        } else {
            while (tasks.moveToNext()) {
                mProductList.add(new Task(tasks.getInt(0), tasks.getString(1), tasks.getString(2)));
                Date strDate;
                try {
                    strDate = sdf.parse(tasks.getString(2));
                    if (new Date().after(strDate)){
                        Toast.makeText(this, "Overdue", Toast.LENGTH_LONG).show();
                        taskCntO += 1;
                    } else {
                        Toast.makeText(this, "Upcoming", Toast.LENGTH_LONG).show();
                        taskCnt +=1;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            //Set count total
            overDue.setText(Integer.toString(taskCntO));
            notOverdue.setText(Integer.toString(taskCnt));

        }

        //Set count total
        //overDue.setText(taskCntO);
        //notOverdue.setText(taskCnt);

        //Init adapter
        adapter = new TaskListAdapter(getApplicationContext(), mProductList);
        taskListView.setAdapter(adapter);
    }
}
