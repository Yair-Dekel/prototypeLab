package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;

import java.util.List;

public class TasksMessageEvent {
    private final List<Task> tasks;

    public TasksMessageEvent(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}

