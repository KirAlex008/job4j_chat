package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.repository.PersonRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController

@RequestMapping("/person")
public class PersonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getSimpleName());

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private final ObjectMapper objectMapper;

    public PersonController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostMapping("/login")
    public Person loginPerson(@Valid @RequestBody Person person, HttpServletRequest request) {
        return personRepository.findByLogin(person.getLogin());
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return StreamSupport.stream(
                this.personRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.personRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Person is not found. Please, check requisites."
        ));
        return new ResponseEntity<Person>(person, HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        var pass = person.getPassword();
        if (pass.length() < 6) {
            throw new IllegalArgumentException("Invalid password. Password length must be more than 5 characters.");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        return new ResponseEntity<Person>(
                this.personRepository.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@Valid @RequestBody Person person) {
        this.personRepository.save(person);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
            put("message", e.getMessage());
            put("type", e.getClass());
        }
        }));
        LOGGER.error(e.getLocalizedMessage());
    }

    @PatchMapping("/patch")
    public Person path(@Valid @RequestBody Person person) throws InvocationTargetException, IllegalAccessException {
        var current = personRepository.findById(person.getId());
        if (!current.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Patch<Person> patch = new Patch<>();
        personRepository.save(patch.getPatch(current.get(), person));
        return current.get();
    }
}
