package io.tanibilet.server.events;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.events.dto.CreateEventDto;
import io.tanibilet.server.events.dto.EventDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PreAuthorize("hasRole('event_creator')")
    @PostMapping("/crud")
    public ResponseEntity<EventDto> createEvent(@AuthenticationPrincipal UserPrincipal user, @RequestBody @Valid CreateEventDto createEventDto) {
        return eventService.createEvent(createEventDto, user)
                .map(EventDto::fromEventEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/crud")
    public List<EventDto> getEvents() {
        return eventService.getAllEvents().stream().map(EventDto::fromEventEntity).toList();
    }

    @GetMapping("/createdByUserOnly")
    public List<EventDto> getEventForUser(@AuthenticationPrincipal UserPrincipal user) {
        return eventService.getAllEventsForUser(user.userId()).stream().map(EventDto::fromEventEntity).toList();
    }

    @GetMapping("/crud/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(EventDto::fromEventEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('event_creator')")
    @PutMapping("/crud/{id}")
    public ResponseEntity<EventDto> updateEvent(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id,
            @RequestBody @Valid CreateEventDto updateDto) {
        return eventService.updateEvent(id, updateDto, user)
                .map(EventDto::fromEventEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PreAuthorize("hasRole('event_creator')")
    @DeleteMapping("/crud/{id}")
    public ResponseEntity<?> deleteEvent(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id) {
        return eventService.deleteEvent(id, user) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
