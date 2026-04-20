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

import ru.vako.rbpovako.model.Candidate;
import ru.vako.rbpovako.service.RecruitingStorage;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {
    private final RecruitingStorage storage;

    public CandidateController(RecruitingStorage storage) {
        this.storage = storage;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Candidate create(@RequestBody Candidate candidate) {
        return storage.candidates().create(candidate);
    }

    @GetMapping
    public List<Candidate> findAll() {
        return storage.candidates().findAll();
    }

    @GetMapping("/{id}")
    public Candidate findById(@PathVariable Long id) {
        return storage.candidates().findById(id);
    }

    @PutMapping("/{id}")
    public Candidate update(@PathVariable Long id, @RequestBody Candidate candidate) {
        return storage.candidates().update(id, candidate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        storage.candidates().delete(id);
    }
}
