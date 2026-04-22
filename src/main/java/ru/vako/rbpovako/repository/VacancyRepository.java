package ru.vako.rbpovako.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.vako.rbpovako.model.Vacancy;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
}
