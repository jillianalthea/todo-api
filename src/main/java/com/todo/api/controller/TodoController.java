package com.todo.api.controller;


import com.todo.api.entities.Item;
import com.todo.api.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public ResponseEntity<Item> putItem(@RequestBody Item item) {
        return new ResponseEntity<>(itemRepository.save(createNewItem(item)), HttpStatus.CREATED);
    }

    @PostMapping("/{itemId}")
    public ResponseEntity<Item> postItem(@PathVariable String itemId, @RequestBody Item item) {
        Optional<Item> existingItemOpt = itemRepository.findById(itemId);
        if (existingItemOpt.isPresent()) {
            Item existingItem = existingItemOpt.get();
            Item postingItem = new Item();
            postingItem.setItemId(itemId);
            postingItem.setCreationDate(existingItem.getCreationDate());
            postingItem.setPriority(item.getPriority());
            postingItem.setComplete(item.isComplete());
            postingItem.setText(item.getText());
            if(item.isComplete() && !existingItem.isComplete()) {
                postingItem.setCompleteDate(new Date());
            }
            return new ResponseEntity<>(itemRepository.save(postingItem), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(itemRepository.save(createNewItem(item)), HttpStatus.CREATED);
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

    private Item createNewItem(Item item) {
        Item postingItem = new Item();
        postingItem.setText(item.getText());
        postingItem.setComplete(false);
        postingItem.setCompleteDate(null);
        postingItem.setPriority(item.getPriority());

        return postingItem;
    }
}
