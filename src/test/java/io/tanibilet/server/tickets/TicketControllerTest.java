package io.tanibilet.server.tickets;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.events.entities.EventEntity;
import io.tanibilet.server.events.entities.EventType;
import io.tanibilet.server.tickets.dto.GetTicketDto;
import io.tanibilet.server.tickets.dto.OrderTicketDto;
import io.tanibilet.server.tickets.dto.OrderTicketUnauthenticatedDto;
import io.tanibilet.server.tickets.entities.TicketEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TicketControllerTest {

    private TicketService ticketService = mock(TicketService.class);

    private TicketController ticketController = new TicketController(ticketService);

    private UserPrincipal user;
    private TicketEntity ticketEntity;

    @BeforeEach
    void SetUp() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_event_creator"));

        user = new UserPrincipal(
                "1",
                "test@mail.com",
                "TestUser",
                true,
                "John",
                "Doe",
                authorities
        );
        UUID uuid = new UUID(1000L, 500L);

        EventEntity eventEntity = new EventEntity(
                null,
                "Test",
                ZonedDateTime.now().plusDays(1),
                ZonedDateTime.now().plusDays(2),
                "Kraków",
                50.0,
                200,
                user.userId(),
                "Test description",
                false,
                EventType.CONCERT,
                new HashSet<>()
        );

        ticketEntity = new TicketEntity();
        ticketEntity.setId(1L);
        ticketEntity.setQrCodeId(uuid);
        ticketEntity.setBoughtPrice(50.0);
        ticketEntity.setEvent(eventEntity);
        ticketEntity.setUserId(user.userId());
    }

    @Test
    void testGetTicketsForUser()
    {
        //Arrange
        when(ticketService.getTicketsForUser(user.userId())).thenReturn(List.of(ticketEntity));

        //Act
        List<GetTicketDto> tickets = ticketController.getTicketsForUser(user);

        //Assert
        assertEquals(1, tickets.size());
        assertEquals(tickets, List.of(GetTicketDto.fromTicketEntity(ticketEntity)));
    }

    @Test
    void testGetTicketForUserStatusOk()
    {
        //Arrange
        when(ticketService.getTicketForUser(user.userId(), 1L)).thenReturn(Optional.of(ticketEntity));
        GetTicketDto expectedTicketDto = GetTicketDto.fromTicketEntity(ticketEntity);

        //Act
        ResponseEntity<GetTicketDto> ticket = ticketController.getTicketForUser(user, 1L);

        //Assert
        assertEquals(HttpStatus.OK, ticket.getStatusCode());
        assertNotNull(ticket.getBody());
        assertEquals(expectedTicketDto, ticket.getBody());
    }

    @Test
    void testGetTicketForUserStatusNotFound()
    {
        //Arrange
        when(ticketService.getTicketForUser(user.userId(), 1L)).thenReturn(Optional.empty());

        //Act
        ResponseEntity<GetTicketDto> ticket = ticketController.getTicketForUser(user, 1L);

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, ticket.getStatusCode());
    }

    @Test
    void testOrderTicketStatusOk()
    {
        //Arrange
        OrderTicketDto orderTicketDto = new OrderTicketDto(1L, 1);
        when(ticketService.orderTicketForEvent(orderTicketDto, user)).thenReturn(List.of(ticketEntity));
        List<GetTicketDto> expectedTicketDto = List.of(GetTicketDto.fromTicketEntity(ticketEntity));

        //Act
        ResponseEntity<List<GetTicketDto>> ticket = ticketController.orderTicket(user, orderTicketDto);

        //Assert
        assertEquals(HttpStatus.OK, ticket.getStatusCode());
        assertNotNull(ticket.getBody());
        assertEquals(expectedTicketDto, ticket.getBody());
    }

    @Test
    void testOrderTicketStatusNotFound()
    {
        //Arrange
        OrderTicketDto orderTicketDto = new OrderTicketDto(1L, 1);
        when(ticketService.orderTicketForEvent(orderTicketDto, user)).thenReturn(List.of());

        //Act
        ResponseEntity<List<GetTicketDto>> ticket = ticketController.orderTicket(user, orderTicketDto);

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, ticket.getStatusCode());
    }

    @Test
    void testOrderUnauthenticatedTicketStatusOk()
    {
        //Arrange
        ticketEntity.setUserId(null);
        OrderTicketUnauthenticatedDto orderTicketUnauthenticatedDto = new OrderTicketUnauthenticatedDto( 1L, "test@email.com", 1);
        when(ticketService.orderTicketForEvent(orderTicketUnauthenticatedDto)).thenReturn(List.of(ticketEntity));
        List<GetTicketDto> expectedTicketDto = List.of(GetTicketDto.fromTicketEntity(ticketEntity));

        //Act
        ResponseEntity<List<GetTicketDto>> ticket = ticketController.orderTicket(orderTicketUnauthenticatedDto);

        //Assert
        assertEquals(HttpStatus.OK, ticket.getStatusCode());
        assertNotNull(ticket.getBody());
        assertEquals(expectedTicketDto, ticket.getBody());
    }

    @Test
    void testOrderUnauthenticatedTicketStatusNotFound()
    {
        //Arrange
        OrderTicketUnauthenticatedDto orderTicketUnauthenticatedDto = new OrderTicketUnauthenticatedDto(1L, "test@email.com", 1);
        when(ticketService.orderTicketForEvent(orderTicketUnauthenticatedDto)).thenReturn(List.of());

        //Act
        ResponseEntity<List<GetTicketDto>> ticket = ticketController.orderTicket(orderTicketUnauthenticatedDto);

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, ticket.getStatusCode());
    }



}
