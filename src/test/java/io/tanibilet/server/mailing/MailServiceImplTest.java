package io.tanibilet.server.mailing;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.tickets.entities.TicketEntity;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Set;
import java.util.UUID;

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
                .build();

        String recipientEmail = "tani.biletio@gmail.com";

        // Act
        mailService.sendTicketViaEmail(List.of(ticket), recipientEmail);

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
                .build();

        // Act
        mailService.sendTicketViaEmail(List.of(ticket), user);

        // Assert
        verify(mailSender, times(1)).send(mimeMessage);
    }
}