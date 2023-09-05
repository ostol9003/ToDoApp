package io.github.ostol9003.ToDoApp.model.projection;

import io.github.ostol9003.ToDoApp.model.Project;
import io.github.ostol9003.ToDoApp.model.ProjectStep;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class ProjectWriteModel {
    //@NotBlank(message = "Project´s description must not be empty")
    private String description;
    @Valid
    private List<ProjectStep> steps = new ArrayList<>();

    public ProjectWriteModel() {
        steps.add(new ProjectStep());
    }

    public Project toProject() {
        var result = new Project();
        result.setDescription(description);
        steps.forEach(step -> step.setProject(result));
        result.setSteps(new HashSet<>(steps));
        return result;
    }


}
