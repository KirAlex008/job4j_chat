package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;

import java.util.List;
import java.util.Set;

public interface RoomRepository extends CrudRepository<Room, Integer> {
    List<Room> findRoomsByPersons(Person person);
}
