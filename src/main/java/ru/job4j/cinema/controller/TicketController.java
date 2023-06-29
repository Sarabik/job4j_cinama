package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.dto.DtoFilmSession;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final FilmSessionService filmSessionService;
    private final TicketService ticketService;

    public TicketController(FilmSessionService filmSessionService, TicketService ticketService) {
        this.filmSessionService = filmSessionService;
        this.ticketService = ticketService;
    }

    @GetMapping("/{sessionId}")
    public String getTicketByFilmSessionId(Model model, @PathVariable int sessionId, HttpServletRequest request) {
        Optional<DtoFilmSession> optionalFilmSession = filmSessionService.getById(sessionId);
        if (optionalFilmSession.isEmpty()) {
            model.addAttribute("message", "Film session is not found");
            return "errors/404";
        }
        DtoFilmSession filmSession = optionalFilmSession.get();
        model.addAttribute("filmSession", filmSession);
        HttpSession session = request.getSession();
        model.addAttribute("user", session.getAttribute("user"));
        Collection<Integer> rowCollection = Stream.iterate(1, i -> i + 1)
                .limit(filmSession.getHall().getRowCount())
                .toList();
        Collection<Integer> placeCollection = Stream.iterate(1, i -> i + 1)
                .limit(filmSession.getHall().getPlaceCount())
                .toList();
        model.addAttribute("rowCount", rowCollection);
        model.addAttribute("placeCount", placeCollection);
        return "tickets/sessionId";
    }

    @PostMapping("/{sessionId}")
    public String saveTicket(Model model, @PathVariable int sessionId, @ModelAttribute Ticket ticket) {
        Optional<Ticket> optionalTicket = ticketService.save(ticket);
        if (optionalTicket.isEmpty()) {
            model.addAttribute("message", "Ticket the to chosen place is already sold. Choose other place, please!");
            model.addAttribute("currentSessionId", sessionId);
            return "errors/404";
        }
        return "redirect:/tickets/success/" + optionalTicket.get().getId();
    }

    @GetMapping("/success/{ticketId}")
    public String ticketBuySuccess(Model model, @PathVariable int ticketId, HttpServletRequest request) {
        Ticket ticket = ticketService.findById(ticketId).get();
        DtoFilmSession filmSession = filmSessionService.getById(ticket.getSessionId()).get();
        model.addAttribute("ticket", ticket);
        model.addAttribute("filmSession", filmSession);
        HttpSession session = request.getSession();
        model.addAttribute("user", session.getAttribute("user"));
        return "tickets/success/ticketId";
    }
}
