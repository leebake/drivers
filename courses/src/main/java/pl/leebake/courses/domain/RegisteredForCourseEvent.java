package pl.leebake.courses.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
public class RegisteredForCourseEvent implements DomainEvent {
    public static final String TYPE = "student.registered-for-course";

    @NonNull
    private final Instant when;
    @NonNull
    private final UUID uuid;
    @Getter
    @NonNull
    private final Person person;
    @Getter
    @NonNull
    private final Category category;

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
