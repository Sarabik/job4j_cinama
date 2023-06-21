package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.DtoFilmSession;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.FilmSessionRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Service
public class SimpleFilmSessionService implements FilmSessionService {

    private final FilmSessionRepository filmSessionRepository;

    private final FilmService filmService;

    private final HallService hallService;

    public SimpleFilmSessionService(FilmSessionRepository filmSessionRepository,
                                    FilmService filmService,
                                    HallService hallService) {
        this.filmSessionRepository = filmSessionRepository;
        this.filmService = filmService;
        this.hallService = hallService;
    }

    private DtoFilmSession fromFilmSessionToDto(FilmSession session) {
        DtoFilmSession dtoFilmSession = null;
        Optional<Film> optionalFilm = filmService.findById(session.getFilmId());
        Optional<Hall> optionalHall = hallService.getById(session.getHallId());
        if (optionalFilm.isPresent() && optionalHall.isPresent()) {
            dtoFilmSession = new DtoFilmSession(optionalFilm.get(),
                    optionalHall.get(), session);
        }
        return dtoFilmSession;
    }

    private Optional<DtoFilmSession> fromOptFilmSessionToDto(Optional<FilmSession> optFilmSession) {
        if (optFilmSession.isPresent()) {
            return Optional.of(fromFilmSessionToDto(optFilmSession.get()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<DtoFilmSession> getById(int id) {
        return fromOptFilmSessionToDto(filmSessionRepository.getById(id));
    }

    @Override
    public Collection<DtoFilmSession> findAll() {
        return filmSessionRepository.findAll().stream()
                .map(this::fromFilmSessionToDto)
                .sorted(Comparator.comparing(DtoFilmSession::getStartTime))
                .toList();
    }

    @Override
    public Collection<DtoFilmSession> getAllByFilmId(int filmId) {
        return filmSessionRepository.getAllByFilmId(filmId).stream()
                .map(this::fromFilmSessionToDto)
                .sorted(Comparator.comparing(DtoFilmSession::getStartTime))
                .toList();
    }
}
