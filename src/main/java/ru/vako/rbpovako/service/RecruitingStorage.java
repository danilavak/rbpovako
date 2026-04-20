package ru.vako.rbpovako.service;

import org.springframework.stereotype.Service;

import ru.vako.rbpovako.model.Application;
import ru.vako.rbpovako.model.Candidate;
import ru.vako.rbpovako.model.Interview;
import ru.vako.rbpovako.model.Offer;
import ru.vako.rbpovako.model.Vacancy;

@Service
public class RecruitingStorage {
    private final InMemoryCrudService<Vacancy> vacancies = new InMemoryCrudService<>();
    private final InMemoryCrudService<Candidate> candidates = new InMemoryCrudService<>();
    private final InMemoryCrudService<Application> applications = new InMemoryCrudService<>();
    private final InMemoryCrudService<Interview> interviews = new InMemoryCrudService<>();
    private final InMemoryCrudService<Offer> offers = new InMemoryCrudService<>();

    public InMemoryCrudService<Vacancy> vacancies() {
        return vacancies;
    }

    public InMemoryCrudService<Candidate> candidates() {
        return candidates;
    }

    public InMemoryCrudService<Application> applications() {
        return applications;
    }

    public InMemoryCrudService<Interview> interviews() {
        return interviews;
    }

    public InMemoryCrudService<Offer> offers() {
        return offers;
    }
}
