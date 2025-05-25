package io.tanibilet.server.tickets.dto;

import io.tanibilet.server.tickets.entities.TicketEntity;

import java.util.Optional;
import java.util.UUID;

public record GetTicketDto(
        Long id,
        UUID qrCodeId,
        Double boughtPrice,
        Integer seat,
        Long eventId,
        Optional<String> userId
) {
    public static GetTicketDto fromTicketEntity(final TicketEntity ticket) {
        return new GetTicketDto(
                ticket.getId(),
                ticket.getQrCodeId(),
                ticket.getBoughtPrice(),
                ticket.getSeat(),
                ticket.getEvent().getId(),
                Optional.ofNullable(ticket.getUserId())
        );
    }
}
