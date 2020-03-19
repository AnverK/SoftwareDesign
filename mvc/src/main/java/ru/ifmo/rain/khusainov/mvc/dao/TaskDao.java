package ru.ifmo.rain.khusainov.mvc.dao;

import ru.ifmo.rain.khusainov.mvc.model.Task;
import ru.ifmo.rain.khusainov.mvc.model.TaskList;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskDao {
    private final AtomicInteger lastId = new AtomicInteger(0);
    private final List<Task> tasks = new CopyOnWriteArrayList<>();

    public int addTask(TaskList taskList, Task task) {
        int id = lastId.getAndIncrement();
        task.setId(id);
        task.setDone(false);
        taskList.addTask(task);
        tasks.add(task);
        return id;
    }

    public void doTask(int taskId) {
        tasks.get(taskId).setDone(true);
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }
}
