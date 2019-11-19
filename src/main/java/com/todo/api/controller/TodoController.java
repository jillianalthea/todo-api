package com.todo.api.controller;


import com.todo.api.entities.Item;
import com.todo.api.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/items")
public class TodoController {

    @Autowired
    ItemRepository itemRepository;
//    private TodoService todoService;
//    @Autowired
//    public TodoController() {
//        this.todoService = new TodoService();
//    }

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return new ResponseEntity<>(itemRepository.findAll(), HttpStatus.OK);
    }

    @PutMapping

    public ResponseEntity putItem(@RequestBody Item item) {
        itemRepository.save(item);
        return new ResponseEntity<>(  HttpStatus.CREATED);
    }

}
