package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    int id;
    LocalDateTime timeStamp;
    String message;
    Registered_user user;

    String Password;
    String userName;




    String data;

    public Message(int id, LocalDateTime timeStamp, String message) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.message = message;
    }

    public Message(String message,String userName,String password)
    {
        this.message=message;
        this.userName=userName;
        Password=password;

    }
    public Message(String message)
    {
        this.message=message;
    }

    public Message(int id, String message) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.message = message;
        this.data = null;
    }
    public Message(int id, String message,int deadline) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.message = message;
        //this.task=new Task(message,deadline);
        this.data = null;
    }

    public Registered_user getUser() {
        return user;
    }

    public void setUser(Registered_user user) {
        this.user = user;
    }

    public Message(int id, String message, String data) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.message = message;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
}
}