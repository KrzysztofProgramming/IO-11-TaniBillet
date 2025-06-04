package io.tanibilet.server.events.entities;

import io.tanibilet.server.events.dto.CreateEventDto;
import io.tanibilet.server.tickets.entities.TicketEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;


@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
    private String description;
    private Boolean isBuyingTicketsTurnedOff;
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<TicketEntity> tickets = new HashSet<>();

    public void copyPropertiesFromDto(CreateEventDto updateDto) {
        this.setName(updateDto.name());
        this.setEventStartTimeMillis(updateDto.eventStartTimeMillis());
        this.setEventEndTimeMillis(updateDto.eventEndTimeMillis());
        this.setLocation(updateDto.location());
        this.setTicketPrice(updateDto.ticketPrice());
        this.setMaxTicketCount(updateDto.maxTicketCount());
        this.setDescription(updateDto.description());
        this.setIsBuyingTicketsTurnedOff(updateDto.isBuyingTicketsTurnedOff());
        this.setEventType(updateDto.eventType());
    }

    public int getSoldTicketCount() {
        return tickets.size();
    }
}