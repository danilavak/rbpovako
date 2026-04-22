package ru.vako.rbpovako.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ru.vako.rbpovako.model.Application;
import ru.vako.rbpovako.model.ApplicationStatus;
import ru.vako.rbpovako.model.Candidate;
import ru.vako.rbpovako.model.Interview;
import ru.vako.rbpovako.model.InterviewStatus;
import ru.vako.rbpovako.model.Offer;
import ru.vako.rbpovako.model.OfferStatus;
import ru.vako.rbpovako.model.Vacancy;
import ru.vako.rbpovako.model.VacancyStatus;
import ru.vako.rbpovako.repository.ApplicationRepository;
import ru.vako.rbpovako.repository.CandidateRepository;
import ru.vako.rbpovako.repository.InterviewRepository;
import ru.vako.rbpovako.repository.OfferRepository;
import ru.vako.rbpovako.repository.VacancyRepository;

@Service
public class RecruitingService {
    private final VacancyRepository vacancyRepository;
    private final CandidateRepository candidateRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final OfferRepository offerRepository;

    public RecruitingService(
            VacancyRepository vacancyRepository,
            CandidateRepository candidateRepository,
            ApplicationRepository applicationRepository,
            InterviewRepository interviewRepository,
            OfferRepository offerRepository
    ) {
        this.vacancyRepository = vacancyRepository;
        this.candidateRepository = candidateRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
        this.offerRepository = offerRepository;
    }

    public List<Vacancy> findVacancies() {
        return vacancyRepository.findAll();
    }

    public Vacancy findVacancy(Long id) {
        return vacancyRepository.findById(id).orElseThrow(() -> notFound("Вакансия не найдена"));
    }

    public Vacancy saveVacancy(Vacancy vacancy) {
        return vacancyRepository.save(vacancy);
    }

    public Vacancy updateVacancy(Long id, Vacancy vacancy) {
        findVacancy(id);
        vacancy.setId(id);
        return vacancyRepository.save(vacancy);
    }

    public void deleteVacancy(Long id) {
        vacancyRepository.delete(findVacancy(id));
    }

    public List<Candidate> findCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate findCandidate(Long id) {
        return candidateRepository.findById(id).orElseThrow(() -> notFound("Кандидат не найден"));
    }

    public Candidate saveCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Candidate updateCandidate(Long id, Candidate candidate) {
        findCandidate(id);
        candidate.setId(id);
        return candidateRepository.save(candidate);
    }

    public void deleteCandidate(Long id) {
        candidateRepository.delete(findCandidate(id));
    }

    public List<Application> findApplications() {
        return applicationRepository.findAll();
    }

    public Application findApplication(Long id) {
        return applicationRepository.findById(id).orElseThrow(() -> notFound("Отклик не найден"));
    }

    @Transactional
    public Application saveApplication(Application application) {
        fillApplicationLinks(application);
        return applicationRepository.save(application);
    }

    @Transactional
    public Application updateApplication(Long id, Application application) {
        findApplication(id);
        application.setId(id);
        fillApplicationLinks(application);
        return applicationRepository.save(application);
    }

    public void deleteApplication(Long id) {
        applicationRepository.delete(findApplication(id));
    }

    public List<Interview> findInterviews() {
        return interviewRepository.findAll();
    }

    public Interview findInterview(Long id) {
        return interviewRepository.findById(id).orElseThrow(() -> notFound("Интервью не найдено"));
    }

    @Transactional
    public Interview saveInterview(Interview interview) {
        fillInterviewLinks(interview);
        return interviewRepository.save(interview);
    }

    @Transactional
    public Interview updateInterview(Long id, Interview interview) {
        findInterview(id);
        interview.setId(id);
        fillInterviewLinks(interview);
        return interviewRepository.save(interview);
    }

    public void deleteInterview(Long id) {
        interviewRepository.delete(findInterview(id));
    }

    public List<Offer> findOffers() {
        return offerRepository.findAll();
    }

    public Offer findOffer(Long id) {
        return offerRepository.findById(id).orElseThrow(() -> notFound("Оффер не найден"));
    }

    @Transactional
    public Offer saveOffer(Offer offer) {
        fillOfferLinks(offer);
        return offerRepository.save(offer);
    }

    @Transactional
    public Offer updateOffer(Long id, Offer offer) {
        findOffer(id);
        offer.setId(id);
        fillOfferLinks(offer);
        return offerRepository.save(offer);
    }

    public void deleteOffer(Long id) {
        offerRepository.delete(findOffer(id));
    }

    @Transactional
    public Application applyForVacancy(Long vacancyId, Long candidateId) {
        Vacancy vacancy = findVacancy(vacancyId);
        Candidate candidate = findCandidate(candidateId);
        if (vacancy.getStatus() != VacancyStatus.OPEN) {
            throw badRequest("Нельзя откликнуться на закрытую вакансию");
        }
        if (applicationRepository.existsByVacancy_IdAndCandidate_Id(vacancyId, candidateId)) {
            throw badRequest("Кандидат уже откликнулся на эту вакансию");
        }
        Application application = new Application();
        application.setVacancy(vacancy);
        application.setCandidate(candidate);
        application.setStatus(ApplicationStatus.NEW);
        application.setCreatedAt(LocalDateTime.now());
        return applicationRepository.save(application);
    }

    @Transactional
    public Interview scheduleInterview(Long applicationId, String interviewer, LocalDateTime startsAt, LocalDateTime endsAt) {
        if (startsAt == null || endsAt == null || !endsAt.isAfter(startsAt)) {
            throw badRequest("Время окончания интервью должно быть позже времени начала");
        }
        if (interviewRepository.existsByInterviewerAndStatusAndStartsAtLessThanAndEndsAtGreaterThan(
                interviewer, InterviewStatus.PLANNED, endsAt, startsAt)) {
            throw badRequest("У интервьюера уже есть интервью в это время");
        }
        Application application = findApplication(applicationId);
        application.setStatus(ApplicationStatus.INTERVIEW);
        Interview interview = new Interview();
        interview.setApplication(application);
        interview.setInterviewer(interviewer);
        interview.setStartsAt(startsAt);
        interview.setEndsAt(endsAt);
        interview.setStatus(InterviewStatus.PLANNED);
        applicationRepository.save(application);
        return interviewRepository.save(interview);
    }

    @Transactional
    public Interview completeInterview(Long interviewId, boolean successful) {
        Interview interview = findInterview(interviewId);
        interview.setStatus(InterviewStatus.COMPLETED);
        Application application = interview.getApplication();
        application.setStatus(successful ? ApplicationStatus.HIRED : ApplicationStatus.REJECTED);
        applicationRepository.save(application);
        return interviewRepository.save(interview);
    }

    @Transactional
    public Offer createOffer(Long applicationId, BigDecimal salary) {
        Application application = findApplication(applicationId);
        if (!interviewRepository.existsByApplication_IdAndStatus(applicationId, InterviewStatus.COMPLETED)) {
            throw badRequest("Оффер можно создать только после завершенного интервью");
        }
        Offer offer = new Offer();
        offer.setApplication(application);
        offer.setCandidate(application.getCandidate());
        offer.setSalary(salary);
        offer.setStatus(OfferStatus.SENT);
        offer.setCreatedAt(LocalDateTime.now());
        return offerRepository.save(offer);
    }

    @Transactional
    public Offer acceptOffer(Long offerId) {
        Offer offer = findOffer(offerId);
        Long candidateId = offer.getCandidate().getId();
        if (offerRepository.existsByCandidate_IdAndStatus(candidateId, OfferStatus.ACCEPTED)) {
            throw badRequest("Кандидат уже принял другой оффер");
        }
        offer.setStatus(OfferStatus.ACCEPTED);
        List<Offer> otherOffers = offerRepository.findByCandidate_IdAndStatus(candidateId, OfferStatus.SENT);
        for (Offer otherOffer : otherOffers) {
            if (!otherOffer.getId().equals(offerId)) {
                otherOffer.setStatus(OfferStatus.DECLINED);
            }
        }
        offerRepository.saveAll(otherOffers);
        return offerRepository.save(offer);
    }

    private void fillApplicationLinks(Application application) {
        application.setVacancy(findVacancy(required(application.getVacancyId(), "vacancyId")));
        application.setCandidate(findCandidate(required(application.getCandidateId(), "candidateId")));
        if (application.getCreatedAt() == null) {
            application.setCreatedAt(LocalDateTime.now());
        }
    }

    private void fillInterviewLinks(Interview interview) {
        interview.setApplication(findApplication(required(interview.getApplicationId(), "applicationId")));
    }

    private void fillOfferLinks(Offer offer) {
        Application application = findApplication(required(offer.getApplicationId(), "applicationId"));
        offer.setApplication(application);
        Long candidateId = offer.getCandidateId() != null ? offer.getCandidateId() : application.getCandidate().getId();
        offer.setCandidate(findCandidate(candidateId));
        if (offer.getCreatedAt() == null) {
            offer.setCreatedAt(LocalDateTime.now());
        }
    }

    private Long required(Long value, String field) {
        if (value == null) {
            throw badRequest("Поле " + field + " обязательно");
        }
        return value;
    }

    private ResponseStatusException notFound(String message) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }

    private ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
