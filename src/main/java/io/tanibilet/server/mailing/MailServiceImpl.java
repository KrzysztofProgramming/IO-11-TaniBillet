package io.tanibilet.server.mailing;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.events.entities.TicketEntity;
import org.springframework.stereotype.Component;

@Component
public class MailServiceImpl implements MailService {

    @Override
    public void sendTicketViaEmail(TicketEntity ticketToSend, String email) {

    }

    @Override
    public void sendTicketViaEmail(TicketEntity ticketToSend, UserPrincipal user) {

    }
}
