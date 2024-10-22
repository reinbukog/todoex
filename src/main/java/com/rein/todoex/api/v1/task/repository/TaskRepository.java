package com.rein.todoex.api.v1.task.repository;

import com.rein.todoex.api.v1.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
