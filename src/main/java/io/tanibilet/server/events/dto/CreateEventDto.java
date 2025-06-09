package io.tanibilet.server.events.dto;

import io.tanibilet.server.events.entities.EventType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record CreateEventDto(
        @NotBlank
        String name,
        @NotNull
        ZonedDateTime eventStartTimeMillis,
        @NotNull
        ZonedDateTime eventEndTimeMillis,
        @NotBlank
        String location,
        @NotNull
        @PositiveOrZero
        Double ticketPrice,
        @NotNull
        @Min(1)
        Integer maxTicketCount,
        @NotBlank
        String description,
        @NotNull
        Boolean isBuyingTicketsTurnedOff,
        @NotNull
        EventType eventType
) {
}
