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

import ru.vako.rbpovako.model.Interview;
import ru.vako.rbpovako.service.RecruitingStorage;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {
    private final RecruitingStorage storage;

    public InterviewController(RecruitingStorage storage) {
        this.storage = storage;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Interview create(@RequestBody Interview interview) {
        return storage.interviews().create(interview);
    }

    @GetMapping
    public List<Interview> findAll() {
        return storage.interviews().findAll();
    }

    @GetMapping("/{id}")
    public Interview findById(@PathVariable Long id) {
        return storage.interviews().findById(id);
    }

    @PutMapping("/{id}")
    public Interview update(@PathVariable Long id, @RequestBody Interview interview) {
        return storage.interviews().update(id, interview);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        storage.interviews().delete(id);
    }
}
