package io.tanibilet.server.mailing;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.events.entities.TicketEntity;

public interface MailService {
    void sendTicketViaEmail(TicketEntity ticketToSend, String email);
    void sendTicketViaEmail(TicketEntity ticketToSend, UserPrincipal user);
}
