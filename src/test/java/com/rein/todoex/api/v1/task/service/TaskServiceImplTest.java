package com.rein.todoex.api.v1.task.service;

import com.rein.todoex.api.v1.task.domain.Task;
import com.rein.todoex.api.v1.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Captor
    private ArgumentCaptor<Task> taskArgCaptor;

    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    private Task task;

    @BeforeEach
    void setup() {
        task = Task.builder()
                .title("Do Chore")
                .description("Life Process")
                .isCompleted(true)
                .build();
    }

    @Test
    void shouldAddNewTask() {
        when(taskRepository.save(any())).thenReturn(task);

        Task result = taskServiceImpl.addTask(task);

        assertThat(result, is(notNullValue()));
        assertThat(result.getTitle(), is(task.getTitle()));
        assertThat(result.getDescription(), is(task.getDescription()));
        assertThat(result.getId(), is(task.getId()));
        assertThat(result.getIsCompleted(), is(task.getIsCompleted()));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void whenInvalidTitle_shouldThrowException(String param) {
        Task taskToAdd = Task.builder()
                .title(param)
                .build();
        Exception exception = assertThrows(ResponseStatusException.class, () -> taskServiceImpl.addTask(taskToAdd));

        assertThat(exception.getMessage(), is("400 BAD_REQUEST \"Title should not be empty\""));
    }

    @Test
    void whenDefaultValue_shouldAddNewTask() {
        Task taskToAdd = Task.builder()
                .title("Task1")
                .build();
        taskServiceImpl.addTask(taskToAdd);
        verify(taskRepository, times(1)).save(taskArgCaptor.capture());

        Task result = taskArgCaptor.getValue();

        assertThat(result, is(notNullValue()));
        assertThat(result.getTitle(), is(taskToAdd.getTitle()));
        assertThat(result.getDescription(), is(""));
        assertThat(result.getIsCompleted(), is(false));
    }

    @Test
    void whenGetTaskById_shouldReturnTask() {
        when(taskRepository.findById(any())).thenReturn(Optional.of(task));

        Task result = taskServiceImpl.getTask(UUID.randomUUID());

        assertThat(result, is(notNullValue()));
        assertThat(result.getTitle(), is(task.getTitle()));
        assertThat(result.getDescription(), is(task.getDescription()));
        assertThat(result.getId(), is(task.getId()));
        assertThat(result.getIsCompleted(), is(task.getIsCompleted()));
    }

    @Test
    void whenTaskDoesNotExist_shouldThrowException() {
        when(taskRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> taskServiceImpl.getTask(UUID.fromString("29aa1a14-4733-40b4-a43f-95bc46786ba1")));

        assertThat(exception.getMessage(), is("400 BAD_REQUEST \"Valid Task Id Required\""));
    }

    @Test
    void shouldReturnAllTasks() {
        Pageable pageable = PageRequest.of(0, 5);
        Task task1 = Task.builder()
                .id(UUID.randomUUID())
                .title("Task1")
                .build();
        Task task2 = Task.builder()
                .id(UUID.randomUUID())
                .title("Task2")
                .build();

        Page<Task> taskPage = new PageImpl<>(Arrays.asList(task1, task2));
        when(taskRepository.findAll(pageable)).thenReturn(taskPage);

        Iterable<Task> result = taskServiceImpl.getAllTasks(pageable);

        assertThat(((Page<Task>)result).getContent().size(), is(2));
        assertThat(((Page<Task>)result).getTotalElements(), is(2L));
        assertThat(((Page<Task>)result).getTotalPages(), is(1));
        assertThat(((Page<Task>)result).getContent().get(0).getId(), is(task1.getId()));
        assertThat(((Page<Task>)result).getContent().get(1).getId(), is(task2.getId()));

    }

    @Test
    void shouldUpdateExistingTask() {
        Task taskForUpdate = Task.builder()
                .title("Task Updated")
                .description("Description Updated")
                .isCompleted(true)
                .build();

        when(taskRepository.findById(any())).thenReturn(Optional.of(task));

        taskServiceImpl.updateTask(task.getId(), taskForUpdate);

        verify(taskRepository, times(1)).save(taskArgCaptor.capture());

        Task result = taskArgCaptor.getValue();
        assertThat(result, is(notNullValue()));
        assertThat(result.getTitle(), is(taskForUpdate.getTitle()));
        assertThat(result.getDescription(), is(taskForUpdate.getDescription()));
        assertThat(result.getIsCompleted(), is(taskForUpdate.getIsCompleted()));
        assertThat(result.getId(), is(task.getId()));
    }

    @Test
    void whenUpdatingNonExistentId_shouldThrowException() {
        Task taskForUpdate = Task.builder()
                .title("Task Updated")
                .description("Description Updated")
                .isCompleted(true)
                .build();
        when(taskRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> taskServiceImpl.updateTask(task.getId(), taskForUpdate));

        assertThat(exception.getMessage(), is("400 BAD_REQUEST \"Valid Task Id Required\""));
    }

    @Test
    void shouldDeleteExistingTask() {
        when(taskRepository.findById(any())).thenReturn(Optional.of(task));

        taskServiceImpl.deleteTask(task.getId());

        verify(taskRepository, times(1)).deleteById(any());
    }

    @Test
    void whenDeletingNonExistentTask_shouldThrowException() {
        when(taskRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> taskServiceImpl.deleteTask(UUID.fromString("29aa1a14-4733-40b4-a43f-95bc46786ba1")));

        assertThat(exception.getMessage(), is("400 BAD_REQUEST \"Valid Task Id Required\""));
    }
}
