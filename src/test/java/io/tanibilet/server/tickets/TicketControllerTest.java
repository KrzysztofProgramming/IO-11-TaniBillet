package io.tanibilet.server.tickets;

import io.tanibilet.server.auth.UserPrincipal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketControllerTest {

    private TicketService ticketService = mock(TicketService.class);

    private static UserPrincipal user;

    @BeforeAll
    static void SetUp() {
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
    }

    @Test
    void testGetTicketsForUser()
    {
        //Arrange

        //Act

        //Assert
    }

    @Test
    void testGetTicketForUserStatusOk()
    {
        //Arrange

        //Act

        //Assert
    }

    @Test
    void testGetTicketForUserStatusNotFound()
    {
        //Arrange

        //Act

        //Assert
    }

    @Test
    void testOrderTicketStatusOk()
    {
        //Arrange

        //Act

        //Assert
    }

    @Test
    void testOrderTicketStatusNotFound()
    {
        //Arrange

        //Act

        //Assert
    }

    @Test
    void testOrderUnauthenticatedTicketStatusOk()
    {
        //Arrange

        //Act

        //Assert
    }

    @Test
    void testOrderUnauthenticatedTicketStatusNotFound()
    {
        //Arrange

        //Act

        //Assert
    }



}
