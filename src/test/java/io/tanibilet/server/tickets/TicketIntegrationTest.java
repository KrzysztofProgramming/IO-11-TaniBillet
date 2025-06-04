package io.tanibilet.server.tickets;

import io.tanibilet.server.auth.UserPrincipal;
import io.tanibilet.server.events.EventRepository;
import io.tanibilet.server.events.entities.EventEntity;
import io.tanibilet.server.events.entities.EventType;
import io.tanibilet.server.mailing.MailService;
import io.tanibilet.server.tickets.entities.TicketEntity;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.mock;

public class TicketIntegrationTest {
    private TicketRepository ticketRepository = mock(TicketRepository.class);
    private EventRepository  eventRepository = mock(EventRepository.class);
    private MailService mailService = mock(MailService.class);
    private TicketService ticketService = new TicketService(ticketRepository, eventRepository, mailService);
    private TicketController ticketController = new TicketController(ticketService);

    private UserPrincipal user;
    private TicketEntity ticketEntity;

    @BeforeEach
    void SetUp() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_event_creator"));

        user = new UserPrincipal(
                "1",
                "test@mail.com",
                "TestUser",
                true,
                "John",
                "Doe",
                authorities
        );
        UUID uuid = new UUID(1000L, 500L);

        EventEntity eventEntity = new EventEntity(
                null,
                "Test",
                ZonedDateTime.now().plusDays(1),
                ZonedDateTime.now().plusDays(2),
                "Kraków",
                50.0,
                200,
                user.userId(),
                "Test description",
                false,
                EventType.CONCERT,
                new HashSet<>()
        );

        ticketEntity = new TicketEntity();
        ticketEntity.setId(1L);
        ticketEntity.setQrCodeId(uuid);
        ticketEntity.setBoughtPrice(50.0);
        ticketEntity.setSeat(12);
        ticketEntity.setEvent(eventEntity);
        ticketEntity.setUserId(user.userId());
    }


}
