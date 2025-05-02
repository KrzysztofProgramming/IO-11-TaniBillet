package io.tanibilet.server.tickets.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;


@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime eventStartTimeMillis;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime eventEndTimeMillis;
    private String location;
    private Double ticketPrice;
    private Integer maxTicketCount;
    private String ownerUserId;
}