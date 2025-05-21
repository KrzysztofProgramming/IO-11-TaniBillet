package io.tanibilet.server.mailing;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.events.entities.TicketEntity;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.internet.MimeMessage;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MailServiceImplTest {

    private final JavaMailSender mailSender = mock(JavaMailSender.class);
    private final MailServiceImpl mailService = new MailServiceImpl(mailSender);

    @Test
    void shouldSendEmailToProvidedAddress() throws Exception {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        TicketEntity ticket = TicketEntity.builder()
                .qrCodeId(UUID.randomUUID())
                .boughtPrice(50.0)
                .seat(1)
                .build();

        String recipientEmail = "tani.biletio@gmail.com";

        // Act
        mailService.sendTicketViaEmail(ticket, recipientEmail);

        // Assert
        verify(mailSender, times(1)).send(mimeMessage);
        ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        MimeMessage sentMessage = messageCaptor.getValue();
    }

    @Test
    void shouldSendEmailToUserPrincipal() throws Exception {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        UserPrincipal user = new UserPrincipal(
            "testUserId",
            "tani.biletio@gmail.com",
            "testUsername",
            true,
            "TestFirstname",
            "TestLastname",
            Set.of()
        );

        TicketEntity ticket = TicketEntity.builder()
                .qrCodeId(UUID.randomUUID())
                .boughtPrice(50.0)
                .seat(1)
                .build();

        // Act
        mailService.sendTicketViaEmail(ticket, user);

        // Assert
        verify(mailSender, times(1)).send(mimeMessage);
    }
}