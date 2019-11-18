package com.todo.api.controller;


import com.todo.api.entities.Todo;
import com.todo.api.repos.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/todo")
public class APIController {

    @Autowired
    TodoRepository todoRepository;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getOneTodo(@PathVariable String id) {
        return new ResponseEntity(todoRepository.findById(id).orElse(null), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllTodos() {
        return new ResponseEntity(todoRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity postTodo(@RequestBody Todo todo) {
        return new ResponseEntity(todoRepository.save(todo), HttpStatus.CREATED);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity editTodo(@RequestBody Todo todo) {
        return new ResponseEntity(todoRepository.save(todo), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteTodo(@PathVariable String id) {
        todoRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
