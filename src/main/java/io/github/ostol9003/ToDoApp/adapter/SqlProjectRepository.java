package io.github.ostol9003.ToDoApp.adapter;

import io.github.ostol9003.ToDoApp.model.Project;
import io.github.ostol9003.ToDoApp.model.ProjectRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    @Override
    @Query("select distinct p from Project p join fetch p.steps ")
    List<Project> findAll();

}
