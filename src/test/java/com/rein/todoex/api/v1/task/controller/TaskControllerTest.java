package com.rein.todoex.api.v1.task.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rein.todoex.api.v1.task.domain.Task;
import com.rein.todoex.api.v1.task.service.TaskService;
import com.rein.todoex.api.v1.task.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskServiceImpl taskService;

    @BeforeEach
    void setup() {
        taskService.deleteAll();
    }

    @Test
    void testAddTask() throws Exception {
        MvcResult result;

        // POST /tasks
        String taskJson = "{\"title\":\"Task 1\", \"description\":\"First Task\", \"isCompleted\":true}";
        result = mockMvc.perform(post("/tasks")
                        .contentType("application/json")
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("First Task"))
                .andExpect(jsonPath("$.isCompleted").value(true))
                .andReturn();
        UUID idTask1 = getUuid(result.getResponse().getContentAsString());

        String taskWithMinimumValues = "{\"title\":\"Task 2\"}";
        result = mockMvc.perform(post("/tasks")
                        .contentType("application/json")
                        .content(taskWithMinimumValues))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Task 2"))
                .andExpect(jsonPath("$.description").value(""))
                .andExpect(jsonPath("$.isCompleted").value(false))
                .andReturn();
        UUID idTask2 = getUuid(result.getResponse().getContentAsString());

        String completedTask = "{\"title\":\"Task 3\", \"description\":\"Take A Swim\", \"isCompleted\":true}";
        result = mockMvc.perform(post("/tasks")
                        .contentType("application/json")
                        .content(completedTask))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Task 3"))
                .andExpect(jsonPath("$.description").value("Take A Swim"))
                .andExpect(jsonPath("$.isCompleted").value(true))
                .andReturn();
        UUID idTask3 = getUuid(result.getResponse().getContentAsString());

        String taskWithInvalidTitle = "{\"title\":\"\"}";
        mockMvc.perform(post("/tasks")
                        .contentType("application/json")
                        .content(taskWithInvalidTitle))
                .andExpect(status().is4xxClientError());


        // GET /tasks
        mockMvc.perform(get("/tasks?size=2&page=0"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id").value(idTask1))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.content[0].title").value("Task 1"))
                .andExpect(jsonPath("$.content[1].title").value("Task 2"));

        mockMvc.perform(get("/tasks?size=2&page=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Task 3"));


        // GET /tasks/{id}
        mockMvc.perform(get("/tasks/" + idTask1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idTask1.toString()))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("First Task"))
                .andExpect(jsonPath("$.isCompleted").value(true));

        mockMvc.perform(get("/tasks/" + idTask2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idTask2.toString()))
                .andExpect(jsonPath("$.title").value("Task 2"))
                .andExpect(jsonPath("$.description").value(""))
                .andExpect(jsonPath("$.isCompleted").value(false));

        mockMvc.perform(get("/tasks/" + UUID.randomUUID()))
                .andExpect(status().isBadRequest());


        // PUT /tasks/{id}
        String updateTask = "{\"title\":\"Task 1\", \"description\":\"First Task is incomplete\", \"isCompleted\":false}";
        mockMvc.perform(put("/tasks/" + idTask1)
                    .contentType("application/json")
                    .content(updateTask))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idTask1.toString()))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("First Task is incomplete"))
                .andExpect(jsonPath("$.isCompleted").value(false));

        mockMvc.perform(put("/tasks/" + UUID.randomUUID())
                        .contentType("application/json")
                        .content(updateTask))
                .andExpect(status().isBadRequest());


        // DELETE /tasks/{id}
        mockMvc.perform(delete("/tasks/" + idTask1))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/tasks/" + idTask2))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/tasks/" + idTask3))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/tasks/" + UUID.randomUUID()))
                .andExpect(status().isBadRequest());
    }

    private UUID getUuid(String taskJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Task task = mapper.readValue(taskJson, Task.class);
        return task.getId();
    }
}
