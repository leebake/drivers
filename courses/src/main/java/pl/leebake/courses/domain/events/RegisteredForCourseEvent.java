package pl.leebake.courses.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.leebake.courses.domain.Category;
import pl.leebake.courses.domain.Person;

import java.time.Instant;
import java.util.UUID;

@Builder
@RequiredArgsConstructor(onConstructor_ = {@JsonCreator})
@Getter
public class RegisteredForCourseEvent implements DomainEvent {
    public static final String TYPE = "student.registered-for-course";

    @NonNull
    private final Instant when;
    @NonNull
    private final UUID uuid;
    @NonNull
    private final Person person;
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
