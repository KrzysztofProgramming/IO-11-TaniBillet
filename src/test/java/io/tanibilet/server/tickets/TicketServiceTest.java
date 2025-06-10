package io.tanibilet.server.tickets;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.events.EventRepository;
import io.tanibilet.server.events.entities.EventEntity;
import io.tanibilet.server.events.entities.EventType;
import io.tanibilet.server.mailing.MailService;
import io.tanibilet.server.tickets.dto.OrderTicketDto;
import io.tanibilet.server.tickets.dto.OrderTicketUnauthenticatedDto;
import io.tanibilet.server.tickets.entities.TicketEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketServiceTest {

    private TicketRepository ticketRepository;
    private EventRepository eventRepository;
    private MailService mailService;
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketRepository = mock(TicketRepository.class);
        eventRepository = mock(EventRepository.class);
        mailService = mock(MailService.class);
        ticketService = new TicketService(ticketRepository, eventRepository, mailService);
    }

    @Test
    void testGetTicketById() {
        // Arrange
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setId(1L);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketEntity));

        // Act
        Optional<TicketEntity> result = ticketService.getTicketById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(ticketEntity, result.get());
    }

    @Test
    void testOrderTicketForEventAuthenticated() {
        // Arrange
        OrderTicketDto orderTicketDto = new OrderTicketDto(1L, 1);
        EventEntity eventEntity = new EventEntity(
                1L,
                "Test Event",
                ZonedDateTime.now().plusDays(1),
                ZonedDateTime.now().plusDays(2),
                "Kraków",
                50.0,
                200,
                "creatorId",
                "Test description",
                false,
                EventType.CONCERT,
                null
        );
        when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));
        when(ticketRepository.countByEventId(1L)).thenReturn(0L);

        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setId(1L);
        ticketEntity.setEvent(eventEntity);
        when(ticketRepository.saveAll(any(Iterable.class))).thenReturn(Collections.singletonList(ticketEntity));

        UserPrincipal user = mock(UserPrincipal.class);
        when(user.userId()).thenReturn("testUserId");

        // Act
        List<TicketEntity> result = ticketService.orderTicketForEvent(orderTicketDto, user);

        // Assert
        assertEquals(1, result.size());
        assertEquals(ticketEntity, result.getFirst());
        verify(mailService, times(1)).sendTicketViaEmail(List.of(ticketEntity), user);
    }

    @Test
    void testOrderTicketForEventUnauthenticated() {
        // Arrange
        OrderTicketUnauthenticatedDto orderTicketUnauthenticatedDto = new OrderTicketUnauthenticatedDto(1L, "test@email.com", 1);
        EventEntity eventEntity = new EventEntity(
                1L,
                "Test Event",
                ZonedDateTime.now().plusDays(1),
                ZonedDateTime.now().plusDays(2),
                "Kraków",
                50.0,
                200,
                "creatorId",
                "Test description",
                false,
                EventType.CONCERT,
                null
        );
        when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));
        when(ticketRepository.countByEventId(1L)).thenReturn(0L);

        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setId(1L);
        ticketEntity.setEvent(eventEntity);
        when(ticketRepository.saveAll(any(Iterable.class))).thenReturn(Collections.singletonList(ticketEntity));

        // Act
        List<TicketEntity> result = ticketService.orderTicketForEvent(orderTicketUnauthenticatedDto);

        // Assert
        assertEquals(1, result.size());
        assertEquals(ticketEntity, result.getFirst());
        verify(mailService, times(1)).sendTicketViaEmail(List.of(ticketEntity), "test@email.com");
    }
}