package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.DtoFilmSession;

import java.util.Collection;
import java.util.Optional;

public interface FilmSessionService {

    Optional<DtoFilmSession> getById(int id);

    Collection<DtoFilmSession> findAll();

    Collection<DtoFilmSession> getAllByFilmId(int filmId);

}
