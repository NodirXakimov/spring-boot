package com.amigoscode.person;

import com.amigoscode.SortingOrder;
import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getPeople(SortingOrder sort) {
        return personRepository.findAll(Sort.by(Sort.Direction.valueOf(sort.name()), "id"));
    }

    public Person getPersonById(Integer id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " does not exist."));
    }

    public void deletePersonById(Integer id) {
        boolean existById = personRepository.existsById(id);
        if (!existById) {
            throw new ResourceNotFoundException("Person with id " + id + " does not exist.");
        }
        personRepository.deleteById(id);
    }

    public void addPerson(NewPersonRequest personRequest) {
        if (personRequest.email() != null && !personRequest.email().isEmpty()) {
            boolean exist = personRepository.existsByEmail(personRequest.email());
            if (exist) {
                throw new DuplicateResourceException("Email taken");
            }
        }
        Person person = new Person(
                personRequest.name(),
                personRequest.age(),
                personRequest.gender(),
                personRequest.email()
        );
        personRepository.save(person);
    }

    public void updatePerson(Integer id, PersonUpdateRequest request) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " does not exist."));

        if (request.name() != null && !request.name().isEmpty() && !request.name().equals(person.getName())) {
            person.setName(request.name());
            personRepository.save(person);
        }
        if (request.age() != null && !request.age().equals(person.getAge())) {
            person.setAge(request.age());
            personRepository.save(person);
        }
        if (request.email() != null && !request.email().equals(person.getEmail())) {
            person.setEmail(request.email());
            personRepository.save(person);
        }
    }
}
