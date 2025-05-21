package io.tanibilet.server.events;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.events.dto.CreateEventDto;
import io.tanibilet.server.events.dto.EventDto;
import io.tanibilet.server.shared.PageableDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PreAuthorize("hasRole('event_creator')")
    @PostMapping
    public ResponseEntity<EventDto> createEvent(@AuthenticationPrincipal UserPrincipal user, @RequestBody @Valid CreateEventDto createEventDto) {
        return eventService.createEvent(createEventDto, user)
                .map(EventDto::fromEventEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping
    public Page<EventDto> getEvents(PageableDto pageable) {
        return eventService.getAllEvents(pageable.toPageable()).map(EventDto::fromEventEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(EventDto::fromEventEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('event_creator')")
    @PutMapping("/{id}")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id) {
        return eventService.deleteEvent(id, user) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
