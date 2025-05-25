package io.tanibilet.server.mailing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.tickets.entities.TicketEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

import java.io.ByteArrayOutputStream;

@Component
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void sendTicketViaEmail(TicketEntity ticketToSend, String email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Twój bilet na wydarzenie");
            helper.setText(buildEmailContent(ticketToSend), true);

            byte[] qrCodeImage = generateQrCode(ticketToSend.getQrCodeId().toString());
            helper.addAttachment("qr-code.png", new ByteArrayResource(qrCodeImage));

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Nie udało się wysłać e-maila", e);
        }
    }

    private byte[] generateQrCode(String qrCodeText) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitMatrix bitMatrix = new QRCodeWriter().encode(qrCodeText, BarcodeFormat.QR_CODE, 200, 200);
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream);
        return stream.toByteArray();
    }

    @Override
    @Async
    public void sendTicketViaEmail(TicketEntity ticketToSend, UserPrincipal user) {
        sendTicketViaEmail(ticketToSend, user.getEmail());
    }

    private String buildEmailContent(TicketEntity ticket) {
        return "<h1>Twój bilet</h1>" +
                "<p>Miejsce: " + ticket.getSeat() + "</p>" +
                "<p>Cena: " + ticket.getBoughtPrice() + " PLN</p>" +
                "<p>Identyfikator QR: " + ticket.getQrCodeId() + "</p>";
    }
}