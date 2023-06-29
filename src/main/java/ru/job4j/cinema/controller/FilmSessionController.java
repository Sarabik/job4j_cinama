package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.DtoFilmSession;
import ru.job4j.cinema.service.FilmSessionService;

import java.util.Collection;

@Controller
@RequestMapping("/sessions")
public class FilmSessionController {

    private final FilmSessionService filmSessionService;

    public FilmSessionController(FilmSessionService filmSessionService) {
        this.filmSessionService = filmSessionService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("sessions", filmSessionService.findAll());
        return "sessions/list";
    }

    @GetMapping("/{id}")
    public String getFilmSessions(Model model, @PathVariable int id) {
        Collection<DtoFilmSession> collection = filmSessionService.getAllByFilmId(id);
        model.addAttribute("sessions", collection);
        return "sessions/film";
    }
}
