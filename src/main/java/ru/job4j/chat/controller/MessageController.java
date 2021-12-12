package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.repository.MessageRepository;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/")
    public List<Message> findAll() {
        return StreamSupport.stream(
                this.messageRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        var message = this.messageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Message is not found. Please, check requisites."
        ));
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Message> create(@Valid @RequestBody Message message) {
        return new ResponseEntity<Message>(
                this.messageRepository.save(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@Valid @RequestBody Message message) {
        this.messageRepository.save(message);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/patch")
    public Message path(@Valid @RequestBody Message message) throws InvocationTargetException, IllegalAccessException {
        var current = messageRepository.findById(message.getId());
        if (!current.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Patch<Message> patch = new Patch<>();
        messageRepository.save(patch.getPatch(current.get(), message));
        return current.get();
    }
}
