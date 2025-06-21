package com.amigoscode.person;

import com.amigoscode.SortingOrder;
import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getPeople(SortingOrder sort) {
        if (sort == SortingOrder.ASC) {
            return personRepository.getPeople().stream().sorted(Comparator.comparing(Person::id)).toList();
        }
        return personRepository.getPeople().stream().sorted(Comparator.comparing(Person::id).reversed()).toList();
    }

    public Person getPersonById(Integer id) {
        return personRepository.getPeople().stream()
                .filter(p -> Objects.equals(p.id(), id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " does not exist."));
    }

    public void deletePersonById(Integer id) {
        Person person = personRepository.getPeople().stream()
                .filter(p -> Objects.equals(p.id(), id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " does not exist."));
        personRepository.getPeople().remove(person);
    }

    public void addPerson(NewPersonRequest person) {
        if(person.email() != null && !person.email().isEmpty()) {
            boolean exist = personRepository.getPeople().stream()
                    .anyMatch(p -> p.email().equalsIgnoreCase(person.email()));
            if(exist) {
                throw new DuplicateResourceException("Email taken");
            }
        }
        personRepository.getPeople().add(new Person(
                personRepository.getIdCounter().incrementAndGet(),
                person.name(),
                person.age(),
                person.gender(),
                person.email()
        ));
    }

    public void updatePerson(Integer id, PersonUpdateRequest request) {
        Person p = personRepository.getPeople()
                .stream()
                .filter(person -> Objects.equals(person.id(), id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " does not exist."));

        var index = personRepository.getPeople().indexOf(p);
        if (request.name() != null && !request.name().isEmpty() && !request.name().equals(p.name())) {
            personRepository.getPeople().set(index, new Person(
                    p.id(),
                    request.name(),
                    p.age(),
                    p.gender(),
                    p.email()
            ));
        }
        if (request.age() != null && !request.age().equals(p.age())) {
            Person person = new Person(
                    p.id(),
                    p.name(),
                    request.age(),
                    p.gender(),
                    p.email()
            );
            personRepository.getPeople().set(index, person);
        }
    }
}
