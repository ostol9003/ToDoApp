package io.github.ostol9003.ToDoApp.controller;

import io.github.ostol9003.ToDoApp.TaskConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoControler {
    private final DataSourceProperties dataSource;
    private final TaskConfigurationProperties myProp;

    public InfoControler(DataSourceProperties dataSource, TaskConfigurationProperties myProp) {
        this.dataSource = dataSource;
        this.myProp = myProp;
    }

    @GetMapping("/url")
    String url() {
        return dataSource.getUrl();
    }

    @GetMapping("prop")
    boolean myProp() {
        return myProp.getTemplate().isAllowMultipleTasks();
    }
}
