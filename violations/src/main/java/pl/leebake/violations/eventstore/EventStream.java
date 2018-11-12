package pl.leebake.violations.eventstore;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static javax.persistence.FetchType.EAGER;

@Entity(name = "event_streams")
@NoArgsConstructor
class EventStream {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Column(unique = true, nullable = false, name = "aggregate_uuid", length = 36)
    private UUID aggregateUUID;

    @Version
    @Column(nullable = false)
    private long version;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = EAGER)
    private List<EventDescriptor> events = new ArrayList<>();

    EventStream(UUID aggregateUUID) {
        this.aggregateUUID = aggregateUUID;
    }

    void addEvents(List<EventDescriptor> events) {
        this.events.addAll(events);
    }

    List<EventDescriptor> getEvents() {
        return events
                .stream()
                .sorted(comparing(EventDescriptor::getOccurredAt))
                .collect(toList());
    }

}
