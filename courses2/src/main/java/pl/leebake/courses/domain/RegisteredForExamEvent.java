package pl.leebake.courses.domain;

import lombok.Builder;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
public class RegisteredForExamEvent implements DomainEvent {

    public static final String TYPE = "student.registered-for-exam";

    @NonNull
    private UUID uuid;
    @NonNull
    private final Instant when;

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
