package pl.leebake.violations.domain;

import java.time.Instant;
import java.util.UUID;

public interface ViolationRepository {

    Violation save(Violation aggregate);

    Violation getByUUID(UUID uuid);

    Violation getByUUIDAt(UUID uuid, Instant at);

}
