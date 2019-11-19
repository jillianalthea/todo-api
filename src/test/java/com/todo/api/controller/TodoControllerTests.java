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

import java.util.Collections;

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
    public void getReturnsListOfItems() throws Exception {

        Item item = new Item().withItemId("test");

        Mockito.when(repository.findAll()).thenReturn(Collections.singletonList(item));

        mvc.perform(MockMvcRequestBuilders
                .get("/items")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].itemId").value("test"));

    }

    @Test
    public void putNewItemAddsToList() throws Exception {
        Item item = new Item().withItemId("test");

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

    @Test
    public void postItemEditsExisting() throws Exception {
        Item original = new Item().withItemId("id").withText("original");

        Item updated = new Item().withItemId("id").withText("updated");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(updated);

        Mockito.when(repository.findById("id")).thenReturn(java.util.Optional.of(original));
        Mockito.when(repository.save(updated)).thenReturn(updated);

        mvc.perform(MockMvcRequestBuilders
                .post("/items")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("updated"));
    }

    @Test
    public void postNewItemAddsToList() throws Exception {
        Item updated = new Item().withItemId("test").withText("new");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(updated);

        Mockito.when(repository.findById("test")).thenReturn(java.util.Optional.ofNullable(null));
        Mockito.when(repository.save(updated)).thenReturn(updated);

        mvc.perform(MockMvcRequestBuilders
                .post("/items")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("new"));
    }

    @Test
    public void deleteItem() throws Exception {
        Item item = new Item().withItemId("idDelete").withText("testing text");

        Mockito.when(repository.findById(item.getItemId())).thenReturn(java.util.Optional.of(item));
        Mockito.doNothing().when(repository).delete(item);

        mvc.perform(MockMvcRequestBuilders
                .delete("/items/"+item.getItemId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteItemThatDoesntExist() throws Exception {
        Item item = new Item().withItemId("idDelete2").withText("testing text");

        Mockito.when(repository.findById(item.getItemId())).thenReturn(java.util.Optional.empty());
        Mockito.doNothing().when(repository).delete(item);

        mvc.perform(MockMvcRequestBuilders
                .delete("/items/"+item.getItemId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getItemById() throws Exception {
        Item item = new Item().withItemId("newId").withText("testing text");
        Mockito.when(repository.findById(item.getItemId())).thenReturn(java.util.Optional.of(item));

        mvc.perform(MockMvcRequestBuilders
                .get("/items/" + item.getItemId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value("newId"));
    }
}
