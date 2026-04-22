package ru.vako.rbpovako.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ru.vako.rbpovako.model.Application;
import ru.vako.rbpovako.model.Interview;
import ru.vako.rbpovako.model.Offer;
import ru.vako.rbpovako.service.RecruitingService;

@RestController
@RequestMapping("/api/business")
public class BusinessOperationController {
    private final RecruitingService service;

    public BusinessOperationController(RecruitingService service) {
        this.service = service;
    }

    @PostMapping("/applications/apply")
    @ResponseStatus(HttpStatus.CREATED)
    public Application applyForVacancy(@RequestBody ApplyRequest request) {
        return service.applyForVacancy(request.vacancyId(), request.candidateId());
    }

    @PostMapping("/interviews/schedule")
    @ResponseStatus(HttpStatus.CREATED)
    public Interview scheduleInterview(@RequestBody ScheduleInterviewRequest request) {
        return service.scheduleInterview(
                request.applicationId(),
                request.interviewer(),
                request.startsAt(),
                request.endsAt()
        );
    }

    @PostMapping("/interviews/{id}/complete")
    public Interview completeInterview(@PathVariable Long id, @RequestBody CompleteInterviewRequest request) {
        return service.completeInterview(id, request.successful());
    }

    @PostMapping("/offers/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Offer createOffer(@RequestBody CreateOfferRequest request) {
        return service.createOffer(request.applicationId(), request.salary());
    }

    @PostMapping("/offers/{id}/accept")
    public Offer acceptOffer(@PathVariable Long id) {
        return service.acceptOffer(id);
    }

    public record ApplyRequest(Long vacancyId, Long candidateId) {
    }

    public record ScheduleInterviewRequest(
            Long applicationId,
            String interviewer,
            LocalDateTime startsAt,
            LocalDateTime endsAt
    ) {
    }

    public record CompleteInterviewRequest(boolean successful) {
    }

    public record CreateOfferRequest(Long applicationId, BigDecimal salary) {
    }
}
