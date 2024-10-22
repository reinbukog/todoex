package com.rein.todoex.api.v1.task.service;

import com.rein.todoex.api.v1.task.domain.Task;

import com.rein.todoex.api.v1.task.repository.TaskRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Task addTask(Task task) {
        if (StringUtils.isBlank(task.getTitle())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title should not be empty");
        }
        return taskRepository.save(task);
    }

    public Iterable<Task> getAllTasks(Pageable page) {
        return taskRepository.findAll(page);
    }

    public Task getTask(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid Task Id Required"));
    }

    @Transactional
    public Task updateTask(UUID id, Task task) {
        Task existingTask = getTask(id);
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setIsCompleted(task.getIsCompleted());
        return taskRepository.save(existingTask);
    }

    @Transactional
    public void deleteTask(UUID id) {
        getTask(id);
        taskRepository.deleteById(id);
    }

    // used for testing only
    @Transactional
    public void deleteAll() {
        taskRepository.deleteAll();
    }
}
