package io.tanibilet.server.tickets;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.shared.PageableDto;
import io.tanibilet.server.tickets.dto.GetTicketDto;
import io.tanibilet.server.tickets.dto.OrderTicketDto;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/")
    Page<GetTicketDto> getTicketsForUser(PageableDto pageable, @AuthenticationPrincipal UserPrincipal user) {
        return ticketService.getTicketsForUser(user.userId(), pageable.toPageable()).map(GetTicketDto::fromTicketEntity);
    }

    @GetMapping("/{id}")
    ResponseEntity<GetTicketDto> getTicketForUser(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id) {
        return ticketService.getTicketForUser(user.userId(), id)
                .map(GetTicketDto::fromTicketEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PermitAll
    @PostMapping("/order")
    ResponseEntity<GetTicketDto> orderTicket(@RequestBody OrderTicketDto orderTicketDto) {
        return ticketService.orderTicketForEvent(orderTicketDto, getAuthenticatedUser())
                .map(GetTicketDto::fromTicketEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    Optional<UserPrincipal> getAuthenticatedUser() {
        val context = SecurityContextHolder.getContext().getAuthentication();
        if(context != null && context.isAuthenticated() && context.getPrincipal() instanceof UserPrincipal principal) {
            return Optional.of(principal);
        }
        return Optional.empty();
    }
}
