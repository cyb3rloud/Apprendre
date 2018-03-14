package com.alanhughjones.studyeasy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SubjectListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);
    }

    /** Called when user clicks + button **/
    public void openAddSubject(View view) {
        // Open subject 'add activity' when button is clicked
        Intent intent = new Intent(this, SubjectAddActivity.class);
        startActivity(intent);
    }
}
