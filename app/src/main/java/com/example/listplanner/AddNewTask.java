//Imports and Packages
package com.example.listplanner;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import DB.DB;
import Models.Task;

// Class Declaration
public class AddNewTask extends AppCompatActivity {
    // Variable Declarations and Function Definitions
    EditText txtEditTitle, txtEditDesc;
    Spinner spPriority;
    Button btnAddNewTask;
    FloatingActionButton btnClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_task);

        addTaskPageInit();
        btnAddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setting up the date in a nice readable format
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d, yyyy");

                // Calling the DB to Add our new Task
                DB myDB = new DB(AddNewTask.this);
                Task newTask = new Task(spPriority.getSelectedItemPosition(), txtEditTitle.getText().toString() , txtEditDesc.getText().toString(), formatter.format(new Date()));
                myDB.insertTask(newTask);
                //TODO: Error Handling


                // Redirecting the page after inserting new Task to the DB
                Intent mainScreenIntent = new Intent(AddNewTask.this , MainActivity.class);
                startActivity(mainScreenIntent);
                finish();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMainScreen();
            }
        });


    }

    public void backToMainScreen(){
        Intent backToMainScreen = new Intent(AddNewTask.this , MainActivity.class);
        startActivity(backToMainScreen);
        finish();
    }

    public void addTaskPageInit(){
        txtEditTitle = (EditText) findViewById(R.id.txtEditSingleTitle);
        txtEditDesc = (EditText) findViewById(R.id.txtEditSingleDesc);
        spPriority = (Spinner) findViewById(R.id.spEditSinglePriority);
        btnAddNewTask = (Button) findViewById(R.id.btnUpdateTask);
        btnClose = (FloatingActionButton) findViewById(R.id.btnCloseAdd);
    }
}
