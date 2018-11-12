package pl.leebake.violations.eventstore

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.leebake.violations.domain.Category
import pl.leebake.violations.domain.Person
import pl.leebake.violations.domain.Violation
import pl.leebake.violations.domain.ViolationRepository
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

import static java.time.LocalDate.now
import static java.time.ZoneId.systemDefault
import static java.time.temporal.ChronoUnit.DAYS

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventSourcedViolationRepositoryTest extends Specification {

    private static final Instant TODAY = now().atStartOfDay(systemDefault()).toInstant()
    private static final Instant TOMORROW = TODAY.plus(1, DAYS)
    private static final Instant DAY_AFTER_TOMORROW =  TOMORROW.plus(1, DAYS)

    @Autowired
    @Subject
    ViolationRepository violationRepository

    private static final UUID aggregateUUID = UUID.randomUUID()

    def "should store and load violation"() {
        given:
            Violation stored = Violation.from(aggregateUUID, Collections.emptyList())
        when:
            violationRepository.save(stored)
        and:
            Violation loaded = violationRepository.getByUUID(aggregateUUID)
        then:
            loaded.uuid == aggregateUUID
            loaded.hasNoLicense()
            !loaded.isAlreadyRevoked()
            !loaded.isGrantedAgain()
    }

    def "should load violation at given date"() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .pesel("50122026321")
                .build()
        Violation stored = Violation.from(aggregateUUID, Collections.emptyList())
                .grantLicense(person, Category.C, TOMORROW)
                .revoke(DAY_AFTER_TOMORROW)
        when:
            violationRepository.save(stored)
        and:
            Violation granted = violationRepository.getByUUIDAt(aggregateUUID, TOMORROW)
            Violation revoked = violationRepository.getByUUIDAt(aggregateUUID, DAY_AFTER_TOMORROW)
        then:
            !granted.hasNoLicense()
            revoked.isAlreadyRevoked()

    }
}
