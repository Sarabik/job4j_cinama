package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.DtoFilmSession;
import ru.job4j.cinema.model.*;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketControllerTest {

    private TicketController ticketController;

    private FilmSessionService filmSessionService;

    private TicketService ticketService;

    private HttpServletRequest request;

    @BeforeEach
    public void initServices() {
        filmSessionService = mock(FilmSessionService.class);
        ticketService = mock(TicketService.class);
        ticketController = new TicketController(filmSessionService, ticketService);
        request = new MockHttpServletRequest();
    }

    @Test
    public void whenRequestFilmSessionPageThenGetSessionPage() {
        LocalDateTime start = LocalDateTime.of(2023, 7, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 7, 1, 12, 15);
        FilmSession filmSession1 = new FilmSession(1, 1, 1, start, end, 6);
        Film film1 = new Film(filmSession1.getFilmId(), "name1", "description1", 2001, "Comedy", 16, 100, 1);
        Hall hall1 = new Hall(filmSession1.getHallId(), "hall1", 3, 5, "description1");
        DtoFilmSession exceptedDtoFilmSession = new DtoFilmSession(film1, hall1, filmSession1);
        when(filmSessionService.getById(anyInt())).thenReturn(Optional.of(exceptedDtoFilmSession));

        User exceptedUser = new User(1, "mail@mail.com", "name", "password");
        HttpSession session = request.getSession();
        session.setAttribute("user", exceptedUser);

        ConcurrentModel model = new ConcurrentModel();
        String view = ticketController.getTicketByFilmSessionId(model, 1, request);
        Object actualDtoFilmSession = model.getAttribute("filmSession");
        Object actualUser = request.getSession().getAttribute("user");
        Object actualRowCollection = model.getAttribute("rowCount");
        Object actualPlaceCollection = model.getAttribute("placeCount");

        assertThat(view).isEqualTo("tickets/sessionId");
        assertThat(actualDtoFilmSession).usingRecursiveComparison().isEqualTo(exceptedDtoFilmSession);
        assertThat(actualUser).isEqualTo(exceptedUser);
        assertThat(actualRowCollection).isEqualTo(List.of(1, 2, 3));
        assertThat(actualPlaceCollection).isEqualTo(List.of(1, 2, 3, 4, 5));
    }

    @Test
    public void whenSuccessfullySaveNewTicket() {
        Ticket expectedTicket = new Ticket(1, 1, 3, 3, 1);
        ArgumentCaptor<Ticket> ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        when(ticketService.save(ticketArgumentCaptor.capture())).thenReturn(Optional.of(expectedTicket));

        ConcurrentModel model = new ConcurrentModel();
        String view = ticketController.saveTicket(model, expectedTicket.getSessionId(), expectedTicket);
        Object actualTicket = ticketArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/tickets/success/" + expectedTicket.getId());
        assertThat(actualTicket).isEqualTo(expectedTicket);
    }

    @Test
    public void whenCanNotSaveNewTicket() {
        RuntimeException expectedRuntimeException = new RuntimeException("Ticket the to chosen place is already sold. Choose other place, please!");
        Ticket expectedTicket = new Ticket(1, 1, 3, 3, 1);
        ArgumentCaptor<Ticket> ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        when(ticketService.save(ticketArgumentCaptor.capture())).thenReturn(Optional.empty());

        ConcurrentModel model = new ConcurrentModel();
        String view = ticketController.saveTicket(model, expectedTicket.getSessionId(), expectedTicket);
        Object actualExceptionMessage = model.getAttribute("message");
        Object actualSessionId = model.getAttribute("currentSessionId");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualSessionId).isEqualTo(expectedTicket.getSessionId());
        assertThat(actualExceptionMessage).isEqualTo(expectedRuntimeException.getMessage());
    }

    @Test
    public void whenSuccessfullyBuyTicketThenGetTicketInfoPage() {
        FilmSession expectedFilmSession = new FilmSession();
        expectedFilmSession.setId(1);
        DtoFilmSession expecteDtoFilmSession = new DtoFilmSession(new Film(), new Hall(), expectedFilmSession);
        Ticket expectedTicket = new Ticket(1, 1, 3, 3, 1);
        when(ticketService.findById(anyInt())).thenReturn(Optional.of(expectedTicket));
        when(filmSessionService.getById(anyInt())).thenReturn(Optional.of(expecteDtoFilmSession));

        User exceptedUser = new User(1, "mail@mail.com", "name", "password");
        HttpSession session = request.getSession();
        session.setAttribute("user", exceptedUser);

        ConcurrentModel model = new ConcurrentModel();
        String view = ticketController.ticketBuySuccess(model, expectedTicket.getId(), request);
        Object actualTicket = model.getAttribute("ticket");
        Object actualFilmSession = model.getAttribute("filmSession");
        Object actualUser = request.getSession().getAttribute("user");

        assertThat(view).isEqualTo("tickets/success/ticketId");
        assertThat(actualUser).isEqualTo(exceptedUser);
        assertThat(actualTicket).isEqualTo(expectedTicket);
        assertThat(actualFilmSession).isEqualTo(expecteDtoFilmSession);
    }
}