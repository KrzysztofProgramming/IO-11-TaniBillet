package io.tanibilet.server.tickets.dto;

public record OrderTicketDto(
        Integer seat,
        Long eventId
){}
