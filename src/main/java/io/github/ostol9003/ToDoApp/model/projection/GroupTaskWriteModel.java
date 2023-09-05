package io.github.ostol9003.ToDoApp.model.projection;

import io.github.ostol9003.ToDoApp.model.Task;
import io.github.ostol9003.ToDoApp.model.TaskGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class GroupTaskWriteModel {
    @NotBlank(message = "Task groups´s description must not be empty")
    private String description;
    @DateTimeFormat(pattern = "yy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;

    public Task toTask(TaskGroup group) {
        return new Task(description, deadline, group);
    }
}
