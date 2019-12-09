package ru.ifmo.rain.khusainov.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.rain.khusainov.mvc.dao.TaskListDao;
import ru.ifmo.rain.khusainov.mvc.logic.DataFilter;
import ru.ifmo.rain.khusainov.mvc.model.Task;
import ru.ifmo.rain.khusainov.mvc.model.TaskList;

import java.util.List;
import java.util.Optional;

@Controller
public class TaskListController {
    private final TaskListDao taskListDao;

    public TaskListController(TaskListDao taskListDao) {
        this.taskListDao = taskListDao;
    }

    @RequestMapping(value = "/add-taskList", method = RequestMethod.POST)
    public String addTaskList(@ModelAttribute("taskList") TaskList taskList) {
        taskListDao.addTaskList(taskList);
        return "redirect:/taskLists";
    }

    @RequestMapping(value = "/taskLists", method = RequestMethod.GET)
    public String getTaskList(ModelMap map) {
        prepareModelMap(map, taskListDao.getTasksList());
        return "index";
    }

    @RequestMapping(value = "/taskLists/{taskListId}", method = RequestMethod.GET)
    public String getTaskListTasks(ModelMap map, @PathVariable int taskListId) {
        prepareModelMap(map, taskListDao.getTaskListById(taskListId), taskListDao.getTasksByTaskListId(taskListId));
        return "taskList";
    }

    @RequestMapping(value = "/taskLists/{id}/delete", method = RequestMethod.GET)
    public String deleteTaskList(@PathVariable int id) {
        taskListDao.removeTaskList(id);
        return "redirect:/taskLists";
    }

    @RequestMapping(value = "/taskLists/{id}/filter-tasks", method = RequestMethod.GET)
    public String getTasks(@PathVariable int id, @RequestParam String filter, ModelMap map) {
        Optional<DataFilter> dataFilter = DataFilter.getFilterByName(filter);
        dataFilter.ifPresent(value -> prepareModelMap(map, taskListDao.getTaskListById(id), value.filter(id, taskListDao)));
        return "taskList";
    }

    private void prepareModelMap(ModelMap map, List<TaskList> taskLists) {
        map.addAttribute("taskLists", taskLists);
        map.addAttribute("newTaskList", new TaskList());
    }

    private void prepareModelMap(ModelMap map, TaskList taskList, List<Task> tasks) {
        map.addAttribute("currentTaskList", taskList);
        map.addAttribute("currentTasks", tasks);
        map.addAttribute("task", new Task());
    }

}
