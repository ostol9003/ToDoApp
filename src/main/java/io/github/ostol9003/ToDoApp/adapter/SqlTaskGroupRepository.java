package io.github.ostol9003.ToDoApp.adapter;

import io.github.ostol9003.ToDoApp.model.TaskGroup;
import io.github.ostol9003.ToDoApp.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {

    @Override
    @Query("select distinct g from TaskGroup g join fetch g.tasks ")
    List<TaskGroup> findAll();

    @Override
    boolean existsByDoneIsFalseAndProject_Id(Integer groupId);
}
