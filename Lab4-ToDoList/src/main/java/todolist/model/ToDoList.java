package todolist.model;

import java.util.ArrayList;
import java.util.List;

public class ToDoList {
    private int id;
    private String title;

    private final List<ToDoTask> tasks = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ToDoTask> getTasks() {
        return tasks;
    }

}
