package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NewTaskMessage implements Serializable{

    private LocalDateTime deadline;
    private String details;
    private TaskType type;
    private  Registered_user openby;
    private boolean inDataBase;

    private Task newTask;
    public Registered_user getOpenby() {
        return openby;
    }

    public void setOpenby(Registered_user openby) {
        this.openby = openby;
    }

    public boolean isInDataBase() {
        return inDataBase;
    }

    public Task getNewTask() {
        return newTask;
    }

    public void setNewTask(Task newTask) {
        this.newTask = newTask;
    }

    public void setInDataBase(boolean inDataBase) {
        this.inDataBase = inDataBase;
    }

    public NewTaskMessage(LocalDateTime deadline, String details, TaskType type, Registered_user openby ) {
        this.deadline = deadline;
        this.details = details;
        this.type = type;
        this.openby=openby;
        this.inDataBase=false;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

}