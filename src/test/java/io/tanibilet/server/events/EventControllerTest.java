package io.tanibilet.server.events;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.events.dto.CreateEventDto;
import io.tanibilet.server.events.dto.EventDto;
import io.tanibilet.server.shared.PageableDto;
import io.tanibilet.server.tickets.entities.EventEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.script.ScriptEngine;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    private EventService eventService = mock(EventService.class);

    private EventController eventController = new EventController(eventService);

    private static UserPrincipal user;
    private static CreateEventDto createEventDto;
    private static EventEntity eventEntity;

    @BeforeAll
    static void SetUp()
    {
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

        createEventDto = new CreateEventDto(
                "Test",
                ZonedDateTime.now().plusDays(1),
                ZonedDateTime.now().plusDays(2),
                "Kraków",
                50.0,
                200
        );

        eventEntity = new EventEntity(
                null,
                "Test",
                ZonedDateTime.now().plusDays(1),
                ZonedDateTime.now().plusDays(2),
                "Kraków",
                50.0,
                200,
                user.userId()
        );
    }
    

    @Test
    void testCreateEventStatusOK() {
        //Arrange
        Mockito.when(eventService.createEvent(createEventDto, user)).thenReturn(Optional.of(eventEntity));
        //Act
        ResponseEntity<EventDto> event = eventController.createEvent(user, createEventDto);
        //Assert
        assertEquals(HttpStatus.OK, event.getStatusCode());
    }

    @Test
    void testCreateEventStatusBadRequest() {
        //Arrange
        Mockito.when(eventService.createEvent(createEventDto, user)).thenReturn(Optional.empty());
        //Act
        ResponseEntity<EventDto> event = eventController.createEvent(user, createEventDto);
        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, event.getStatusCode());
    }

    @Test
    void testGetEvents() {

    }

    @Test
    void testGetEventById() {

    }
}
