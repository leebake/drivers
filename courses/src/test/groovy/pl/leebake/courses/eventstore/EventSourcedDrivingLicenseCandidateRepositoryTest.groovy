package pl.leebake.courses.eventstore

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.leebake.courses.domain.Category
import pl.leebake.courses.domain.DrivingLicenseCandidate
import pl.leebake.courses.domain.DrivingLicenseCandidateRepository
import pl.leebake.courses.domain.Person
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant
import java.time.LocalDate
import java.time.Month

import static java.time.LocalDate.now
import static java.time.ZoneId.systemDefault
import static java.time.temporal.ChronoUnit.DAYS

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventSourcedDrivingLicenseCandidateRepositoryTest extends Specification {

    private static final Instant TODAY = now().atStartOfDay(systemDefault()).toInstant()
    private static final Instant TOMORROW = TODAY.plus(1, DAYS)
    private static final Instant DAY_AFTER_TOMORROW =  TOMORROW.plus(1, DAYS)

    @Autowired
    @Subject
    DrivingLicenseCandidateRepository drivingLicenseCandidateRepository

    private static final UUID aggregateUUID = UUID.randomUUID()

    def "should store and load candidate"() {
        given:
            DrivingLicenseCandidate stored = DrivingLicenseCandidate.from(aggregateUUID, Collections.emptyList())
        when:
            drivingLicenseCandidateRepository.save(stored)
        and:
            DrivingLicenseCandidate loaded = drivingLicenseCandidateRepository.getByUUID(aggregateUUID)
        then:
            loaded.uuid == aggregateUUID
            !loaded.isRegisteredForCourse()
            !loaded.isRegisteredForExam()
            !loaded.isCourseCompleted()
    }

    def "should load candidate at given date"() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.of(1990, Month.JANUARY, 1))
                .build()
        DrivingLicenseCandidate stored = DrivingLicenseCandidate.from(aggregateUUID, Collections.emptyList())
                .registerForCourse(person, Category.C, TOMORROW)
                .completeCourse(DAY_AFTER_TOMORROW)
        when:
            drivingLicenseCandidateRepository.save(stored)
        and:
            DrivingLicenseCandidate registeredForCourse = drivingLicenseCandidateRepository.getByUUIDAt(aggregateUUID, TOMORROW)
            DrivingLicenseCandidate courseCompleted = drivingLicenseCandidateRepository.getByUUIDAt(aggregateUUID, DAY_AFTER_TOMORROW)
        then:
            registeredForCourse.isRegisteredForCourse()
            !registeredForCourse.isCourseCompleted()
            courseCompleted.isCourseCompleted()
            !courseCompleted.isRegisteredForExam()

    }
}
