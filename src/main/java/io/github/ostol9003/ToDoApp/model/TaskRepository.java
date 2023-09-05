package io.github.ostol9003.ToDoApp.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Optional<Task> findById(Integer id);

    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroupId(Integer groupId);

    Task save(Task entity);

    Page<Task> findAll(Pageable page);

    List<Task> findByDone(@Param("state") boolean done);


    List<Task> findAllByGroupId(Integer groupId);
}