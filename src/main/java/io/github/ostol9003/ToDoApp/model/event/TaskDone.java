package io.github.ostol9003.ToDoApp.model.event;

import io.github.ostol9003.ToDoApp.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent{
     TaskDone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
