package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class DisplayTasksMassage implements Serializable{

    private List<Task> tasks;


    public DisplayTasksMassage(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks()
    {
       return tasks;

    }
    public void setTasks(List<Task> tasks){
        this.tasks =tasks;
    }


}