package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.DtoFilmSession;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.service.FilmSessionService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmSessionControllerTest {

    private FilmSessionController filmSessionController;

    private FilmSessionService filmSessionService;

    @BeforeEach
    public void initServices() {
        filmSessionService = mock(FilmSessionService.class);
        filmSessionController = new FilmSessionController(filmSessionService);
    }

    @Test
    public void whenRequestFilmSessionListPageThenGetPageWithAllFilmSessions() {
        LocalDateTime start = LocalDateTime.of(2023, 7, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 7, 1, 12, 15);
        FilmSession filmSession1 = new FilmSession(1, 1, 1, start, end, 6);
        Film film1 = new Film(filmSession1.getFilmId(), "name1", "description1", 2001, "Comedy", 16, 100, 1);
        Hall hall1 = new Hall(filmSession1.getHallId(), "hall1", 8, 14, "description1");
        DtoFilmSession dtoFilmSession = new DtoFilmSession(film1, hall1, filmSession1);
        Collection<DtoFilmSession> expectedFilmSessions = List.of(dtoFilmSession);
        when(filmSessionService.findAll()).thenReturn(expectedFilmSessions);

        ConcurrentModel model = new ConcurrentModel();
        String view = filmSessionController.getAll(model);
        Object actualFilmSessions = model.getAttribute("sessions");

        assertThat(view).isEqualTo("sessions/list");
        assertThat(actualFilmSessions).usingRecursiveComparison().isEqualTo(expectedFilmSessions);
    }

    @Test
    public void whenRequestFilmInfoPageThenGetPageWithFilmInfo() {
        LocalDateTime start = LocalDateTime.of(2023, 7, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 7, 1, 12, 15);
        FilmSession filmSession1 = new FilmSession(1, 1, 1, start, end, 6);
        FilmSession filmSession2 = new FilmSession(2, 1, 2, start, end, 6);
        Film film1 = new Film(filmSession1.getFilmId(), "name1", "description1", 2001, "Comedy", 16, 100, 1);
        Hall hall1 = new Hall(filmSession1.getHallId(), "hall1", 8, 14, "description1");
        Hall hall2 = new Hall(filmSession2.getHallId(), "hall2", 8, 14, "description2");
        DtoFilmSession dtoFilmSession1 = new DtoFilmSession(film1, hall1, filmSession1);
        DtoFilmSession dtoFilmSession2 = new DtoFilmSession(film1, hall2, filmSession2);
        Collection<DtoFilmSession> expectedFilmSessions = List.of(dtoFilmSession1, dtoFilmSession2);
        when(filmSessionService.getAllByFilmId(1)).thenReturn(expectedFilmSessions);

        ConcurrentModel model = new ConcurrentModel();
        String view = filmSessionController.getFilmSessions(model, film1.getId());
        Object actualFilmSessions = model.getAttribute("sessions");

        assertThat(view).isEqualTo("sessions/film");
        assertThat(actualFilmSessions).usingRecursiveComparison().isEqualTo(expectedFilmSessions);
    }

}