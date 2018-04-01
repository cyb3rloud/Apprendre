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

import java.util.ArrayList;
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

    DatabaseHelper myDB;

    private String selectedSubject;
    private int selectedID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        //Add sample data for list
        //mProductList.add(new Task(1, "GermanTest1", "2018-4-20"));
        //mProductList.add(new Task(2, "GermanTest2", "2018-4-21"));
        //mProductList.add(new Task(3, "GermanTest3", "2018-4-22"));
        //mProductList.add(new Task(4, "GermanTest4", "2018-4-23"));


        // mProductList.add(new Task(1, TASK_DESC, TASK_DATE)

        Cursor tasks = myDB.getTaskData(selectedID);

        if (tasks.getCount() == 0){
            Toast.makeText(this, "No data", Toast.LENGTH_LONG).show();
        } else {

            while (tasks.moveToNext()){
                Toast.makeText(this, "ID: "+ tasks.getString(2), Toast.LENGTH_LONG).show();
               mProductList.add(new Task(tasks.getInt(0),tasks.getString(1),tasks.getString(2)));
                //mProductList.add(new Task(1, "GermanTest1", "2018-4-20"));
                //mProductList.add(new Task(2, "GermanTest2", "2018-4-21"));
                //mProductList.add(new Task(3, "GermanTest3", "2018-4-22"));
                //mProductList.add(new Task(4, "GermanTest4", "2018-4-23"));
            }
        }


        //Init adapter
        adapter = new TaskListAdapter(getApplicationContext(), mProductList);
        taskListView.setAdapter(adapter);
    }
}
