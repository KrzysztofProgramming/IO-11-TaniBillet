package io.tanibilet.server.tickets;

import io.tanibilet.server.tickets.entities.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    Optional<TicketEntity> findOneByIdAndUserId(Long id, String userId);
    List<TicketEntity> findByUserId(String userId);
    long countByEventId(Long eventId);
}
