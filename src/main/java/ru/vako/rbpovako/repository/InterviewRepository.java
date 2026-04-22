package ru.vako.rbpovako.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.vako.rbpovako.model.Interview;
import ru.vako.rbpovako.model.InterviewStatus;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    boolean existsByInterviewerAndStatusAndStartsAtLessThanAndEndsAtGreaterThan(
            String interviewer,
            InterviewStatus status,
            LocalDateTime endsAt,
            LocalDateTime startsAt
    );

    boolean existsByApplication_IdAndStatus(Long applicationId, InterviewStatus status);
}
