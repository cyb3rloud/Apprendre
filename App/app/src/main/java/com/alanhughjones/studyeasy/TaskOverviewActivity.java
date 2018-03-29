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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

        populateTaskList(selectedID);
    }

    private void populateTaskList(int currentID){
        String sub_id = Integer.toString(currentID);
        Log.d(TAG, "populateTaskList: Displaying tasks in the ListView");

        Cursor tasks = myDB.getTaskData(currentID);
        taskListView = findViewById(R.id.task_list);


        // for testing only (toast)
        String rowCount = Integer.toString(tasks.getCount());

        if (tasks.getCount() == 0){
            Toast.makeText(this, "Number of rows: " + rowCount, Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "Number of rows: " + rowCount, Toast.LENGTH_LONG).show();

            // Try using a HashMap
            HashMap<String, String> taskDetails = new HashMap<>();
            while (tasks.moveToNext()){
                taskDetails.put(tasks.getString(0),tasks.getString(1));
            }

            List<HashMap<String, String>> taskItems = new ArrayList<>();
            SimpleAdapter adapter = new SimpleAdapter(this, taskItems, R.layout.task_item,
                    new String[]{"First Line", "Second Line"},
                    new int[]{R.id.text1,R.id.text2});

            Iterator it = taskDetails.entrySet().iterator();
            while (it.hasNext()){
                HashMap<String, String> resultMap = new HashMap<>();
                Map.Entry pair = (Map.Entry)it.next();
                resultMap.put("First Line", pair.getKey().toString());
                resultMap.put("Second Line", pair.getValue().toString());
                taskItems.add(resultMap);
            }

            taskListView.setAdapter(adapter);
            /*
            ArrayList<String> listTasks = new ArrayList<>();
            while (tasks.moveToNext()) {
                listTasks.add(tasks.getString(0));
            }
            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, listTasks);
            taskListView.setAdapter(adapter);
            */
        }
    }


}
