package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class DisplayDataMessage implements Serializable {

    String dataType;
    private List<Task> tasks;
    private List<Registered_user> members;

    private  Registered_user user;

    private Communities community;

    public DisplayDataMessage() {
    }

//    public DisplayDataMessage(List<Task> tasks, List<Registered_user> members) {
//        this.tasks = tasks;
//        this.members = members;
//    }

    public DisplayDataMessage(List<Task> tasks, String task) {
        this.tasks = tasks;
        this.dataType = task;
    }

    public DisplayDataMessage(List<Task> tasks ) {
        this.tasks = tasks;
        this.dataType="";

    }

    public DisplayDataMessage(Communities community, String members) {
        this.community=community;
        this.dataType=members;
    }

    public Registered_user getUser() {
        return user;
    }

    public void setUser(Registered_user user) {
        this.user = user;
    }

    public DisplayDataMessage(Registered_user user1, String uploadedORpreformed) {
        this.user=user1;
        this.dataType=uploadedORpreformed;
    }



    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setCommunity(Communities community) {
        this.community = community;
    }

    public String getDataType() {
        return dataType;
    }

    public Communities getCommunity() {
        return community;
    }

    public DisplayDataMessage(List<Registered_user> members, String mem, Communities community) {
        this.members = members;
        this.dataType=mem;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Registered_user> getMembers() {
        return members;
    }

    public void setMembers(List<Registered_user> members) {
        this.members = members;
    }



}