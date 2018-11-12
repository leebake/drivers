package pl.leebake.violations.domain.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Instant;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = LicenseGrantedAgainEvent.TYPE, value = LicenseGrantedAgainEvent.class),
        @JsonSubTypes.Type(name = LicenseGrantedEvent.TYPE, value = LicenseGrantedEvent.class),
        @JsonSubTypes.Type(name = LicenseRevokedEvent.TYPE, value = LicenseRevokedEvent.class)
})
public interface DomainEvent {

    String type();

    Instant when();

    UUID uuid();
}
