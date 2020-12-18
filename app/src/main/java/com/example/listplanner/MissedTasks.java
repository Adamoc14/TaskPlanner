package com.example.listplanner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import Adapters.TaskAdapter;
import DB.DB;
import Models.Task;

public class MissedTasks extends AppCompatActivity {

    // Variable Declaration and Function Definitions
    public TaskAdapter taskAdapter;
    public DB myDB = null;
    public List<Task> tasks = new ArrayList<>();
    public RecyclerView recyclerView;
    public FloatingActionButton btnAddNewTask;
    public BottomNavigationView bottomNavigationView;
    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d, yyyy");

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = new DB(MissedTasks.this);
        tasks = myDB.getAllTasks();
        tasks = tasks.stream()
                .filter((Task task) -> {
                    Date strDate = null;
                    try {
                        strDate = formatter.parse(task.getdueDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return new Date().after(strDate);
                })
                .collect(Collectors.toList());
        mainScreenInit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.toString().equals("Missed Tasks")){
                    Toast.makeText(MissedTasks.this , "You are on the missed tasks screen" , Toast.LENGTH_LONG).show();
                } else {
                    backToMainScreen();
                }

                return false;
            }
        });
        btnAddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewTaskIntent = new Intent(MissedTasks.this, AddNewTask.class );
                startActivity(addNewTaskIntent);
                finish();
            }
        });
    }

    public void mainScreenInit(){
        recyclerView = (RecyclerView) findViewById(R.id.rycViewTask);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        btnAddNewTask = (FloatingActionButton) findViewById(R.id.btnAddNewTask);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setItemIconSize(170);
        taskAdapter = new TaskAdapter(MissedTasks.this , tasks);
        // Layout the recycler view as desired
        structureLayout();
    }

    public void backToMainScreen(){
        Intent backToMainScreen = new Intent(MissedTasks.this , MainActivity.class);
        startActivity(backToMainScreen);
        finish();
    }

    public void structureLayout(){
        GridLayoutManager layoutManager = new GridLayoutManager(this , 2 , GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        // Setting up a class just to provide a little space in between the items in the recycler view
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10) , true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(taskAdapter);
    }
}