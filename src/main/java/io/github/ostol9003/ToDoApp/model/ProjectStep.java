package io.github.ostol9003.ToDoApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "project_steps")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProjectStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private int id;

    @NotBlank(message = "Project step´s description must not be empty")
    private String description;

    private int daysToDeadline;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
