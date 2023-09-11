package io.github.ostol9003.ToDoApp.reports;

import io.github.ostol9003.ToDoApp.model.event.TaskEvent;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;

@NoArgsConstructor
@Entity
@Table(name = "task_events")
public class PersistedTaskEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int taskId;
    String name;
    LocalDateTime occurrence;

    PersistedTaskEvent(TaskEvent source){
        taskId = source.getTaskId();
        name = source.getClass().getSimpleName();
        occurrence = LocalDateTime.ofInstant(source.getOccurrence(), ZoneId.systemDefault());
    }

}
