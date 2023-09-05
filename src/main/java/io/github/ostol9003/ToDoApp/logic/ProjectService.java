package io.github.ostol9003.ToDoApp.logic;

import io.github.ostol9003.ToDoApp.TaskConfigurationProperties;
import io.github.ostol9003.ToDoApp.model.Project;
import io.github.ostol9003.ToDoApp.model.ProjectRepository;
import io.github.ostol9003.ToDoApp.model.TaskGroupRepository;
import io.github.ostol9003.ToDoApp.model.projection.GroupReadModel;
import io.github.ostol9003.ToDoApp.model.projection.GroupTaskWriteModel;
import io.github.ostol9003.ToDoApp.model.projection.GroupWriteModel;
import io.github.ostol9003.ToDoApp.model.projection.ProjectWriteModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository repository;
    private final TaskGroupRepository taskGroupRepository;
    private final TaskConfigurationProperties config;
    private final TaskGroupService taskGroupService;

    public ProjectService(ProjectRepository repository,
                          TaskGroupRepository taskGroupRepository,
                          TaskGroupService taskGroupService,
                          TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskGroupService = taskGroupService;
        this.config = config;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(ProjectWriteModel toSave) {
        return repository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId))
            throw new IllegalStateException("Only one undone group from project is allowed");

        return repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> {
                                                var task = new GroupTaskWriteModel();
                                                task.setDescription(projectStep.getDescription());
                                                task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                                return task;
                                            }

                                    ).collect(Collectors.toList()));
                    return taskGroupService.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}
