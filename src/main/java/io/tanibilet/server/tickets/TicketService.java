package io.tanibilet.server.tickets;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.events.EventRepository;
import io.tanibilet.server.events.entities.EventEntity;
import io.tanibilet.server.mailing.MailService;
import io.tanibilet.server.tickets.dto.OrderTicketDto;
import io.tanibilet.server.tickets.dto.OrderTicketUnauthenticatedDto;
import io.tanibilet.server.tickets.entities.TicketEntity;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

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

    public List<TicketEntity> getTicketsForUser(String userId) {
        return ticketRepository.findByUserId(userId);
    }

    public Optional<TicketEntity> getTicketForUser(String userId, Long ticketId) {
        return ticketRepository.findOneByIdAndUserId(ticketId, userId);
    }

    public List<TicketEntity> orderTicketForEvent(OrderTicketDto orderTicketDto, UserPrincipal user) {
        return orderTicketForEvent(
                orderTicketDto,
                ticketEntities -> mailService.sendTicketViaEmail(ticketEntities, user),
                Optional.of(user.userId())
        );
    }

    public List<TicketEntity> orderTicketForEvent(OrderTicketUnauthenticatedDto orderTicketDto) {
        return orderTicketForEvent(
                orderTicketDto.toOrderTicketDto(),
                ticketEntities -> mailService.sendTicketViaEmail(ticketEntities, orderTicketDto.email()),
                Optional.empty()
        );
    }

    private List<TicketEntity> orderTicketForEvent(OrderTicketDto orderTicketDto, Consumer<List<TicketEntity>> mailSendingConsumer, Optional<String> userId) {
        val eventOpt = eventRepository.findById(orderTicketDto.eventId());
        if(eventOpt.isEmpty()) return List.of();

        val event = eventOpt.get();

        long maxTicketCount = event.getMaxTicketCount();
        long ticketCount = ticketRepository.countByEventId(event.getId());
        if (ticketCount + orderTicketDto.ticketsCount() > maxTicketCount) return List.of();
        if (event.getIsBuyingTicketsTurnedOff()) return List.of();

        val tickets = ticketRepository.saveAll(
                Stream.generate(() -> createTicketEntity(event, userId)).limit(orderTicketDto.ticketsCount()).toList()
        );

        mailSendingConsumer.accept(tickets);
        return tickets;
    }

    private TicketEntity createTicketEntity(EventEntity eventEntity, Optional<String> userId) {
        return new TicketEntity(
                null,
                UUID.randomUUID(),
                eventEntity.getTicketPrice(),
                eventEntity,
                userId.orElse(null)
        );
    }
}
