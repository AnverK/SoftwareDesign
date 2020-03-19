package ru.ifmo.rain.khusainov.mvc.dao;

import ru.ifmo.rain.khusainov.mvc.model.Task;
import ru.ifmo.rain.khusainov.mvc.model.TaskList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TaskListDao {
    private final AtomicInteger lastId = new AtomicInteger(0);
    private final LinkedHashMap<Integer, TaskList> taskLists = new LinkedHashMap<>();

    public int addTaskList(TaskList taskList) {
        int id = lastId.getAndIncrement();
        taskList.setId(id);
        taskLists.put(id, taskList);
        return id;
    }

    public TaskList getTaskListById(int id) {
        return taskLists.get(id);
    }

    public List<TaskList> getTasksList() {
        return new ArrayList<>(taskLists.values());
    }

    public void removeTaskList(int id) {
        taskLists.remove(id);
    }

    public List<Task> getTasksByTaskListId(int id) {
        return taskLists.get(id).getTasks();
    }

    public List<Task> getActiveTasksByTaskListId(int id) {
        return getTasksByTaskListId(id).stream().filter(task -> !task.isDone()).collect(Collectors.toList());
    }
}
