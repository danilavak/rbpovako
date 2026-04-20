package ru.vako.rbpovako.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import ru.vako.rbpovako.model.Identifiable;

public class InMemoryCrudService<T extends Identifiable> {
    private final AtomicLong sequence = new AtomicLong();
    private final Map<Long, T> storage = new ConcurrentHashMap<>();

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public T findById(Long id) {
        T entity = storage.get(id);
        if (entity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись не найдена");
        }
        return entity;
    }

    public T create(T entity) {
        Long id = sequence.incrementAndGet();
        entity.setId(id);
        storage.put(id, entity);
        return entity;
    }

    public T update(Long id, T entity) {
        if (!storage.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись не найдена");
        }
        entity.setId(id);
        storage.put(id, entity);
        return entity;
    }

    public void delete(Long id) {
        if (storage.remove(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись не найдена");
        }
    }
}
