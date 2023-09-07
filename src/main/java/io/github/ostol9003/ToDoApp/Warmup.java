package io.github.ostol9003.ToDoApp;

import io.github.ostol9003.ToDoApp.model.Task;
import io.github.ostol9003.ToDoApp.model.TaskGroup;
import io.github.ostol9003.ToDoApp.model.TaskGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class Warmup implements ApplicationListener<ContextRefreshedEvent> {
    private final TaskGroupRepository groupRepository;

    public Warmup(TaskGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        log.info("Application warmup after context refreshed");
        final String description = "ApplicationContextEvent";
        if(!groupRepository.existsByDescription(description)) {
            log.info("No required group found! Adding it!");
            var group = new TaskGroup();
            group.setDescription(description);
            group.setTasks(Set.of(
                    new Task("ContextClosedEvent", null, group),
                    new Task("ContextRefreshedEvent", null, group),
                    new Task("ContextStoppedEvent", null, group),
                    new Task("ContextStartedEvent", null, group)
            ));
            groupRepository.save(group);
        }
    }
}
