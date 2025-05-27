package io.tanibilet.server.tickets;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.tickets.dto.GetTicketDto;
import io.tanibilet.server.tickets.dto.OrderTicketDto;
import io.tanibilet.server.tickets.dto.OrderTicketUnauthenticatedDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/crud")
    List<GetTicketDto> getTicketsForUser(@AuthenticationPrincipal UserPrincipal user) {
        return ticketService.getTicketsForUser(user.userId()).stream().map(GetTicketDto::fromTicketEntity).toList();
    }

    @GetMapping("/crud/{id}")
    ResponseEntity<GetTicketDto> getTicketForUser(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id) {
        return ticketService.getTicketForUser(user.userId(), id)
                .map(GetTicketDto::fromTicketEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/order")
    ResponseEntity<GetTicketDto> orderTicket(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody OrderTicketDto orderTicketDto) {
        return ticketService.orderTicketForEvent(orderTicketDto, user)
                .map(GetTicketDto::fromTicketEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/orderUnauthenticated")
    ResponseEntity<GetTicketDto> orderTicket(@Valid @RequestBody OrderTicketUnauthenticatedDto orderTicketDto) {
        return ticketService.orderTicketForEvent(orderTicketDto)
                .map(GetTicketDto::fromTicketEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
