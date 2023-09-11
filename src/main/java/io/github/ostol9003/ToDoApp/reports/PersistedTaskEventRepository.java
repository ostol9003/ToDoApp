package io.github.ostol9003.ToDoApp.reports;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersistedTaskEventRepository extends JpaRepository<PersistedTaskEvent,Integer> {
    List<PersistedTaskEvent> findByTaskId (int taskId);
}
