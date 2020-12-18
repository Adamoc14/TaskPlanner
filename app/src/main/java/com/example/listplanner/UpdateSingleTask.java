package com.example.listplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import DB.DB;
import Models.Task;

public class UpdateSingleTask extends AppCompatActivity {
    EditText txtEditSingleTitle , txtEditSingleDesc , txtEditSingleDueDate;
    Spinner spSinglePriority;
    Button btnUpdateSingleTask;
    FloatingActionButton btnClose;
    //Setting up the date in a nice readable format
    Calendar editCalendar;
    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d, yyyy");
    DB myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_single_task);
        myDB = new DB(UpdateSingleTask.this);
        updateSinglePageInit();

        //Get the info of the task selected and pre-populate the form fields
        int task_id = getIntent().getExtras().getInt("Task_ID");

        // Get back the task with the respective id to display it's values in the screen
        Task foundTask = myDB.getTask(task_id);
        if(foundTask == null) return;
        else {

            //Have to get the array list inside in the spinner to be able to populate it as .setPosition needs an index and
            // otherwise i have no way of getting the dynamic position of the priority picked so convert to array and findIndex of
            //priority string beforehand then set it
            String[] priorities = getResources().getStringArray(R.array.priority_list);
            spSinglePriority.setSelection(Arrays.asList(priorities).indexOf(String.valueOf(foundTask.getPriority())));
            txtEditSingleTitle.setText(foundTask.getTitle());
            txtEditSingleDesc.setText(foundTask.getDescription());
            txtEditSingleDueDate.setText(foundTask.getdueDate());

            txtEditSingleDueDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleDatePicker();
                }
            });

            btnUpdateSingleTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Calling the DB to Add our updated Task
                    Task updatedTask = new Task(spSinglePriority.getSelectedItemPosition(), txtEditSingleTitle.getText().toString(), txtEditSingleDesc.getText().toString(), formatter.format(new Date()) , txtEditSingleDueDate.getText().toString());
                    myDB.updateTask(updatedTask , foundTask.getId());
                    //TODO: Error Handling


                    // Redirecting the page after updating Task in the DB
                    Intent mainScreenIntent = new Intent(UpdateSingleTask.this, MainActivity.class);
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

    }

    public void handleDatePicker(){
        editCalendar  = Calendar.getInstance();
        new DatePickerDialog(UpdateSingleTask.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                editCalendar.set(Calendar.YEAR , year);
                editCalendar.set(Calendar.MONTH , month);
                editCalendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);
                txtEditSingleDueDate.setText(formatter.format(editCalendar.getTime()));
            }
        }, editCalendar.get(Calendar.YEAR), editCalendar.get(Calendar.MONTH), editCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void backToMainScreen(){
        Intent backToMainScreen = new Intent(UpdateSingleTask.this , MainActivity.class);
        startActivity(backToMainScreen);
        finish();
    }

    public void updateSinglePageInit(){
        txtEditSingleDesc = (EditText) findViewById(R.id.txtEditSingleDesc);
        txtEditSingleTitle = (EditText) findViewById(R.id.txtEditSingleTitle);
        txtEditSingleDueDate = (EditText) findViewById(R.id.txtEditDueDateReal);
        spSinglePriority = (Spinner) findViewById(R.id.spEditSinglePriority);
        btnUpdateSingleTask = (Button) findViewById(R.id.btnUpdateTask);
        btnClose = (FloatingActionButton) findViewById(R.id.btnCloseUpdate);
    }
}