package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class DisplayTasksMassage implements Serializable{

    private List<Task> tasks;
    String message;



    public DisplayTasksMassage(List<Task> tasks ) {
        this.tasks = tasks;
        this.message="";

    }

    public DisplayTasksMassage(List<Task> tasks,String message ) {
        this.tasks = tasks;
        this.message=message;

    }

    public List<Task> getTasks()
    {
       return tasks;

    }
    public void setTasks(List<Task> tasks){
        this.tasks =tasks;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}