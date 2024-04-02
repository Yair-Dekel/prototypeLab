package il.cshaifasweng.OCSFMediatorExample.entities;
import java.io.Serializable;
public class MessageOfStatus implements Serializable{

    private Task task;
    private String changeStatus;

    private String username;


    public MessageOfStatus(Task task, String changeStatus)
    {
        this.task = task;
        this.changeStatus = changeStatus;
        this.username="";

    }
    public MessageOfStatus(String changeStatus,String username)
    {
        this.task = null;
        this.changeStatus = changeStatus;
        this.username=username;

    }

    public MessageOfStatus(Task task, String changeStatus,String username)
    {
        this.task = task;
        this.changeStatus = changeStatus;
        this.username=username;

    }
    public MessageOfStatus(Task task)
    {
        this.task = task;

    }
//    public MessageOfStatus( String changeStatus)
//    {
//        this.changeStatus = changeStatus;
//
//    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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