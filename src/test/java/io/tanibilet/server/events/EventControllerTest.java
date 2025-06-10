package io.tanibilet.server.events;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.events.dto.CreateEventDto;
import io.tanibilet.server.events.dto.EventDto;
import io.tanibilet.server.events.entities.EventEntity;
import io.tanibilet.server.events.entities.EventType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

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
                200,
                "Test description",
                false,
                EventType.CONCERT
        );

        eventEntity = new EventEntity(
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
        //Arrange
        Mockito.when(eventService.getAllEvents()).thenReturn(List.of(eventEntity));

        EventDto expectedDto = EventDto.fromEventEntity(eventEntity);


        //Act
        List<EventDto> events = eventController.getEvents();

        //Assert
        assertEquals(1, events.size());
        assertEquals(expectedDto, events.get(0));

    }

    @Test
    void testGetEventByIdStatusOK() {
        //Arrange
        Mockito.when(eventService.getEventById(1L)).thenReturn(Optional.of(eventEntity));
        EventDto expectedDto = EventDto.fromEventEntity(eventEntity);

        //Act
        ResponseEntity<EventDto> event = eventController.getEventById(1L);

        //Assert
        assertEquals(HttpStatus.OK, event.getStatusCode());
        assertNotNull(event.getBody());
        assertEquals(expectedDto, event.getBody());
    }

    @Test
    void testGetEventByIdStatusNotFound() {
        //Arrange
        Mockito.when(eventService.getEventById(1L)).thenReturn(Optional.empty());

        //Act
        ResponseEntity<EventDto> event = eventController.getEventById(1L);

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, event.getStatusCode());
    }

    @Test
    void testUpdateEventStatusOK()
    {
        //Arrange
        CreateEventDto updateDto = new CreateEventDto(
                "Updated test",
                ZonedDateTime.now().plusDays(1),
                ZonedDateTime.now().plusDays(3),
                "Kraków",
                69.0,
                150,
                "Updated test description",
                false,
                EventType.CONCERT
        );

        EventEntity updatedEventEntity = new EventEntity(
                null,
                "Updated test",
                ZonedDateTime.now().plusDays(1),
                ZonedDateTime.now().plusDays(3),
                "Kraków",
                69.0,
                150,
                user.userId(),
                "Updated test description",
                false,
                EventType.CONCERT,
                new HashSet<>()
        );

        EventDto expectedDto = EventDto.fromEventEntity(updatedEventEntity);

        Mockito.when(eventService.updateEvent(1L, updateDto, user)).thenReturn(Optional.of(updatedEventEntity));

        //Act
        ResponseEntity<EventDto> updatedEvent = eventController.updateEvent(user, 1L, updateDto);

        //Assert
        assertEquals(HttpStatus.OK, updatedEvent.getStatusCode());
        assertNotNull(updatedEvent.getBody());
        assertEquals(expectedDto, updatedEvent.getBody());

    }

    @Test
    void testUpdateEventStatusBadRequest()
    {
        //Arrange
        CreateEventDto updateDto = new CreateEventDto(
                "Updated test",
                ZonedDateTime.now().plusDays(1),
                ZonedDateTime.now().plusDays(3),
                "Kraków",
                69.0,
                150,
                "Updated test description",
                false,
                EventType.CONCERT
        );



        Mockito.when(eventService.updateEvent(1L, updateDto, user)).thenReturn(Optional.empty());

        //Act
        ResponseEntity<EventDto> updatedEvent = eventController.updateEvent(user, 1L, updateDto);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, updatedEvent.getStatusCode());

    }

    @Test
    void testDeleteEventStatusOK()
    {
        //Arrange
        Mockito.when(eventService.deleteEvent(1L, user)).thenReturn(true);

        //Act
        ResponseEntity<?> ent = eventController.deleteEvent(user, 1L);

        //Assert
        assertEquals(HttpStatus.OK, ent.getStatusCode());
        assertNull(ent.getBody());
    }

    @Test
    void testDeleteEventStatusNotFound()
    {
        //Arrange
        Mockito.when(eventService.deleteEvent(1L, user)).thenReturn(false);

        //Act
        ResponseEntity<?> ent = eventController.deleteEvent(user, 1L);

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, ent.getStatusCode());
    }
}
