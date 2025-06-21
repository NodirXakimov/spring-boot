package com.amigoscode.person;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class PersonRepository {

    private final AtomicInteger idCounter = new AtomicInteger(0);

    private final List<Person> people = new ArrayList<>();

    {
        people.add(new Person(idCounter.incrementAndGet(), "Nodir", 29, Gender.MALE, "nodir@gmail.com"));
        people.add(new Person(idCounter.incrementAndGet(), "Adham", 25, Gender.MALE, "adham@gmail.com"));
        people.add(new Person(idCounter.incrementAndGet(), "Xasan", 20, Gender.MALE, "xasan@gmail.com"));
        people.add(new Person(idCounter.incrementAndGet(), "Xusan", 20, Gender.MALE, "xusan@gmail.com"));
    }

    public AtomicInteger getIdCounter() {
        return idCounter;
    }

    public List<Person> getPeople() {
        return people;
    }
}
