package io.tanibilet.server.events;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.events.dto.CreateEventDto;
import io.tanibilet.server.tickets.entities.EventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;

    public Optional<EventEntity> createEvent(CreateEventDto createEventDto, UserPrincipal user) {
        if (createEventDto.eventStartTimeMillis().isAfter(createEventDto.eventEndTimeMillis())) {
            return Optional.empty();
        }

        EventEntity entity = mapToEntity(createEventDto, user.userId());
        EventEntity saved = eventRepository.save(entity);
        return Optional.of(saved);
    }

    public Page<EventEntity> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<EventEntity> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Optional<EventEntity> updateEvent(Long id, CreateEventDto updateDto, UserPrincipal principal) {
        Optional<EventEntity> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) return Optional.empty();
        if (updateDto.eventEndTimeMillis().isBefore(updateDto.eventStartTimeMillis())) return Optional.empty();

        EventEntity existing = optionalEvent.get();
        if (!existing.getOwnerUserId().equals(principal.userId())) return Optional.empty();

        existing.setName(updateDto.name());
        existing.setEventStartTimeMillis(updateDto.eventStartTimeMillis());
        existing.setEventEndTimeMillis(updateDto.eventEndTimeMillis());
        existing.setLocation(updateDto.location());

        return Optional.of(eventRepository.save(existing));
    }

    public boolean deleteEvent(Long id, UserPrincipal principal) {
        if (!eventRepository.existsById(id)) {
            return false;
        }
        eventRepository.deleteById(id);
        return true;
    }

    private EventEntity mapToEntity(CreateEventDto dto, String userId) {
        return new EventEntity(
                null,
                dto.name(),
                dto.eventStartTimeMillis(),
                dto.eventEndTimeMillis(),
                dto.location(),
                dto.ticketPrice(),
                dto.maxTicketCount(),
                userId
        );
    }
}
