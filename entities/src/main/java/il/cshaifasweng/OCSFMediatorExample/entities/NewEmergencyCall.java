package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NewEmergencyCall implements  Serializable{

    private Registered_user openby1;

    private String given_name;
    private String phone_number;

    private boolean inDataBase;


    public NewEmergencyCall(String given_name , String phone_number, Registered_user openby1){

        this.phone_number=phone_number;
        this.given_name= given_name;
        this.openby1=openby1;
        this.inDataBase=false;

    }


    public Registered_user getOpenby1() {
        return openby1;
    }

    public void setOpenby(Registered_user openby1) {
        this.openby1 = openby1;
    }

    public String getGiven_name() {
        return given_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isInDataBase() {
        return inDataBase;
    }

    public void setInDataBase(boolean inDataBase) {
        this.inDataBase = inDataBase;
    }
}