package pl.leebake.courses.domain;

import lombok.Builder;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
public class LicenseGrantedEvent implements DomainEvent {
    public static final String TYPE = "license.granted";

    @NonNull
    private final Instant when;
    @NonNull
    private final UUID uuid;

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Instant when() {
        return when;
    }

    @Override
    public UUID uuid() {
        return uuid;
    }
}
