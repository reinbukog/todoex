package com.rein.todoex.api.v1.task.service;

import com.rein.todoex.api.v1.task.domain.Task;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TaskService {

    Task addTask(Task task);
    Iterable<Task> getAllTasks(Pageable page);
    Task getTask(UUID id);
    Task updateTask(UUID id, Task task);
    void deleteTask(UUID id);
    void deleteAll();
}
