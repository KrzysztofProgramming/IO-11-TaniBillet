package io.tanibilet.server.events.dto;

import io.tanibilet.server.events.entities.EventEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
    String location,
    @NotNull
    String description,
    @NotNull
    Boolean isBuyingTicketsTurnedOff,
    @NotBlank
    String ownerId,
    @NotNull
    @PositiveOrZero
    Integer maxTicketsCount,
    @NotNull
    @PositiveOrZero
    Integer ticketsSoldCount
){
    public static EventDto fromEventEntity(final EventEntity entity) {
        return new EventDto(
                entity.getId(),
                entity.getName(),
                entity.getEventStartTimeMillis(),
                entity.getEventEndTimeMillis(),
                entity.getLocation(),
                entity.getDescription(),
                entity.getIsBuyingTicketsTurnedOff(),
                entity.getOwnerUserId(),
                entity.getMaxTicketCount(),
                entity.getSoldTicketCount()
        );
    }
}
