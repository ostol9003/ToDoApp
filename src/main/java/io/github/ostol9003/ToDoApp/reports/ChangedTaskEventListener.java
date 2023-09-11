package io.github.ostol9003.ToDoApp.reports;

import io.github.ostol9003.ToDoApp.model.event.TaskDone;
import io.github.ostol9003.ToDoApp.model.event.TaskEvent;
import io.github.ostol9003.ToDoApp.model.event.TaskUndone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class ChangedTaskEventListener {
    private final PersistedTaskEventRepository repository;

    ChangedTaskEventListener(PersistedTaskEventRepository repository) {
        this.repository = repository;
    }

    @Async
    @EventListener
    public void on(TaskDone event) {
        onChanged(event);
    }

    @Async
    @EventListener
    public void on(TaskUndone event) {
        onChanged(event);
    }

    private void onChanged(final TaskEvent event) {
        log.info("Got " + event);
        repository.save(new PersistedTaskEvent(event));
    }
}
