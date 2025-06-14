package io.tanibilet.server.tickets.entities;

import io.tanibilet.server.events.entities.EventEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID qrCodeId;
    private Double boughtPrice;

    @ManyToOne
    private EventEntity event;

    @Nullable
    private String userId;
}
