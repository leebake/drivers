package pl.leebake.courses.eventstore;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.leebake.courses.domain.events.DomainEvent;
import pl.leebake.courses.domain.DrivingLicenseCandidate;
import pl.leebake.courses.domain.DrivingLicenseCandidateRepository;
import pl.leebake.event.store.EventDescriptor;
import pl.leebake.event.store.EventStore;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class EventSourcedDrivingLicenseCandidateRepository implements DrivingLicenseCandidateRepository {

    private final EventStore eventStore;
    private final EventSerializer eventSerializer;

    @Override
    public DrivingLicenseCandidate save(DrivingLicenseCandidate aggregate) {
        List<EventDescriptor> events = aggregate.getPendingEvents()
                .stream()
                .map(eventSerializer::serialize)
                .collect(Collectors.toList());
        eventStore.saveEvents(aggregate.getUuid(), events);
        return aggregate.markChangesAsCommited();
    }

    @Override
    public DrivingLicenseCandidate getByUUID(UUID uuid) {
        return DrivingLicenseCandidate.from(uuid, getRelatedEvents(uuid));
    }

    @Override
    public DrivingLicenseCandidate getByUUIDAt(UUID uuid, Instant at) {
        return DrivingLicenseCandidate.from(uuid,
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
