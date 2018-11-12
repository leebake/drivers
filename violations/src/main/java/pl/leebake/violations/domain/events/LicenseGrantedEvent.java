package pl.leebake.violations.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
import pl.leebake.violations.domain.Category;
import pl.leebake.violations.domain.Person;

import java.time.Instant;
import java.util.UUID;

@Builder
@RequiredArgsConstructor(onConstructor_ = {@JsonCreator})
@Getter
public class LicenseGrantedEvent implements DomainEvent {
    public static final String TYPE = "license.granted";

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
