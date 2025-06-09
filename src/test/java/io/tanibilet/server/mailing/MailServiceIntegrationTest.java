package io.tanibilet.server.mailing;

import io.tanibilet.server.tickets.entities.TicketEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class MailServiceIntegrationTest {

    @Autowired
    private MailService mailService;

    @Test
    void shouldSendRealEmail() {
        // Arrange
        TicketEntity ticket = TicketEntity.builder()
                .qrCodeId(UUID.randomUUID())
                .boughtPrice(50.0)
                .build();

        String recipientEmail = "tani.biletio@gmail.com";

        // Act
        mailService.sendTicketViaEmail(ticket, recipientEmail);


    }
}