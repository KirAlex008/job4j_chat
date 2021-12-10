package ru.job4j.chat.repository;

import junit.framework.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j.chat.model.Person;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@RunWith(SpringRunner.class)

class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void whenCreatePerson() {
        Person person = new Person("test@mail.ru", "123");
        personRepository.save(person);
        Person result = personRepository.findPersonById(person.getId());
        Assert.assertEquals(person, result);
    }

/*    @Test
    public void whenUpdate() {
        Person person = new Person("test@mail.ru", "123");
        personRepository.save(person);
        Person updated = new Person("test2@mail.ru", "1234");
        updated.setId(person.getId());
        personRepository.save(updated);
        Person result = personRepository.findById(person.getId()).orElse(new Person());
        Assert.assertEquals(updated, result);
    }

    @Test
    public void whenDelete() {
        Person person = new Person("test@mail.ru", "123");
        personRepository.save(person);
        personRepository.deleteById(person.getId());
        Assert.assertEquals(
                Optional.empty(),
                personRepository.findById(person.getId())
        );
    }

    @Test
    public void whenFindAll() {
        Person person1 = new Person("test@mail.ru", "123");
        Person person2 = new Person("test@mail.ru", "123");
        Person person3 = new Person("test@mail.ru", "123");
        List<Person> input = List.of(person1, person2, person3);
        personRepository.saveAll(input);
        Assert.assertEquals(
                List.of(person1, person2, person3),
                personRepository.findAll()
        );
    }*/

/*    @Test
    public void whenFindParticipant() {
        Person person = new Person("test", "test@mail.ru", "123");
        personRepository.save(person);
        Room room1 = roomRepository.save(new Room(1));
        Room room2 = roomRepository.save(new Room(2));
        roomRepository.addRelation(person, room1);
        roomRepository.addRelation(person, room2);
        Assert.assertEquals(
                List.of(person),
                personRepository.findByRoomsContains(room1)
        );
        Assert.assertEquals(
                List.of(person),
                personRepository.findByRoomsContains(room2)
        );
        Assert.assertEquals(
                List.of(),
                personRepository.findByRoomsContains(new Room(3))
        );
    }*/
}
