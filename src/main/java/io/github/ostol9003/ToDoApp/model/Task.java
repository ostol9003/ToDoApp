package io.github.ostol9003.ToDoApp.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private int id;

    @NotBlank(message = "Task´s description must not be empty")
    private String description;

    private boolean done;
    @Column(name = "deadline")
    private LocalDateTime deadline;


    /* nadpisanie nazw kolumn
    @AttributeOverrides(
             {
                     @AttributeOverride(column = @Column(name="updatedOn"), name = "updatedOn")
                     @AttributeOverride(column = @Column(name="createdOn1"), name = "createdOn")
             }
     )*/
    @Embedded
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Audit audit = new Audit();

    @ManyToOne
    @JoinColumn(name = "task_group_id")
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.NONE)
    private TaskGroup group;


    public Task(String description, LocalDateTime deadLine, TaskGroup group) {
        this.description = description;
        this.deadline = deadLine;
        if (group != null)
            this.group = group;
    }

    public Task(String description, LocalDateTime deadLine) {
        this(description, deadLine, null);

    }

    public void updateFrom(final Task source) {
        description = source.description;
        done = source.done;
        deadline = source.deadline;
        group = source.group;
    }


}
