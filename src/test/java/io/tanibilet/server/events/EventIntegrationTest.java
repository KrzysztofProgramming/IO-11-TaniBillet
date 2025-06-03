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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class EventIntegrationTest {

    EventRepository eventRepository = mock(EventRepository.class);
    private EventService eventService = new EventService(eventRepository);
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
    void testCreateEventSuccessfully()
    {
        //Arrange
        Mockito.when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);
        //Act
        ResponseEntity<EventDto> event = eventController.createEvent(user, createEventDto);
        //Assert
        assertEquals(HttpStatus.OK, event.getStatusCode());
    }

    @Test
    void testCreateEventWithWrongEventTime()
    {
        //Arrange
        CreateEventDto createEventDtoWithWrongDate = new CreateEventDto(
                "Test",
                ZonedDateTime.now().plusDays(2),
                ZonedDateTime.now().plusDays(1),
                "Kraków",
                50.0,
                200,
                "Test description",
                false,
                EventType.CONCERT
        );

        //Act
        ResponseEntity<EventDto> event = eventController.createEvent(user, createEventDtoWithWrongDate);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, event.getStatusCode());
        verify(eventRepository, times(0)).save(any(EventEntity.class));
    }

    @Test
    void testGetEvents()
    {
        //Arrange
        EventEntity eventEntity2 = new EventEntity(
                null,
                "Test2",
                ZonedDateTime.now().plusDays(1),
                ZonedDateTime.now().plusDays(3),
                "Kraków",
                25.0,
                100,
                user.userId(),
                "Test2 description",
                false,
                EventType.CONFERENCE,
                new HashSet<>()
        );

        List<EventEntity> events = List.of(
                eventEntity,
                eventEntity2
        );

        List<EventDto> expectedEvents = List.of(
                EventDto.fromEventEntity(eventEntity),
                EventDto.fromEventEntity(eventEntity2)
        );

        when(eventRepository.findAll()).thenReturn(events);

        //Act
        List<EventDto> foundEvents = eventController.getEvents();

        //Assert
        assertNotNull(foundEvents);
        assertEquals(2, foundEvents.size());
        assertEquals(expectedEvents, foundEvents);

        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void testGetEventForUser()
    {
        //Arrange
        EventEntity eventEntity2 = new EventEntity(
                null,
                "Test2",
                ZonedDateTime.now().plusDays(1),
                ZonedDateTime.now().plusDays(3),
                "Kraków",
                25.0,
                100,
                user.userId(),
                "Test2 description",
                false,
                EventType.CONFERENCE,
                new HashSet<>()
        );
        List<EventEntity> events = List.of(
                eventEntity,
                eventEntity2
        );

        when(eventRepository.findAllByOwnerUserId(user.userId())).thenReturn(List.of(eventEntity));

        //Act
        List<EventDto> result = eventController.getEventForUser(user);

        //Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(List.of(EventDto.fromEventEntity(eventEntity)), result);

        verify(eventRepository, times(1)).findAllByOwnerUserId(user.userId());
    }

    @Test
    void testGetEventByIdSuccessfully()
    {
        //Arrange
        when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));
        EventDto expectedDto = EventDto.fromEventEntity(eventEntity);

        //Act
        ResponseEntity<EventDto> event = eventController.getEventById(1L);

        //Assert
        assertEquals(HttpStatus.OK, event.getStatusCode());
        assertNotNull(event.getBody());
        assertEquals(expectedDto, event.getBody());

        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEventByIdStatusNotFound()
    {
        //Arrange
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        //Act
        ResponseEntity<EventDto> event = eventController.getEventById(1L);

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, event.getStatusCode());

        verify(eventRepository, times(1)).findById(1L);
    }

}
