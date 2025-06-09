package io.tanibilet.server.mailing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.tickets.entities.TicketEntity;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void sendTicketViaEmail(List<TicketEntity> ticketsToSend, String email) {
        if (ticketsToSend == null || ticketsToSend.isEmpty()) {
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Twoje bilety na wydarzenie");
            helper.setText(buildEmailContent(ticketsToSend), true);

            int index = 1;
            for (TicketEntity ticket : ticketsToSend) {
                byte[] qrCodeImage = generateQrCode(ticket.getQrCodeId().toString());
                String attachmentName = "qr-code-" + index++ + ".png";
                helper.addAttachment(attachmentName, new ByteArrayResource(qrCodeImage));
            }

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
//            throw new RuntimeException("Nie udało się wysłać e-maila", e);
        }
    }

    @Override
    @Async
    public void sendTicketViaEmail(List<TicketEntity> ticketsToSend, UserPrincipal user) {
        sendTicketViaEmail(ticketsToSend, user.getEmail());
    }

    private byte[] generateQrCode(String qrCodeText) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitMatrix bitMatrix = new QRCodeWriter().encode(qrCodeText, BarcodeFormat.QR_CODE, 200, 200);
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream);
        return stream.toByteArray();
    }

    private String buildEmailContent(List<TicketEntity> tickets) {
        StringBuilder content = new StringBuilder("<h1>Twoje bilety</h1>");
        for (TicketEntity ticket : tickets) {
            content.append("<p><b>Cena:</b> ").append(ticket.getBoughtPrice()).append(" PLN<br>")
                    .append("<b>QR ID:</b> ").append(ticket.getQrCodeId()).append("</p><hr>");
        }
        return content.toString();
    }
}