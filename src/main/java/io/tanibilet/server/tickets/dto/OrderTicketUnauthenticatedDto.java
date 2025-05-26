package io.tanibilet.server.tickets.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderTicketUnauthenticatedDto(
        @NotNull
        @Positive
        Integer seat,
        @NotNull
        Long eventId,
        @Email
        @NotNull
        String email
) {
    public OrderTicketDto toOrderTicketDto() {
        return new OrderTicketDto(
                seat, eventId
        );
    }
}
