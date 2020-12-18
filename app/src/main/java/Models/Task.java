// Imports and packages
package Models;

// Class Declaration
public class Task {
    // Enum to hold all the status strings
    public enum StatusType{
        ToDo,
        InProgress,
        Completed
    }

    //  Property and Method Declarations - making them private to use encapsulation
    private int priority;
    private String date_created;
    private StatusType statusType;
    private String title , description;
    private int id;


    // Constructor Declarations
    public Task(){}
    public Task(int priority, String title , String description , String date_created){
        this.priority = priority;
        this.title = title;
        this.description = description;
        this.date_created = date_created;
        this.statusType  = StatusType.ToDo;
    }



    // Getter and Setters Function Definitions for all my Task properties
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDate() {
        return date_created;
    }

    public void setDate(String date_created) {
        this.date_created = date_created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusType getStatus() {
        return statusType;
    }

    public void setStatus(StatusType statusType) {
        this.statusType = statusType;
    }
}
