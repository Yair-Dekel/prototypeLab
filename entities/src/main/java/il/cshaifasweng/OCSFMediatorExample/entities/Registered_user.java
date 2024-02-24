package il.cshaifasweng.OCSFMediatorExample.entities;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "Users")
public class Registered_user extends User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "given name")
    private String givenName;
    @Column(name = "family name")
    private String familyName;

    private String username;

    private String password;

    //    @OneToMany(mappedBy = "user")
//    private List <Task> volunteered;
    boolean permission; //1 for manager
    private String SerialNumber;
    @Column(name = "phone number")
    private String phone_number;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String community;

    // Default constructor for JPA
    public Registered_user() {
        // Default constructor required by JPA
    }

    // Constructor with parameters
    public Registered_user(String givenName, String familyName, String username, String password,
                           boolean permission, String phoneNumber, String community) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.username = username;
        this.password = password;
        this.permission = permission;
        this.phone_number = phoneNumber;
        this.community = community;
        // Initialize other fields or collections if needed
        // task_list = new ArrayList<>();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public int getId() {
        return id;
    }

/*
    public List<Task> getTask_list() {
        return task_list;
    }

    public void setTask_list(List<Task> task_list) {
        this.task_list = task_list;
    }*/

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}