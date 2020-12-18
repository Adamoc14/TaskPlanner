//Imports and Packages
package com.example.listplanner;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import DB.DB;
import Models.Task;

// Class Declaration
public class AddNewTask extends AppCompatActivity {
    // Variable Declarations and Function Definitions
    EditText txtEditTitle, txtEditDesc, txtEditDueDate;
    Spinner spPriority;
    Button btnAddNewTask;
    //Setting up the date in a nice readable format
    Calendar editCalendar;
    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d, yyyy");
    FloatingActionButton btnClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_task);

        addTaskPageInit();
        txtEditDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDatePicker();
            }
        });
        btnAddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Calling the DB to Add our new Task
                DB myDB = new DB(AddNewTask.this);
                Task newTask = new Task(spPriority.getSelectedItemPosition(), txtEditTitle.getText().toString() , txtEditDesc.getText().toString(), formatter.format(new Date()) , txtEditDueDate.getText().toString());
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

    public void handleDatePicker(){
        editCalendar  = Calendar.getInstance();
        new DatePickerDialog(AddNewTask.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                editCalendar.set(Calendar.YEAR , year);
                editCalendar.set(Calendar.MONTH , month);
                editCalendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);
                txtEditDueDate.setText(formatter.format(editCalendar.getTime()));
            }
        }, editCalendar.get(Calendar.YEAR), editCalendar.get(Calendar.MONTH), editCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void backToMainScreen(){
        Intent backToMainScreen = new Intent(AddNewTask.this , MainActivity.class);
        startActivity(backToMainScreen);
        finish();
    }

    public void addTaskPageInit(){
        txtEditTitle = (EditText) findViewById(R.id.txtEditSingleTitle);
        txtEditDesc = (EditText) findViewById(R.id.txtEditSingleDesc);
        txtEditDueDate = (EditText) findViewById(R.id.txtEditDueDate);
        spPriority = (Spinner) findViewById(R.id.spEditSinglePriority);
        btnAddNewTask = (Button) findViewById(R.id.btnUpdateTask);
        btnClose = (FloatingActionButton) findViewById(R.id.btnCloseAdd);
    }
}
