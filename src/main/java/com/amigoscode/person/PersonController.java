package com.amigoscode.person;

import com.amigoscode.SortingOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/people")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public List<Person> getPeople(
            @RequestParam(
                    value = "sort",
                    required = false,
                    defaultValue = "ASC") SortingOrder sort) {
        return personService.getPeople(sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(personService.getPersonById(id));
    }

    @DeleteMapping("/{id}")
    public void deletePersonById(@Valid @Positive @PathVariable("id") Integer id) {
        personService.deletePersonById(id);
    }

    @PostMapping("")
    public void addPerson(@Valid @RequestBody NewPersonRequest person) {
        personService.addPerson(person);
    }

    @PutMapping("/{id}")
    public void updatePerson(@PathVariable("id") Integer id,
                             @RequestBody PersonUpdateRequest request) {
        personService.updatePerson(id, request);
    }
}
