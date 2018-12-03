package pl.leebake.courses.eventpublish;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import pl.leebake.courses.domain.events.DomainEvent;
import pl.leebake.courses.domain.events.DomainEventPublisher;

import java.util.HashMap;

@Component
public class KafkaDomainEventPublisher implements DomainEventPublisher {

    private final Source source;
    private final ObjectMapper objectMapper;

    public KafkaDomainEventPublisher(Source source, ObjectMapper objectMapper) {
        this.source = source;
        this.objectMapper = objectMapper;
    }

    @Override
    @SneakyThrows
    public void publish(DomainEvent domainEvent) {
        final HashMap<String, Object> headers = new HashMap<>();
        headers.put("type", domainEvent.type());
        source.output().send(new GenericMessage<>(objectMapper.writeValueAsString(domainEvent), headers));
    }
}
