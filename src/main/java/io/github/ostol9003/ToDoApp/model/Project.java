package io.github.ostol9003.ToDoApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private int id;

    @NotBlank(message = "Project´s description must not be empty")
    private String description;

    @OneToMany(mappedBy = "project")
    @Setter(AccessLevel.PACKAGE)
    private Set<TaskGroup> groups;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<ProjectStep> steps;


}
