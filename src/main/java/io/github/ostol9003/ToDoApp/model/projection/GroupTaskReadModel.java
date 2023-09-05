package io.github.ostol9003.ToDoApp.model.projection;

import io.github.ostol9003.ToDoApp.model.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupTaskReadModel {
    private boolean done;
    private String description;

    public GroupTaskReadModel(Task source) {
        description = source.getDescription();
        done = source.isDone();
    }

}
