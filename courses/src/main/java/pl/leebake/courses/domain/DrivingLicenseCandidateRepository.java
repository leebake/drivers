package pl.leebake.courses.domain;

import java.time.Instant;
import java.util.UUID;

public interface DrivingLicenseCandidateRepository {

    DrivingLicenseCandidate save(DrivingLicenseCandidate aggregate);

    DrivingLicenseCandidate getByUUID(UUID uuid);

    DrivingLicenseCandidate getByUUIDAt(UUID uuid, Instant at);

}
