package io.tanibilet.server.events;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.events.dto.CreateEventDto;
import io.tanibilet.server.events.dto.EventDto;
import io.tanibilet.server.events.entities.EventType;
import io.tanibilet.server.shared.PageableDto;
import io.tanibilet.server.events.entities.EventEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class EventServiceTest {
    EventRepository eventRepository = mock(EventRepository.class);
    private EventService eventService = new EventService(eventRepository);

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
    void testCreateEventSuccessful()
    {
        //Arrange
        Mockito.when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);

        //Act
        Optional<EventEntity> event = eventService.createEvent(createEventDto, user);

        //Assert
        assertTrue(event.isPresent());
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void testCreateEventWrongEventTime()
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
        Optional<EventEntity> event = eventService.createEvent(createEventDtoWithWrongDate, user);

        //Assert
        assertTrue(event.isEmpty());
        verify(eventRepository, times(0)).save(any(EventEntity.class));
    }

    @Test
    void testGetAllEvents()
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
        Pageable pageable = new PageableDto(0, 100).toPageable();
        Page<EventEntity> expectedPage = new PageImpl<>(events, pageable, events.size());

        when(eventRepository.findAll(pageable)).thenReturn(expectedPage);

        //Act
        Page<EventEntity> result = eventService.getAllEvents(pageable);

        //Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(events, result.getContent());

        verify(eventRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetAllEventsForUser()
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
        Pageable pageable = new PageableDto(0, 100).toPageable();
        Page<EventEntity> expectedPage = new PageImpl<>(List.of(eventEntity), pageable, 1);

        when(eventRepository.findAllByOwnerUserId(user.userId(), pageable)).thenReturn(expectedPage);

        //Act
        Page<EventEntity> result = eventService.getAllEventsForUser(user.userId(), pageable);

        //Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(List.of(eventEntity), result.getContent());

        verify(eventRepository, times(1)).findAllByOwnerUserId(user.userId(), pageable);
    }

    @Test
    void testGetEventById()
    {
        //Arrange
        when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));

        //Act
        Optional<EventEntity> event = eventService.getEventById(1L);

        //Assert
        assertTrue(event.isPresent());
        assertEquals(eventEntity, event.get());

        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateEventSuccessful()
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
        when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));
        when(eventRepository.save(any(EventEntity.class))).thenReturn(updatedEventEntity);

        //Act
        Optional<EventEntity> event = eventService.updateEvent(1L, updateDto, user);

        //Assert
        assertTrue(event.isPresent());
        assertEquals(updatedEventEntity, event.get());

        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void testUpdateEventNonExistentEvent()
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
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        //Act
        Optional<EventEntity> event = eventService.updateEvent(1L, updateDto, user);

        //Assert
        assertTrue(event.isEmpty());

        verify(eventRepository, times(0)).save(any(EventEntity.class));

    }

    @Test
    void testUpdateEventWrongEventDate()
    {
        //Arrange
        CreateEventDto updateDto = new CreateEventDto(
                "Updated test",
                ZonedDateTime.now().plusDays(3),
                ZonedDateTime.now().plusDays(1),
                "Kraków",
                69.0,
                150,
                "Updated test description",
                false,
                EventType.CONCERT
        );
        when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));

        //Act
        Optional<EventEntity> event = eventService.updateEvent(1L, updateDto, user);

        //Assert
        assertTrue(event.isEmpty());

        verify(eventRepository, times(0)).save(any(EventEntity.class));
    }

    @Test
    void testUpdateEventWrongUser()
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
        when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_event_creator"));

        UserPrincipal user2 = new UserPrincipal(
                "2",
                "test2@mail.com",
                "TestUser2",
                true,
                "Gordon",
                "Freeman",
                authorities
        );

        //Act
        Optional<EventEntity> event = eventService.updateEvent(1L, updateDto, user2);

        //Assert
        assertTrue(event.isEmpty());

        verify(eventRepository, times(0)).save(any(EventEntity.class));
    }

    @Test
    void testDeleteEventSuccessful()
    {
        //Arrange

        //Act

        //Assert
    }

    @Test
    void testDeleteEventNonExistentEvent()
    {
        //Arrange

        //Act

        //Assert
    }

    @Test
    void testDeleteEventWrongUser()
    {
        //Arrange

        //Act

        //Assert
    }
}
