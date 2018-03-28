package com.alanhughjones.studyeasy;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TaskOverviewActivity extends AppCompatActivity {

    private static final String TAG = "TaskOverviewActivity";

    private TextView subjectTitle;
    private Button btnAddTask;
    private ListView taskListView;

    DatabaseHelper myDB;

    private String selectedSubject;
    private int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_overview);

        taskListView = findViewById(R.id.task_list);
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

        populateTaskList(selectedID,selectedSubject);
    }

    private void populateTaskList(int currentID, String currentSubject){
        String sub_id = Integer.toString(currentID);
        Log.d(TAG, "populateTaskList: Displaying tasks in the ListView");
        Cursor tasks = myDB.getTaskData(currentID);

        String rowCount = Integer.toString(tasks.getCount());

        if (tasks.getCount() == 0){
            Toast.makeText(this, sub_id, Toast.LENGTH_SHORT).show();
        } else {
            /*
            ArrayList<String> listTasks = new ArrayList<>();
            while (tasks.moveToNext()) {
                listTasks.add(tasks.getString(1));
                ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, listTasks);
                taskListView.setAdapter(adapter);
            */
            Toast.makeText(this, "Number of rows: " + rowCount, Toast.LENGTH_SHORT).show();

            //}
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
