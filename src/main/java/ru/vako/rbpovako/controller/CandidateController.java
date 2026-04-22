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
import ru.vako.rbpovako.service.RecruitingService;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {
    private final RecruitingService service;

    public CandidateController(RecruitingService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Candidate create(@RequestBody Candidate candidate) {
        return service.saveCandidate(candidate);
    }

    @GetMapping
    public List<Candidate> findAll() {
        return service.findCandidates();
    }

    @GetMapping("/{id}")
    public Candidate findById(@PathVariable Long id) {
        return service.findCandidate(id);
    }

    @PutMapping("/{id}")
    public Candidate update(@PathVariable Long id, @RequestBody Candidate candidate) {
        return service.updateCandidate(id, candidate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteCandidate(id);
    }
}
