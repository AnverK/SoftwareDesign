package ru.ifmo.rain.khusainov.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.ifmo.rain.khusainov.mvc.dao.TaskDao;
import ru.ifmo.rain.khusainov.mvc.dao.TaskListDao;
import ru.ifmo.rain.khusainov.mvc.model.Task;

@Controller
public class TaskController {
    private final TaskDao taskDao;
    private final TaskListDao taskListDao;

    public TaskController(TaskDao taskDao, TaskListDao taskListDao) {
        this.taskDao = taskDao;
        this.taskListDao = taskListDao;
    }

    @RequestMapping(value = "/taskLists/{taskListId}/add-task", method = RequestMethod.POST)
    public String addTask(@PathVariable int taskListId, @ModelAttribute("task") Task task) {
        taskDao.addTask(taskListDao.getTaskListById(taskListId), task);
        return "redirect:/taskLists/{taskListId}";
    }

    @RequestMapping(value = "/taskLists/{id}/task/{taskId}", method = RequestMethod.GET)
    public String getTask(ModelMap map, @PathVariable int id, @PathVariable int taskId) {
        prepareModelMap(map, id, taskDao.getTaskById(taskId));
        return "task";
    }

    @RequestMapping(value = "/taskLists/{id}/task/{taskId}", method = RequestMethod.POST)
    public String doTask(@PathVariable int id, @PathVariable int taskId, @ModelAttribute("task") Task task) {
        taskDao.doTask(taskId);
        return "redirect:/taskLists/{id}/task/{taskId}";
    }

    private void prepareModelMap(ModelMap map, int taskListId, Task task) {
        map.addAttribute("task", task);
        map.addAttribute("currentTaskListId", taskListId);
    }

}
