package io.tanibilet.server.tickets;

import io.tanibilet.server.events.entities.TicketEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    Optional<TicketEntity> findOneByIdAndUserId(Long id, String userId);
    boolean existsByEventIdAndSeat(Long eventId, Integer seat);
    Page<TicketEntity> findByUserId(String userId, Pageable pageable);
    long countByEventId(Long eventId);
}
