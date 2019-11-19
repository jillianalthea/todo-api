package com.todo.api.controller;


import com.todo.api.entities.Item;
import com.todo.api.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/items")
public class TodoController {

    private ItemRepository itemRepository;

    @Autowired
    public TodoController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return new ResponseEntity<>(itemRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItemById(@PathVariable String itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            return new ResponseEntity<>(optionalItem.get(), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity putItem(@RequestBody Item item) {
        itemRepository.save(item);
        return new ResponseEntity<>(HttpStatus.CREATED);
        // TODO: can return item from here
    }

    @PostMapping
    public ResponseEntity<Item> postItem(@RequestBody Item item) {
        Optional<Item> existingItemOpt = itemRepository.findById(item.getItemId());
        if (existingItemOpt.isPresent()) {
            return new ResponseEntity<>(itemRepository.save(item.withItemId(existingItemOpt.get().getItemId())), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(itemRepository.save(item), HttpStatus.CREATED);
        }

    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity deleteItemById(@PathVariable String itemId) {
        Optional<Item> existingItemOpt = itemRepository.findById(itemId);
        if (existingItemOpt.isPresent()) {
            itemRepository.delete(existingItemOpt.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
