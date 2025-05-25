package io.tanibilet.server.tickets;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.shared.PageableDto;
import io.tanibilet.server.tickets.dto.GetTicketDto;
import io.tanibilet.server.tickets.dto.OrderTicketDto;
import io.tanibilet.server.tickets.dto.OrderTicketUnauthenticatedDto;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/crud")
    Page<GetTicketDto> getTicketsForUser(@Valid @ModelAttribute PageableDto pageable, @AuthenticationPrincipal UserPrincipal user) {
        return ticketService.getTicketsForUser(user.userId(), pageable.toPageable()).map(GetTicketDto::fromTicketEntity);
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
