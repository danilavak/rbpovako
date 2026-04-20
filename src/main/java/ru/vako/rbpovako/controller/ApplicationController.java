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

import ru.vako.rbpovako.model.Application;
import ru.vako.rbpovako.service.RecruitingStorage;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final RecruitingStorage storage;

    public ApplicationController(RecruitingStorage storage) {
        this.storage = storage;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Application create(@RequestBody Application application) {
        return storage.applications().create(application);
    }

    @GetMapping
    public List<Application> findAll() {
        return storage.applications().findAll();
    }

    @GetMapping("/{id}")
    public Application findById(@PathVariable Long id) {
        return storage.applications().findById(id);
    }

    @PutMapping("/{id}")
    public Application update(@PathVariable Long id, @RequestBody Application application) {
        return storage.applications().update(id, application);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        storage.applications().delete(id);
    }
}
