package io.tanibilet.server.mailing;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.tickets.entities.TicketEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface MailService {
    @Async
    void sendTicketViaEmail(List<TicketEntity> ticketToSend, String email);
    @Async
    void sendTicketViaEmail(List<TicketEntity> ticketToSend, UserPrincipal user);
}
