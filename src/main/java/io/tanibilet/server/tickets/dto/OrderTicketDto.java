package io.tanibilet.server.tickets.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderTicketDto(
        @NotNull
        Long eventId,
        @Positive
        @NotNull
        Integer ticketCount
){}
