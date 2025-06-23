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
            return personRepository.getPeople().stream().sorted(Comparator.comparing(Person::getId)).toList();
        }
        return personRepository.getPeople().stream().sorted(Comparator.comparing(Person::getId).reversed()).toList();
    }

    public Person getPersonById(Integer id) {
        return personRepository.getPeople().stream()
                .filter(p -> Objects.equals(p.getId(), id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " does not exist."));
    }

    public void deletePersonById(Integer id) {
        Person person = personRepository.getPeople().stream()
                .filter(p -> Objects.equals(p.getId(), id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " does not exist."));
        personRepository.getPeople().remove(person);
    }

    public void addPerson(NewPersonRequest person) {
        if(person.email() != null && !person.email().isEmpty()) {
            boolean exist = personRepository.getPeople().stream()
                    .anyMatch(p -> p.getEmail().equalsIgnoreCase(person.email()));
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
                .filter(person -> Objects.equals(person.getId(), id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " does not exist."));

        var index = personRepository.getPeople().indexOf(p);
        if (request.name() != null && !request.name().isEmpty() && !request.name().equals(p.getName())) {
            personRepository.getPeople().set(index, new Person(
                    p.getId(),
                    request.name(),
                    p.getAge(),
                    p.getGender(),
                    p.getEmail()
            ));
        }
        if (request.age() != null && !request.age().equals(p.getAge())) {
            Person person = new Person(
                    p.getId(),
                    p.getName(),
                    request.age(),
                    p.getGender(),
                    p.getEmail()
            );
            personRepository.getPeople().set(index, person);
        }
    }
}
