package ru.ifmo.rain.khusainov.mvc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {
    private int id;
    private String name;
    private List<Task> tasks = new ArrayList<>();

    public void setId(int id) {
        this.id = id;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }


}
