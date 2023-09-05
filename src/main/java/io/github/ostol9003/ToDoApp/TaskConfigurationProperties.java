package io.github.ostol9003.ToDoApp;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties("task")
public
class TaskConfigurationProperties {
    private Template template;

    public void setTemplate(Template template) {
        this.template = template;
    }

    @Getter
    public static class Template {
        private boolean allowMultipleTasks;

        public void setAllowMultipleTasks(boolean allowMultipleTasks) {
            this.allowMultipleTasks = allowMultipleTasks;
        }
    }

}
