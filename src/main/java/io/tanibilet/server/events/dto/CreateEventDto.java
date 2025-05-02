package io.tanibilet.server.events.dto;

import jakarta.validation.constraints.*;
import lombok.*;

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
    Double ticketPrice,
    @NotNull
    @Min(1)
    Integer maxTicketCount
){}
