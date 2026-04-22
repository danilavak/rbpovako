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

import ru.vako.rbpovako.model.Offer;
import ru.vako.rbpovako.service.RecruitingService;

@RestController
@RequestMapping("/api/offers")
public class OfferController {
    private final RecruitingService service;

    public OfferController(RecruitingService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Offer create(@RequestBody Offer offer) {
        return service.saveOffer(offer);
    }

    @GetMapping
    public List<Offer> findAll() {
        return service.findOffers();
    }

    @GetMapping("/{id}")
    public Offer findById(@PathVariable Long id) {
        return service.findOffer(id);
    }

    @PutMapping("/{id}")
    public Offer update(@PathVariable Long id, @RequestBody Offer offer) {
        return service.updateOffer(id, offer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteOffer(id);
    }
}
