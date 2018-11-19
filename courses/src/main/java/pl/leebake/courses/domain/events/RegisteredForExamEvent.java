package pl.leebake.courses.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Builder
@RequiredArgsConstructor(onConstructor_ = {@JsonCreator})
@Getter
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
