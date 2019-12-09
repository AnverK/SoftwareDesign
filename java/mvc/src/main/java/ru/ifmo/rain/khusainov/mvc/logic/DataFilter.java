package ru.ifmo.rain.khusainov.mvc.logic;

import ru.ifmo.rain.khusainov.mvc.dao.TaskListDao;
import ru.ifmo.rain.khusainov.mvc.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class DataFilter {
    private static Map<String, DataFilter> filters = createFiltersMap();

    private static HashMap<String, DataFilter> createFiltersMap() {
        HashMap<String, DataFilter> filters = new HashMap<>();
        filters.put("all", new AllFilter());
        filters.put("active", new ActiveFilter());
        return filters;
    }

    public abstract List<Task> filter(int id, TaskListDao taskListDao);

    private static class AllFilter extends DataFilter {
        public List<Task> filter(int id, TaskListDao taskListDao) {
            return taskListDao.getTasksByTaskListId(id);
        }
    }

    private static class ActiveFilter extends DataFilter {
        public List<Task> filter(int id, TaskListDao taskListDao) {
            return taskListDao.getActiveTasksByTaskListId(id);
        }
    }

    public static Optional<DataFilter> getFilterByName(String name) {
        return Optional.ofNullable(filters.get(name));
    }
}
