package pl.leebake.violations.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Builder
@RequiredArgsConstructor(onConstructor_ = {@JsonCreator})
@Getter
public class LicenseRevokedEvent implements DomainEvent {
    public static final String TYPE = "license.revoked";

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
