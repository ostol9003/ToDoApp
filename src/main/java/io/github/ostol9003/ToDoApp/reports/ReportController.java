package io.github.ostol9003.ToDoApp.reports;

import io.github.ostol9003.ToDoApp.model.Task;
import io.github.ostol9003.ToDoApp.model.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/reports")
public class ReportController {
    private final TaskRepository taskRepository;
    private final PersistedTaskEventRepository eventRepository;

    public ReportController(TaskRepository taskRepository,
                            PersistedTaskEventRepository eventRepository) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/count/{id}")
    ResponseEntity<TaskWithChangesCount> readTaskWithCount(@PathVariable int id) {

        return taskRepository.findById(id)
                .map(task -> new TaskWithChangesCount(task, eventRepository.findByTaskId(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/doneBeforeDeadline/{id}")
    ResponseEntity<TaskDoneBeforeDeadline> taskDoneBeforeDeadline(@PathVariable int id) {

        return taskRepository.findById(id)
                .map(task -> new TaskDoneBeforeDeadline(task, eventRepository.findByTaskId(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private class TaskWithChangesCount {
        public String description;
        public boolean done;
        public int changesCount;

        TaskWithChangesCount(final Task task, final List<PersistedTaskEvent> events) {
            description = task.getDescription();
            done = task.isDone();
            changesCount = events.size();
        }

    }

    private class TaskDoneBeforeDeadline {
        public String description;
        public boolean done;
        public boolean doneBeforeDeadline;
        public LocalDateTime taskDeadline;
        public LocalDateTime changedDate;
        public TaskDoneBeforeDeadline(Task task, List<PersistedTaskEvent> events) {
            description = task.getDescription();
            done = task.isDone();
            try {
                taskDeadline = task.getDeadline();
                changedDate = events.get(events.size() - 1).occurrence;
                if(task.isDone() && changedDate.isBefore(taskDeadline)
                ) doneBeforeDeadline = true;

            }catch (Exception e)
            {
                log.info(e.toString());
                doneBeforeDeadline = false;
            }


        }
    }
}
