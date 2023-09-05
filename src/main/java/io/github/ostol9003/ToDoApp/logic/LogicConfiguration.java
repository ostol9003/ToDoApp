package io.github.ostol9003.ToDoApp.logic;

import io.github.ostol9003.ToDoApp.TaskConfigurationProperties;
import io.github.ostol9003.ToDoApp.model.ProjectRepository;
import io.github.ostol9003.ToDoApp.model.TaskGroupRepository;
import io.github.ostol9003.ToDoApp.model.TaskRepository;
import org.springframework.context.annotation.Bean;

public class LogicConfiguration {
    @Bean
    ProjectService projectService(
            ProjectRepository repository,
            TaskGroupRepository taskGroupRepository,
            TaskGroupService taskGroupService,
            TaskConfigurationProperties config) {
        return new ProjectService(repository, taskGroupRepository, taskGroupService, config);
    }

    @Bean
    TaskGroupService taskGroupService(
            TaskGroupRepository repository,
            TaskRepository taskRepository) {
        return new TaskGroupService(repository, taskRepository);
    }

}
