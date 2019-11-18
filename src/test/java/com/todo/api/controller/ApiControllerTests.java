package com.todo.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.api.entities.Todo;
import com.todo.api.repos.TodoRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(APIController.class)
public class ApiControllerTests {

    @MockBean
    TodoRepository repo;

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldGet201Created_postTodoIntoRepo() throws Exception {
        Date now = new Date();
        Todo todo = new Todo();
        todo.setId("123");
        todo.setText("bla");
        todo.setComplete(false);
        todo.setPriority(1);
        todo.setCreationDate(now);

        when(repo.save(todo)).thenReturn(todo);

        mvc.perform(MockMvcRequestBuilders
                .post("/todo")
                .content(asJsonString(todo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(todo.getId()))
                .andExpect(jsonPath("$.text").value(todo.getText()))
                .andExpect(jsonPath("$.complete").value(todo.isComplete()))
                .andExpect(jsonPath("$.priority").value(todo.getPriority()));
    }

    @Test
    public void shouldGet200OK_editTodoIntoRepo() throws Exception {
        Date now = new Date();
        Todo todo = new Todo();
        todo.setId("123");
        todo.setText("bla");
        todo.setComplete(false);
        todo.setPriority(1);
        todo.setCreationDate(now);

        when(repo.save(todo)).thenReturn(todo);

        mvc.perform(MockMvcRequestBuilders
                .put("/todo")
                .content(asJsonString(todo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todo.getId()))
                .andExpect(jsonPath("$.text").value(todo.getText()))
                .andExpect(jsonPath("$.complete").value(todo.isComplete()))
                .andExpect(jsonPath("$.priority").value(todo.getPriority()));
    }

    @Test
    public void shouldGet200OK_deleteTodoIntoRepo() throws Exception {

        doNothing().when(repo).deleteById("1");

        mvc.perform(MockMvcRequestBuilders
                .delete("/todo/1")
                .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGet200OK_getOneTodo() throws Exception {

        Date now = new Date();
        Todo todo = new Todo();
        todo.setId("123");
        todo.setText("bla");
        todo.setComplete(false);
        todo.setPriority(1);
        todo.setCreationDate(now);

        when(repo.getOne("123")).thenReturn(todo);

        mvc.perform(MockMvcRequestBuilders
                .get("/todo/123")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todo.getId()))
                .andExpect(jsonPath("$.text").value(todo.getText()))
                .andExpect(jsonPath("$.complete").value(todo.isComplete()))
                .andExpect(jsonPath("$.priority").value(todo.getPriority()));
    }

    @Test
    public void shouldGet200OK_getTodos() throws Exception {

        Date now = new Date();
        Todo todo = new Todo();
        todo.setId("123");
        todo.setText("bla");
        todo.setComplete(false);
        todo.setPriority(1);
        todo.setCreationDate(now);

        Todo todo2 = new Todo();
        todo2.setId("123");
        todo2.setText("bla");
        todo2.setComplete(false);
        todo2.setPriority(1);
        todo2.setCreationDate(now);

        List<Todo> todos = new ArrayList<>();
        todos.add(todo);
        todos.add(todo2);

        when(repo.findAll()).thenReturn(todos);

        mvc.perform(MockMvcRequestBuilders
                .get("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(todo.getId()))
                .andExpect(jsonPath("$[0].text").value(todo.getText()))
                .andExpect(jsonPath("$[0].complete").value(todo.isComplete()))
                .andExpect(jsonPath("$[0].priority").value(todo.getPriority()))
                .andExpect(jsonPath("$[1].id").value(todo2.getId()))
                .andExpect(jsonPath("$[1].text").value(todo2.getText()))
                .andExpect(jsonPath("$[1].complete").value(todo2.isComplete()))
                .andExpect(jsonPath("$[1].priority").value(todo2.getPriority()));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
