package ru.vako.rbpovako.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.vako.rbpovako.model.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
