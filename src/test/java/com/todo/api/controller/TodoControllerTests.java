package com.todo.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.api.entities.Item;
import com.todo.api.repositories.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TodoController.class)
public class TodoControllerTests {


    @MockBean
    private ItemRepository repository;

    @Autowired
    private MockMvc mvc;

    @Test
    public void getReturnsListOfItems() throws Exception{

        Item item = new Item();
        item.setItemId("test");

        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(item));

        mvc.perform( MockMvcRequestBuilders
                .get("/items")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].itemId").value("test"));

    }

    @Test
    public void putNewItemAddsToList() throws Exception {
        Item item = new Item();
        item.setItemId("test");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(item);

        Mockito.when(repository.save(item)).thenReturn(item);

        mvc.perform(MockMvcRequestBuilders
                .put("/items")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated());
                }


    // put == create; post == update

}
