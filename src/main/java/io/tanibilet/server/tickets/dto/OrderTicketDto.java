package io.tanibilet.server.tickets.dto;

import jakarta.validation.constraints.NotNull;

public record OrderTicketDto(
        @NotNull
        Long eventId
){}
