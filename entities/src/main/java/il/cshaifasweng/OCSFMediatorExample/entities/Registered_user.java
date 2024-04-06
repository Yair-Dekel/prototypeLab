


package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import java.util.List;

@Entity
@Table(name = "Users")
public class Registered_user implements Serializable { //extends User

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "given_name")
    private String givenName;
    @Column(name = "family_name")
    private String familyName;

    @Column(name = "salt")
    private String salt;

    private String username;

    private String password;


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
                           boolean permission, String phoneNumber, Communities community) throws NoSuchAlgorithmException {
        this.givenName = givenName;
        this.familyName = familyName;
        this.username = username;
        byte[] saltBytes = SecureUtils.getSalt();
        this.salt = Base64.getEncoder().encodeToString(saltBytes);
        this.password = SecureUtils.getSecurePassword(password,saltBytes);
        this.permission = permission;
        this.phone_number = phoneNumber;
        this.community = community;

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public int getId() {
        return id;
    }

    public boolean isPermission() {
        return permission;
    }

    public Communities getCommunity() {
        return community;
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



    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
}


}
