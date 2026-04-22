package ru.vako.rbpovako.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.vako.rbpovako.model.Offer;
import ru.vako.rbpovako.model.OfferStatus;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    boolean existsByCandidate_IdAndStatus(Long candidateId, OfferStatus status);

    List<Offer> findByCandidate_IdAndStatus(Long candidateId, OfferStatus status);
}
