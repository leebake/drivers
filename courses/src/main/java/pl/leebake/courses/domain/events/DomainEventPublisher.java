package pl.leebake.courses.domain.events;

public interface DomainEventPublisher {

    void publish(DomainEvent domainEvent);

}
