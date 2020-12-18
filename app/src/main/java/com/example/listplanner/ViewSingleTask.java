package com.example.listplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.HashMap;

import DB.DB;
import Models.Task;

public class ViewSingleTask extends AppCompatActivity {
    TextView txtViewTitle , txtViewDescription , txtViewDate, txtViewPriority , txtViewSingleDueDate;
    Button btnUpdate;
    ImageButton btnDelete;
    Spinner spStatus;
    FloatingActionButton btnClose;
    DB myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_task);
        myDB = new DB(ViewSingleTask.this);
        startSingleTaskInit();

        //Getting the id from the intent which was passed from the all Tasks view
        int task_id = getIntent().getExtras().getInt("Task_ID");

        // Get back the task with the respective id to display it's values in the screen
        Task foundTask = myDB.getTask(task_id);
        if(foundTask == null) return;
        else {
            txtViewPriority.setBackgroundResource(checkTagColours(foundTask.getPriority()));
            txtViewTitle.setText(foundTask.getTitle());
            txtViewPriority.setText(String.valueOf(foundTask.getPriority()));
            txtViewDescription.setText(foundTask.getDescription());
            //Setting the spinner values to be of the enum status type of the task model, so user can update status
            spStatus.setAdapter(new ArrayAdapter<Task.StatusType>(this, android.R.layout.simple_spinner_item, Task.StatusType.values()));
            spStatus.setSelection(Arrays.asList(Task.StatusType.values()).indexOf(Enum.valueOf(Task.StatusType.class, foundTask.getStatus().toString())));
            txtViewDate.setText("Created at: " + foundTask.getDate());
            txtViewSingleDueDate.setText("Due Date: " + foundTask.getdueDate());

            spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Object item = parent.getSelectedItem();
                    checkStatus(Enum.valueOf(Task.StatusType.class, item.toString()) , foundTask);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // Brings the app to update page to update the respective task
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent updatePageIntent = new Intent(ViewSingleTask.this , UpdateSingleTask.class);
                    updatePageIntent.putExtra("Task_ID", foundTask.getId());
                    startActivity(updatePageIntent);
                    finish();
                }
            });

            // Deletes the task and goes back to the main screen
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDB.deleteTask(foundTask);
                    Toast.makeText(ViewSingleTask.this , "Task Deleted Successfully" , Toast.LENGTH_LONG).show();
                    backToMainScreen();
                }
            });

            // Goes bck to the main screen
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backToMainScreen();
                }
            });
        }
    }

    // Passing in my Enum and taking the necessary and appropriate action based on the result of the enum
    public void checkStatus(Task.StatusType status , Task foundTask){
        HashMap<Task.StatusType, Runnable> statuses = new HashMap<Task.StatusType , Runnable>(){{
            put(Task.StatusType.ToDo , new Runnable(){
                @Override
                public void run() {
                    return;
                }
            });
            put(Task.StatusType.InProgress , new Runnable(){
                @Override
                public void run() {
                    myDB.updateStatus(foundTask.getId() , String.valueOf(status));
                    return;
                }
            });
            put(Task.StatusType.Completed , new Runnable(){
                @Override
                public void run() {
                    myDB.deleteTask(foundTask);
                    Toast.makeText(ViewSingleTask.this , "Task Deleted Successfully" , Toast.LENGTH_LONG).show();
                    backToMainScreen();
                }
            });
        }};
        statuses.get(status).run();
    }

    public void backToMainScreen(){
        Intent backToMainScreen = new Intent(ViewSingleTask.this , MainActivity.class);
        startActivity(backToMainScreen);
        finish();
    }

    public void startSingleTaskInit(){
        txtViewTitle = (TextView) findViewById(R.id.txtViewSingleTitle);
        txtViewDate = (TextView) findViewById(R.id.txtViewSingleDate);
        txtViewDescription = (TextView) findViewById(R.id.txtViewSingleDescription);
        txtViewPriority = (TextView) findViewById(R.id.txtViewSinglePriorityTag);
        txtViewSingleDueDate = (TextView) findViewById(R.id.txtViewSingleDueDate);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnClose = (FloatingActionButton) findViewById(R.id.btnClose);
        btnDelete = (ImageButton) findViewById(R.id.btnDelete);
        spStatus = (Spinner) findViewById(R.id.spStatus);
    }

    // Helper function to identify the priority level and pass back the corresponding colour with that
    public int checkTagColours(int tag_priority) {
        if(tag_priority != 0){
            HashMap<Integer, Integer> colours = new HashMap<>();
            colours.put(1, R.color.priority1);
            colours.put(2, R.color.priority2);
            colours.put(3, R.color.priority3);
            colours.put(4, R.color.priority4);
            colours.put(5, R.color.priority5);
            for(int i : colours.keySet()){
                return colours.get(tag_priority);
            }
        }
        return 0;

    }
}