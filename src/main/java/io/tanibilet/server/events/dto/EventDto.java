package io.tanibilet.server.events.dto;

import io.tanibilet.server.tickets.entities.EventEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.ZonedDateTime;


@Builder
public record EventDto(
    @NotNull
    Long id,
    @NotBlank
    String name,
    @NotNull
    ZonedDateTime eventStartTimeMillis,
    @NotNull
    ZonedDateTime eventEndTimeMillis,
    @NotBlank
    String location
){
    public static EventDto fromEventEntity(final EventEntity entity) {
        return new EventDto(
                entity.getId(),
                entity.getName(),
                entity.getEventStartTimeMillis(),
                entity.getEventEndTimeMillis(),
                entity.getLocation()
        );
    }
}
