package io.tanibilet.server.tickets;

import io.tanibilet.server.tickets.entities.TicketEntity;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    Optional<TicketEntity> findOneByIdAndUserId(Long id, String userId);
    List<TicketEntity> findByUserId(String userId);
    long countByEventId(Long eventId);
}
