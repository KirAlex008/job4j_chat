package ru.job4j.chat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.PersonRepository;
import ru.job4j.chat.repository.RoomRepository;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController

@RequestMapping("/person/{personId}/room")
public class RoomController {
    private RoomRepository roomRepository;
    private PersonRepository personRepository;

    public RoomController(RoomRepository roomRepository, PersonRepository personRepository) {
        this.roomRepository = roomRepository;
        this.personRepository = personRepository;
    }


    @GetMapping("/all")
    public List<Room> findAll(@PathVariable int personId) {
        Person person = personRepository.findPersonById(personId);
        return StreamSupport.stream(
                this.roomRepository.findRoomsByPersons(person).spliterator(), false
        ).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable int id) {
        var room = this.roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Room is not found. Please, check requisites."
                ));
        return new ResponseEntity<Room>(room, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@Valid @RequestBody Room room) {
        return new ResponseEntity<Room>(
                this.roomRepository.save(room),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
        this.roomRepository.save(room);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/patch")
    public Room path(@Valid @RequestBody Room room) throws InvocationTargetException, IllegalAccessException {
        var current = roomRepository.findById(room.getId());
        if (!current.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Patch<Room> patch = new Patch<>();
        roomRepository.save(patch.getPatch(current.get(), room));
        return current.get();
    }
}
