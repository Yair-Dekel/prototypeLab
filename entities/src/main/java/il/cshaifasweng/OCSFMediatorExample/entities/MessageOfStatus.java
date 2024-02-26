package il.cshaifasweng.OCSFMediatorExample.entities;
import java.io.Serializable;
public class MessageOfStatus implements Serializable{

    private Task task;
    private String changeStatus;


    public MessageOfStatus(Task task, String changeStatus)
    {
        this.task = task;
        this.changeStatus = changeStatus;

    }
    public MessageOfStatus(Task task)
    {
        this.task = task;

    }
    public MessageOfStatus( String changeStatus)
    {
        this.changeStatus = changeStatus;

    }

    public String getChangeStatus(){
        return changeStatus;
    }
    public void setTask(Task task){
        this.task =task;
    }

    public Task getTask() {
        return task;
    }

    public void setChangeStatus(String changeStatus)
    {
        this.changeStatus = changeStatus;
    }

}