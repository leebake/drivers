package pl.leebake.courses.domain;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class CourseService {

    private final DrivingLicenseCandidateRepository drivingLicenseCandidateRepository;

    public CourseService(DrivingLicenseCandidateRepository drivingLicenseCandidateRepository) {
        this.drivingLicenseCandidateRepository = drivingLicenseCandidateRepository;
    }

    public UUID registerForCourse(Person person, Category category, Instant when) {
        DrivingLicenseCandidate drivingLicenseCandidate = DrivingLicenseCandidate.init()
                .registerForCourse(person, category, when);

        return drivingLicenseCandidateRepository.save(drivingLicenseCandidate).getUuid();
    }

    public UUID completeCourse(UUID uuid, Instant when) {
        DrivingLicenseCandidate drivingLicenseCandidate = drivingLicenseCandidateRepository.getByUUID(uuid)
                .completeCourse(when);

        return drivingLicenseCandidateRepository.save(drivingLicenseCandidate).getUuid();
    }

    public UUID registerForExam(UUID uuid, Instant when) {
        DrivingLicenseCandidate drivingLicenseCandidate = drivingLicenseCandidateRepository.getByUUID(uuid)
                .registerForExam(when);

        return drivingLicenseCandidateRepository.save(drivingLicenseCandidate).getUuid();
    }

    public UUID grantLicense(UUID uuid) {
        DrivingLicenseCandidate drivingLicenseCandidate = drivingLicenseCandidateRepository.getByUUID(uuid)
                .grantLicense();
        return drivingLicenseCandidateRepository.save(drivingLicenseCandidate).getUuid();
    }

    public UUID forbidLicense(UUID uuid) {
        DrivingLicenseCandidate drivingLicenseCandidate = drivingLicenseCandidateRepository.getByUUID(uuid)
                .forbidLicense();

        return drivingLicenseCandidateRepository.save(drivingLicenseCandidate).getUuid();
    }
}
