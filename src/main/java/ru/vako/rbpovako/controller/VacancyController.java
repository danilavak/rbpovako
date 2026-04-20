package ru.vako.rbpovako.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ru.vako.rbpovako.model.Vacancy;
import ru.vako.rbpovako.service.RecruitingStorage;

@RestController
@RequestMapping("/api/vacancies")
public class VacancyController {
    private final RecruitingStorage storage;

    public VacancyController(RecruitingStorage storage) {
        this.storage = storage;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vacancy create(@RequestBody Vacancy vacancy) {
        return storage.vacancies().create(vacancy);
    }

    @GetMapping
    public List<Vacancy> findAll() {
        return storage.vacancies().findAll();
    }

    @GetMapping("/{id}")
    public Vacancy findById(@PathVariable Long id) {
        return storage.vacancies().findById(id);
    }

    @PutMapping("/{id}")
    public Vacancy update(@PathVariable Long id, @RequestBody Vacancy vacancy) {
        return storage.vacancies().update(id, vacancy);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        storage.vacancies().delete(id);
    }
}
