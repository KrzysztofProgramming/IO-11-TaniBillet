package io.tanibilet.server.tickets.dto;

import io.tanibilet.server.tickets.entities.TicketEntity;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public record GetTicketDto(
        @NotNull Long id,
        @NotNull UUID qrCodeId,
        @NotNull Double boughtPrice,
        @NotNull Integer seat,
        @NotNull Long eventId,
        @NotNull Optional<String> userId,
        @NotNull ZonedDateTime eventStartTime,
        @NotNull ZonedDateTime eventEndTime
) {
    public static GetTicketDto fromTicketEntity(final TicketEntity ticket) {
        return new GetTicketDto(
                ticket.getId(),
                ticket.getQrCodeId(),
                ticket.getBoughtPrice(),
                ticket.getSeat(),
                ticket.getEvent().getId(),
                Optional.ofNullable(ticket.getUserId()),
                ticket.getEvent().getEventStartTimeMillis(),
                ticket.getEvent().getEventEndTimeMillis()
        );
    }
}
