package ru.job4j.cinema.controller;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.service.FilmService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmControllerTest {

    private FilmController filmController;

    private FilmService filmService;

    @BeforeEach
    public void initServices() {
        filmService = mock(FilmService.class);
        filmController = new FilmController(filmService);
    }

    @Test
    public void whenRequestFilmListPageThenGetPageWithAllFilms() {
        Film film1 = new Film(1, "name1", "description1", 2001, "Comedy", 16, 100, 1);
        Film film2 = new Film(2, "name2", "description2", 2002, "Comedy", 16, 120, 2);
        Collection<Film> expectedFilms = List.of(film1, film2);
        when(filmService.findAll()).thenReturn(expectedFilms);

        ConcurrentModel model = new ConcurrentModel();
        String view = filmController.getAll(model);
        Object actualFilms = model.getAttribute("films");

        assertThat(view).isEqualTo("films/list");
        assertThat(actualFilms).usingRecursiveComparison().isEqualTo(expectedFilms);
    }

    @Test
    public void whenRequestFilmInfoPageThenGetPageWithFilmInfo() {
        Film expectedfilm = new Film(1, "name1", "description1", 2001, "Comedy", 16, 100, 1);
        when(filmService.findById(1)).thenReturn(Optional.of(expectedfilm));

        ConcurrentModel model = new ConcurrentModel();
        String view = filmController.getById(model, expectedfilm.getId());
        Object actualFilm = model.getAttribute("film");

        assertThat(view).isEqualTo("films/one");
        assertThat(actualFilm).usingRecursiveComparison().isEqualTo(expectedfilm);

    }

    @Test
    public void whenFilmNotFoundThenReturnErrorPage() {
        RuntimeException expectedException = new RuntimeException("Film not found");
        when(filmService.findById(anyInt())).thenReturn(Optional.empty());

        ConcurrentModel model = new ConcurrentModel();
        String view = filmController.getById(model, 1);
        Object actualExceptionMessage = model.getAttribute("message");

        AssertionsForClassTypes.assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
        AssertionsForClassTypes.assertThat(view).isEqualTo("errors/404");
    }
}