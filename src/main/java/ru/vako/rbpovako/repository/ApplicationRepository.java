package ru.vako.rbpovako.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.vako.rbpovako.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByVacancy_IdAndCandidate_Id(Long vacancyId, Long candidateId);
}
