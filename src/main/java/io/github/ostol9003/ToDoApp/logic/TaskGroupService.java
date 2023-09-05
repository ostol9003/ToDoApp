package io.github.ostol9003.ToDoApp.logic;

import io.github.ostol9003.ToDoApp.model.Project;
import io.github.ostol9003.ToDoApp.model.TaskGroup;
import io.github.ostol9003.ToDoApp.model.TaskGroupRepository;
import io.github.ostol9003.ToDoApp.model.TaskRepository;
import io.github.ostol9003.ToDoApp.model.projection.GroupReadModel;
import io.github.ostol9003.ToDoApp.model.projection.GroupWriteModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskGroupService {
    private final TaskGroupRepository repository;
    private final TaskRepository taskRepository;

    public TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;

    }

    public GroupReadModel createGroup(final GroupWriteModel source) {
        return createGroup(source, null);
    }

    GroupReadModel createGroup(GroupWriteModel source, Project project) {
        TaskGroup result = repository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream().map(GroupReadModel::new).collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroupId(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first");
        }
        TaskGroup result = repository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("TaskGroup with given Id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }


}