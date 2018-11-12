package pl.leebake.violations.eventstore;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "event_descriptors")
@NoArgsConstructor
@Getter
class EventDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 600)
    private String body;

    @Column(nullable = false, name = "occurred_at")
    private Instant occurredAt = Instant.now();

    @Column(nullable = false, length = 60)
    private String type;

    EventDescriptor(String body, Instant occurredAt, String type) {
        this.body = body;
        this.occurredAt = occurredAt;
        this.type = type;
    }
}
