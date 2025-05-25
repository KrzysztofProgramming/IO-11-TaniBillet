package io.tanibilet.server.mailing;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.tickets.entities.TicketEntity;
import org.springframework.scheduling.annotation.Async;

public interface MailService {
    @Async
    void sendTicketViaEmail(TicketEntity ticketToSend, String email);
    @Async
    void sendTicketViaEmail(TicketEntity ticketToSend, UserPrincipal user);
}
