package io.tanibilet.server.events.entities;

import lombok.Getter;

@Getter
public enum EventType {
    CONCERT("Koncert"),
    CONFERENCE("Konferencja"),
    WORKSHOP("Warsztat"),
    MEETUP("Spotkanie"),
    FESTIVAL("Festiwal");

    private final String polishName;

    EventType(String polishName) {
        this.polishName = polishName;
    }

}
