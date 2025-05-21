package io.tanibilet.server.tickets;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.events.EventRepository;
import io.tanibilet.server.mailing.MailService;
import io.tanibilet.server.tickets.dto.OrderTicketDto;
import io.tanibilet.server.tickets.entities.EventEntity;
import io.tanibilet.server.events.entities.TicketEntity;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final MailService mailService;

    public Optional<TicketEntity> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public Page<TicketEntity> getTicketsForUser(String userId, Pageable pageable) {
        return ticketRepository.findByUserId(userId, pageable);
    }

    public Optional<TicketEntity> getTicketForUser(String userId, Long ticketId) {
        return ticketRepository.findOneByIdAndUserId(ticketId, userId);
    }

    public Optional<TicketEntity> orderTicketForEvent(OrderTicketDto orderTicketDto, Optional<UserPrincipal> userOpt) {
        val eventOpt = eventRepository.findById(orderTicketDto.eventId());
        return eventOpt.flatMap(event -> {
            long maxTicketCount = event.getMaxTicketCount();
            long ticketCount = ticketRepository.countByEventId(event.getId());
            if (ticketCount >= maxTicketCount) return Optional.empty();

            if(ticketRepository.existsByEventIdAndSeat(event.getId(), orderTicketDto.seat())){
                // seat is already occupied
                return Optional.empty();
            }

            val createdTicket = ticketRepository.save(createTicketEntity(orderTicketDto, event, userOpt.map(UserPrincipal::userId)));
            userOpt.ifPresent(user -> mailService.sendTicketViaEmail(createdTicket, user));

            return Optional.of(createdTicket);
        });
    }

    private TicketEntity createTicketEntity(OrderTicketDto orderTicketDto, EventEntity eventEntity, Optional<String> userId) {
        return new TicketEntity(
                null,
                UUID.randomUUID(),
                eventEntity.getTicketPrice(),
                orderTicketDto.seat(),
                eventEntity,
                userId.orElse(null)
        );
    }
}
