package pl.leebake.courses.domain;

import java.time.Instant;
import java.util.UUID;

interface DomainEvent {

    String type();
    Instant when();
    UUID uuid();
}
