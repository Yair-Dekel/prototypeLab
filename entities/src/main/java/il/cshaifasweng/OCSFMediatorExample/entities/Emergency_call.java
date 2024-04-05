package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="Emergency_Calls")
public class Emergency_call implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime creation_time;

    @Column(name="given_name")
    private  String given_name;

    @Column(name="phone_number")
    private  String phone_number;

    @OneToOne
    @JoinColumn(name="registered_user_id", referencedColumnName = "id")
    private  Registered_user registered_user;

    @Column(name="host")
    private  String host;


    public Emergency_call (String given_name , String phone_number ,Registered_user user, String host){
        this.creation_time=LocalDateTime.now();
        this.phone_number= phone_number;
        this.given_name=given_name;
        this.registered_user=user;
        this.host=host;
    }


    public Emergency_call() {


    }

    public Registered_user getRegistered_user() {
        return registered_user;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getCreation_time() {
        return creation_time;
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
}