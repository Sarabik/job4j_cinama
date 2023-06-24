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
        return "tickets/buy";
    }

    @PostMapping("/{sessionId}")
    public String saveTicket(Model model, @PathVariable int sessionId, HttpServletRequest request) {
        System.out.println("start");
//        Ticket ticket = new Ticket();
//        ticket.setId(0);
//        ticket.setSessionId(sessionId);
//        ticket.setRowNumber(Integer.parseInt(request.getParameter("rowNumber")));
//        ticket.setPlaceNumber(Integer.parseInt(request.getParameter("placeNumber")));
//        ticket.setUserId(1);
//        System.out.println(ticket.getSessionId() + ticket.getRowNumber() + ticket.getPlaceNumber());
//        ticketService.save(ticket);
        return "redirect:/index";
    }

    @GetMapping("/success")
    public String ticketBuySuccess() {
        return "";
    }

}
