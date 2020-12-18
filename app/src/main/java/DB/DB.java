/**
 *
 * @author adamoc
 */

// Imports and Packages
package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Models.Task;

// Class Declaration
public class DB extends SQLiteOpenHelper{

    // Variable Declarations and Constructor Implementation
    // Setting up our DB
    private SQLiteDatabase db;
    private static final HashMap<String,String> db_data = new HashMap<String, String>(){{
        put("Database_Name" , "Task_DB");
        put("Table_Name" , "Tasks");
        put("Col_1" , "ID");
        put("Col_2" , "Priority");
        put("Col_3" , "Title");
        put("Col_4" , "Description");
        put("Col_5" , "Date_Created");
        put("Col_6" , "Status");
    }};
    private static final HashMap<String,String> db_commands = new HashMap<String, String>(){{
        put("Create_Cmd" , "CREATE TABLE IF NOT EXISTS " + db_data.get("Table_Name") + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , Priority INTEGER, Title Text, Description Text , Date_Created Text , Status Text)" );
        put("Drop_Cmd" , "DROP TABLE IF EXISTS" + db_data.get("Table_Name"));
    }};
    public List<Task> tasks = new ArrayList<Task>();

    //____________________Constructor__________________________

    public DB(@Nullable Context context) {
        super(context, db_data.get("Database_Name"), null, 1);
    }

    // Database Methods to set up the Database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(db_commands.get("Create_Cmd"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(db_commands.get("Drop_Cmd"));
        onCreate(db);
    }


    // CRUD operational Functions on the Tasks
    // Helper Method to fillAllContentValues
    public ContentValues fillContentValues(Task task){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(db_data.get("Col_2") , task.getPriority());
        values.put(db_data.get("Col_3"), task.getTitle());
        values.put(db_data.get("Col_4"), task.getDescription());
        values.put(db_data.get("Col_5"), task.getDate());
        values.put(db_data.get("Col_6") , String.valueOf(task.getStatus()));
        return values;
    }

    // Gets the DB stored on the emulator and runs a cursor the records in DB, and while it has a next one to go to
    //Adds the records to an array which we then return
    public List<Task> getAllTasks() {
        db = this.getWritableDatabase();
        Cursor cursor = null;
        db.beginTransaction();
        try{
            cursor = db.query(db_data.get("Table_Name"), null,null ,null ,null ,null ,null);
            if(cursor != null){
                while(cursor.moveToNext()){
                    Task task = new Task();
                    task.setId(cursor.getInt(cursor.getColumnIndex(db_data.get("Col_1"))));
                    task.setPriority(cursor.getInt(cursor.getColumnIndex(db_data.get("Col_2"))));
                    task.setTitle(cursor.getString(cursor.getColumnIndex(db_data.get("Col_3"))));
                    task.setDescription(cursor.getString(cursor.getColumnIndex(db_data.get("Col_4"))));
                    task.setDate(cursor.getString(cursor.getColumnIndex(db_data.get("Col_5"))));
                    task.setStatus(Enum.valueOf(Task.StatusType.class , cursor.getString(cursor.getColumnIndex(db_data.get("Col_6")))));
                    tasks.add(task);
                }
            }
        } finally {
            db.endTransaction();
            cursor.close();
        }
        return tasks;
    }

    // Get one single task
    public Task getTask(int id){
        db = this.getWritableDatabase();
        Cursor cursor = null;
        db.beginTransaction();
        try{
            cursor = db.query(db_data.get("Table_Name"), null,db_data.get("Col_1") + "=" + id ,null ,null ,null ,null);
            if(cursor != null) {
                cursor.moveToFirst();
                Task foundTask = new Task(
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                foundTask.setId(cursor.getInt(cursor.getColumnIndex(db_data.get("Col_1"))));
                foundTask.setStatus(Enum.valueOf(Task.StatusType.class, cursor.getString(cursor.getColumnIndex(db_data.get("Col_6")))));
                return foundTask;
            }
        } catch(Error err){
            throw new Error();
        } finally{
            db.endTransaction();
            cursor.close();
        }
        return null;
    }

    public void insertTask(Task task){
        // Easy way of adding a new task to the DB, not having to worry about SQL command being all over the place and using Content Values
        ContentValues values = fillContentValues(task);
        db.insert(db_data.get("Table_Name") , null , values);
    }

    public void updateTask(Task task, int id){
        ContentValues values = fillContentValues(task);
        db.update(db_data.get("Table_Name") , values, "ID=?" , new String[]{String.valueOf(id)});
    }

    public void deleteTask(Task task){
        db = this.getWritableDatabase();
        db.delete(db_data.get("Table_Name") , "ID=?" , new String[]{String.valueOf(task.getId())});
    }

    // Update the status of the task
    public void updateStatus(int id , String status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(db_data.get("Col_6") , status);
        db.update(db_data.get("Table_Name") , values, "ID=?" , new String[]{String.valueOf(id)});
    }

}

//public class DB {
//
//    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d, yyyy");
//
//    private List<Task> tasks = new ArrayList<Task>(){{
//       add(new Task(1,"Make a CRUD app" , "That's the plan" , formatter.format(new Date())));
//       add(new Task(2, "Feed the cat" , "Cat is hungry" , formatter.format(new Date())));
//       add(new Task(3, "Feed the cat" , "Cat is hungry" , formatter.format(new Date())));
//       add(new Task(4, "Feed the cat" , "Cat is hungry" , formatter.format(new Date())));
//       add(new Task(1, "Hang out the washing" , "Ah you need to, it's a sunny day boy" , formatter.format(new Date())));
//    }};
//
//    public List<Task> getTasks() {
//        return tasks;
//    }
//}
