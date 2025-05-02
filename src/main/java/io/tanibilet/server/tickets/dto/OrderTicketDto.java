package io.tanibilet.server.tickets.dto;

public record OrderTicketDto(
        String seat,
        Long eventId
){}
