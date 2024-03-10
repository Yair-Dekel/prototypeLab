package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name="Tasks")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String moredetails;
    private TaskType Type_of_task;
    private LocalDateTime Creation_time;
    @ManyToOne
    @JoinColumn(name = "registered_user_id", referencedColumnName = "id")
    private Registered_user registered_user;
    private LocalDateTime Deadline;
    private String Status;
     @ManyToOne
     @JoinColumn(name = "volunteer_id", referencedColumnName = "id")
     private Registered_user Volunteer;


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor

    public Task(TaskType type_of_task, Registered_user user, LocalDateTime time, String details) {
        moredetails = details;
        Type_of_task = type_of_task;
        Creation_time = LocalDateTime.now();
        this.registered_user = user;
        Deadline = time;
        Status = "waiting for approval";
    }
    public Task(TaskType type_of_task, Registered_user user, LocalDateTime time) {
        Type_of_task = type_of_task;
        Creation_time = LocalDateTime.now();
        this.registered_user = user;
        Deadline = time;
        Status = "waiting for approval";
    }
    public Task(TaskType type_of_task)
    {
        Type_of_task = type_of_task;
        Creation_time = LocalDateTime.now();
        this.Deadline=LocalDateTime.now();
        Status = "A task waiting for a volunteer";
    }

    public Task() {

    }

//    public Task()
//    {
//       this.Type_of_task="help";
//    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {    //add from j&Y
        // Example with more information:
        return ("Task ID: " + id + ", Type: " + Type_of_task + ", Status: "+Status);
}

    public TaskType getType_of_task() {
        return Type_of_task;
    }

    public void setType_of_task(TaskType type_of_task) {
        Type_of_task = type_of_task;
    }

    public LocalDateTime getCreation_time() {
        return Creation_time;
    }

    public void setCreation_time(LocalDateTime creation_time) {
        Creation_time = creation_time;
    }

    public Registered_user getRegistered_user() {
        return registered_user;
    }

    public void setRegistered_user(Registered_user registered_user) {
        this.registered_user = registered_user;
    }

    public LocalDateTime getDeadline() {
        return Deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        Deadline = deadline;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Registered_user getVolunteer() {
        return Volunteer;
    }

    public void setVolunteer(Registered_user volunteer) {
        Volunteer = volunteer;
    }




}
