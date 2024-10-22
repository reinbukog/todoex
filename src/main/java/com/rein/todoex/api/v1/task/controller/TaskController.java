package com.rein.todoex.api.v1.task.controller;

import com.rein.todoex.api.v1.task.domain.Task;
import com.rein.todoex.api.v1.task.service.TaskServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/tasks")
public class TaskController {

    private final TaskServiceImpl taskService;

    public TaskController(TaskServiceImpl taskServiceImpl) {
        this.taskService = taskServiceImpl;
    }

    @PostMapping
    public Task addTask(@RequestBody Task task) {
        return taskService.addTask(task);
    }

    @GetMapping
    public Iterable<Task> getAllTasks(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                      @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                      @RequestParam(value = "column", defaultValue = "title", required = false) String column,
                                      @RequestParam(value = "direction", defaultValue = "ASC", required = false) Sort.Direction direction) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, column));
        return taskService.getAllTasks(pageRequest);
    }

    @GetMapping(value = "/{id}")
    public Task getTask(@PathVariable UUID id) {
        return taskService.getTask(id);
    }

    @PutMapping(value = "/{id}")
    public Task updateTask(@PathVariable UUID id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping( value = "/{id}")
    public void deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
    }
}
