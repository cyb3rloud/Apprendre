package com.alanhughjones.studyeasy;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SubjectListActivity extends AppCompatActivity {

    private static final String TAG = "SubjectListActivity";
    DatabaseHelper myDB;
    private ListView subjectListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);
        subjectListView = findViewById(R.id.subj_list);
        myDB = new DatabaseHelper(this);

        populateSubjectList();
    }

    private void populateSubjectList() {
        Log.d(TAG, "populateSubjectList: Displaying subjects in the ListView.");
        Cursor subjects = myDB.getAllData();
        ArrayList<String> listSubjects = new ArrayList<>();
        while (subjects.moveToNext()){
            listSubjects.add(subjects.getString(1));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listSubjects);
        subjectListView.setAdapter(adapter);
    }

    /** Called when user clicks + button **/
    public void openAddSubject(View view) {
        // Open subject 'add activity' when button is clicked
        Intent intent = new Intent(this, SubjectAddActivity.class); // change back to SubjectAddActivity
        startActivity(intent);
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
