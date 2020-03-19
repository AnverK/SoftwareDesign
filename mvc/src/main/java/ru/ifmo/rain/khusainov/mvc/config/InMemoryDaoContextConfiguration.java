package ru.ifmo.rain.khusainov.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ifmo.rain.khusainov.mvc.dao.TaskDao;
import ru.ifmo.rain.khusainov.mvc.dao.TaskListDao;

@Configuration
public class InMemoryDaoContextConfiguration {
    @Bean
    public TaskListDao taskListDao() {
        return new TaskListDao();
    }

    @Bean
    public TaskDao taskDao() {
        return new TaskDao();
    }

}
