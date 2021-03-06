// Packages and Imports
package com.example.listplanner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import Adapters.TaskAdapter;
import DB.DB;
import Models.Task;

// Class Declaration
public class MainActivity extends AppCompatActivity {

    // Variable Declaration and Function Definitions
    public TaskAdapter taskAdapter;
    public DB myDB = null;
    public List<Task> tasks = new ArrayList<>();
    public RecyclerView recyclerView;
    public FloatingActionButton btnAddNewTask;
    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = new DB(MainActivity.this);
        tasks = myDB.getAllTasks();
        mainScreenInit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.toString().equals("Missed Tasks")){
                    Intent gotoMissedTasks = new Intent(MainActivity.this , MissedTasks.class);
                    startActivity(gotoMissedTasks);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this , "You are on the home screen" , Toast.LENGTH_LONG).show();
                    return false;
                }

                return false;
            }
        });
        btnAddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewTaskIntent = new Intent(MainActivity.this, AddNewTask.class );
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
      taskAdapter = new TaskAdapter(MainActivity.this , tasks);
      // Layout the recycler view as desired
      structureLayout();
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