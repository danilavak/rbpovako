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
import ru.vako.rbpovako.service.RecruitingService;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final RecruitingService service;

    public ApplicationController(RecruitingService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Application create(@RequestBody Application application) {
        return service.saveApplication(application);
    }

    @GetMapping
    public List<Application> findAll() {
        return service.findApplications();
    }

    @GetMapping("/{id}")
    public Application findById(@PathVariable Long id) {
        return service.findApplication(id);
    }

    @PutMapping("/{id}")
    public Application update(@PathVariable Long id, @RequestBody Application application) {
        return service.updateApplication(id, application);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteApplication(id);
    }
}
