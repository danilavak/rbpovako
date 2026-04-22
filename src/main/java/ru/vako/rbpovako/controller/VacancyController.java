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
import ru.vako.rbpovako.service.RecruitingService;

@RestController
@RequestMapping("/api/vacancies")
public class VacancyController {
    private final RecruitingService service;

    public VacancyController(RecruitingService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vacancy create(@RequestBody Vacancy vacancy) {
        return service.saveVacancy(vacancy);
    }

    @GetMapping
    public List<Vacancy> findAll() {
        return service.findVacancies();
    }

    @GetMapping("/{id}")
    public Vacancy findById(@PathVariable Long id) {
        return service.findVacancy(id);
    }

    @PutMapping("/{id}")
    public Vacancy update(@PathVariable Long id, @RequestBody Vacancy vacancy) {
        return service.updateVacancy(id, vacancy);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteVacancy(id);
    }
}
