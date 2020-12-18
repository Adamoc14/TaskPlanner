// Packages and Imports
package com.example.listplanner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = new DB(MainActivity.this);
        tasks = myDB.getAllTasks();
        mainScreenInit();
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
      btnAddNewTask = (FloatingActionButton) findViewById(R.id.btnAddNewTask);
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