package io.tanibilet.server.tickets.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderTicketUnauthenticatedDto(
        @NotNull
        Long eventId,
        @Email
        @NotNull
        String email,
        @Positive
        @NotNull
        Integer ticketsCount
) {
    public OrderTicketDto toOrderTicketDto() {
        return new OrderTicketDto(
                eventId,
                ticketsCount
        );
    }
}
