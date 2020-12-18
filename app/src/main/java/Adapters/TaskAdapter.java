// Imports and Packages
package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listplanner.MainActivity;
import com.example.listplanner.R;
import com.example.listplanner.ViewSingleTask;

import java.util.HashMap;
import java.util.List;
import Models.Task;


// Class Declaration
// Setting up our Adapter to extend RecyclerView.Adapter Class
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    // Variable Declarations
    private Context context;
    private List<Task> tasks;

    // Setting up a task adapter constructor to take the context of where the adapter is located in app and takes our list of tasks to be used in the app and adapts it to the Recycler View
    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    // Necessary Methods needed to be implemented to get the task adapter working as desired
    // One Is for creating the view holders, one for attaching a click event or whatever to a view holder
    // And the other is simply to get the task adapters count
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sets up our view using our template view we made in the task layout file and our view Template Holder we made below
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout , parent , false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // Bind the information from the Task's data to the Task Template View Holder
        Task task = tasks.get(position);
        holder.title.setText(task.getTitle());
        // Sets the background of the tag based on priority level
        holder.priority_tag.setBackgroundResource(checkTagColours(task.getPriority()));
        holder.priority_tag.setText(String.valueOf(task.getPriority()));
        holder.date.setText("Created At:\n " + task.getDate());
        holder.description.setText(task.getDescription());
        holder.due_date.setText("Due Date: " + task.getdueDate());
        holder.due_date.setTextSize(10);

        holder.tasksContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewSingleIntent = new Intent(context , ViewSingleTask.class);
                viewSingleIntent.putExtra("Task_ID" , task.getId());
                context.startActivity(viewSingleIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        // Return Tasks Array Size
        return tasks.size();
    }

    //TODO: Make this method a global function or class

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

    // Setting up our View to basically act as a template of our task_layout we made out earlier
    // Template view of one of our Tasks in our app
    // We then Add these Template ViewHolders to the actual RecyclerView
    public class TaskViewHolder extends RecyclerView.ViewHolder{
        // Variable Declarations and Function Definitions
        public TextView title , description, date , priority_tag , due_date;
        public ConstraintLayout tasksContainer;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskHolderInit();
        }

        public void taskHolderInit() {
            tasksContainer = (ConstraintLayout) itemView.findViewById(R.id.tasksContainer);
            title = (TextView) itemView.findViewById(R.id.txtViewTitle);
            description = (TextView) itemView.findViewById(R.id.txtViewDescription);
            date = (TextView) itemView.findViewById(R.id.txtViewDate);
            priority_tag = (TextView) itemView.findViewById(R.id.txtViewPriorityTag);
            due_date = (TextView) itemView.findViewById(R.id.txtViewDueDate);
        }

    }
}
