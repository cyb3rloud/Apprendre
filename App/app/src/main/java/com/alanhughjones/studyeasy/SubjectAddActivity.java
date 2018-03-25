package com.alanhughjones.studyeasy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubjectAddActivity extends AppCompatActivity {

    private static final String TAG = "SubjectAddActivity";

    DatabaseHelper myDB;
    private EditText editSubject;
    private Button btnAddSubj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_add);
        myDB = new DatabaseHelper(this);

        editSubject = findViewById(R.id.input_subj);
        btnAddSubj = findViewById(R.id.subj_done);

        btnAddSubj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newSubject = editSubject.getText().toString();
                if (editSubject.length() != 0){
                    AddData(newSubject);
                    editSubject.setText("");
                    Intent intent = new Intent(SubjectAddActivity.this,SubjectListActivity.class);
                    startActivity(intent);

                } else {
                    toastMessage("You must enter a subject name!");
                }
            }
        });
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void AddData(String subjEntry){
        boolean insertData = myDB.insertSubject(subjEntry);
        if(insertData)
            Toast.makeText(SubjectAddActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(SubjectAddActivity.this,"Data not Inserted",Toast.LENGTH_LONG).show();
    }


}
