package io.github.ostol9003.ToDoApp.controller;

import io.github.ostol9003.ToDoApp.logic.TaskService;
import io.github.ostol9003.ToDoApp.model.Task;
import io.github.ostol9003.ToDoApp.model.TaskRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final ApplicationEventPublisher publisher;
    private final TaskRepository repository;
    private final TaskService taskService;

    TaskController(ApplicationEventPublisher publisher, TaskRepository taskRepository, TaskService taskService) {
        this.publisher = publisher;
        this.repository = taskRepository;
        this.taskService = taskService;
    }


    @GetMapping(params = {"!sort", "!page", "!size"})
    CompletableFuture<ResponseEntity<List<Task>>> readAllTasks() {
        logger.warn("Exposing all the tasks!");
        return taskService.findAllAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @Transactional // albo transakcjonalnie albo reczny zapis jak nizej
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    // repository.save(task);
                });
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) {
        return ResponseEntity.ok(
                repository.findByDone(state)
        );
    }

    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate) {
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .map(Task::toggle)
                .ifPresent(publisher::publishEvent);
        return ResponseEntity.noContent().build();
    }


}
