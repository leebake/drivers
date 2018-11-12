package pl.leebake.violations.eventstore;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.leebake.violations.domain.Violation;
import pl.leebake.violations.domain.ViolationRepository;
import pl.leebake.violations.domain.events.DomainEvent;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class EventSourcedViolationRepository implements ViolationRepository {

    private final EventStore eventStore;
    private final EventSerializer eventSerializer;

    @Override
    public Violation save(Violation aggregate) {
        List<EventDescriptor> events = aggregate.getPendingEvents()
                .stream()
                .map(eventSerializer::serialize)
                .collect(Collectors.toList());
        eventStore.saveEvents(aggregate.getUuid(), events);
        return aggregate.markChangesAsCommited();
    }

    @Override
    public Violation getByUUID(UUID uuid) {
        return Violation.from(uuid, getRelatedEvents(uuid));
    }

    @Override
    public Violation getByUUIDAt(UUID uuid, Instant at) {
        return Violation.from(uuid,
                getRelatedEvents(uuid)
                .stream()
                .filter(event -> !event.when().isAfter(at))
                .collect(toList())
        );
    }

    private List<DomainEvent> getRelatedEvents(UUID uuid) {
        return eventStore.getEventsForAggregate(uuid)
                .stream()
                .map(eventSerializer::deserialize)
                .collect(toList());
    }
}
