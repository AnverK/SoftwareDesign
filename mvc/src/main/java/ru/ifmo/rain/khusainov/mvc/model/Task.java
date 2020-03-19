package ru.ifmo.rain.khusainov.mvc.model;

public class Task {
    private int id;
    private String name;
    private boolean isDone = true;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return isDone ? "done" : "active";
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean active) {
        isDone = active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
