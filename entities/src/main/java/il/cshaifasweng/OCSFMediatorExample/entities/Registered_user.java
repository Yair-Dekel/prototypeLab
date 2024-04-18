/*
package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;

@Entity
@Table(name = "Users")
public class Registered_user
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
//   int permission; //1 for manager
    private String SerialNumber;
    @Column(name = "phone number")
    private String phone_number;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String community;

    // Default constructor for JPA
*/
/*    public Registered_user() {
        // Default constructor required by JPA
    }//


    // Constructor with parameters
    public Registered_user(String givenName, String familyName, String username, String password,
                            String phoneNumber, String community) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.username = username;
        this.password = password;
//        this.permission = 0;
        this.phone_number = phoneNumber;
        this.community = community;
        // Initialize other fields or collections if needed
        // task_list = new ArrayList<>();
    }
    public Registered_user() {
        this.givenName = "givenName";
        this.familyName = "familyName";
        this.username = "username";
        this.password = "password";
//        this.permission = 0;
        this.phone_number = "phoneNumber";
        this.community = "community";
        // Initialize other fields or collections if needed
        // task_list = new ArrayList<>();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public int getId() {
        return id;
    }

*/
/*
    public List<Task> getTask_list() {
        return task_list;
    }

    public void setTask_list(List<Task> task_list) {
        this.task_list = task_list;
    }//


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
}*/


package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Users")
public class Registered_user implements Serializable { //extends User

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPermission() {
        return permission;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(mappedBy = "registered_user")
    private List<Task> tasks_uploaded;
    @Column(name = "given_name")
    private String givenName;
    @Column(name = "family_name")
    private String familyName;

    private String username;

    private String password;

    //    @OneToMany(mappedBy = "user")
//    private List <Task> volunteered;
    boolean permission; //1 for manager
    //private String SerialNumber;
    @Column(name = "phone_number")
    private String phone_number;
    private Communities community;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Default constructor for JPA
    public Registered_user() {
        // Default constructor required by JPA
    }

    // Constructor with parameters
    public Registered_user(String givenName, String familyName, String username, String password,
                           boolean permission, String phoneNumber, Communities community) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.username = username;
        this.password = password;
        this.permission = permission;
        this.phone_number = phoneNumber;
        this.community = community;

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public int getId() {
        return id;
    }



    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public Communities getCommunity() {
        return community;
    }

    public void setCommunity(Communities community) {
        this.community = community;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
}
}
