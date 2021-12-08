package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;

import java.util.Optional;
import java.util.Set;

public interface PersonRepository extends CrudRepository<Person, Integer> {
    Person findByLogin(String login);

    Person findPersonById(int id);
}
