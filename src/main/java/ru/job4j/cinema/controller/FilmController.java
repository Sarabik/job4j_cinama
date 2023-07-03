package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.service.FilmService;

import java.util.Optional;

@Controller
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("films", filmService.findAll());
        return "films/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        Optional<Film> filmOptional = filmService.findById(id);
        if (filmOptional.isEmpty()) {
            model.addAttribute("message", "Film not found");
            return "errors/409";
        }
        model.addAttribute("film", filmOptional.get());
        return "films/one";
    }
}
