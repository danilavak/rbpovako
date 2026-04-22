package ru.vako.rbpovako.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ru.vako.rbpovako.model.Candidate;
import ru.vako.rbpovako.model.Vacancy;
import ru.vako.rbpovako.model.VacancyStatus;
import ru.vako.rbpovako.repository.CandidateRepository;
import ru.vako.rbpovako.repository.VacancyRepository;

@Component
public class DemoDataLoader implements CommandLineRunner {
    private final VacancyRepository vacancyRepository;
    private final CandidateRepository candidateRepository;

    public DemoDataLoader(VacancyRepository vacancyRepository, CandidateRepository candidateRepository) {
        this.vacancyRepository = vacancyRepository;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public void run(String... args) {
        if (vacancyRepository.count() == 0) {
            Vacancy javaVacancy = new Vacancy();
            javaVacancy.setTitle("Java developer");
            javaVacancy.setDescription("Разработка Spring Boot сервиса");
            javaVacancy.setDepartment("IT");
            javaVacancy.setSalaryFrom(BigDecimal.valueOf(120000));
            javaVacancy.setSalaryTo(BigDecimal.valueOf(180000));
            javaVacancy.setStatus(VacancyStatus.OPEN);
            vacancyRepository.save(javaVacancy);

            Vacancy analystVacancy = new Vacancy();
            analystVacancy.setTitle("HR analyst");
            analystVacancy.setDescription("Анализ воронки подбора");
            analystVacancy.setDepartment("HR");
            analystVacancy.setSalaryFrom(BigDecimal.valueOf(90000));
            analystVacancy.setSalaryTo(BigDecimal.valueOf(130000));
            analystVacancy.setStatus(VacancyStatus.OPEN);
            vacancyRepository.save(analystVacancy);
        }

        if (candidateRepository.count() == 0) {
            Candidate firstCandidate = new Candidate();
            firstCandidate.setFullName("Ivan Petrov");
            firstCandidate.setEmail("ivan.petrov@example.com");
            firstCandidate.setPhone("+79990000000");
            firstCandidate.setResume("Java, SQL, Spring");
            candidateRepository.save(firstCandidate);

            Candidate secondCandidate = new Candidate();
            secondCandidate.setFullName("Anna Sidorova");
            secondCandidate.setEmail("anna.sidorova@example.com");
            secondCandidate.setPhone("+79991111111");
            secondCandidate.setResume("HR, analytics, onboarding");
            candidateRepository.save(secondCandidate);
        }
    }
}
