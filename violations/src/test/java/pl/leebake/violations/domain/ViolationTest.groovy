package pl.leebake.violations.domain

import pl.leebake.violations.domain.events.DomainEvent
import spock.lang.Specification

import java.time.Instant

class ViolationTest extends Specification {

    Violation violation = new Violation(UUID.randomUUID(), new ArrayList<DomainEvent>())

    def 'can revoke'() {
        given:
        def person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .pesel("50122026321")
                .build()
        when:
        violation.grantLicense(person, Category.C, Instant.now())

        and:
        violation.revoke(Instant.now())

        then:
        violation.isAlreadyRevoked()
    }

    def 'can grant again'() {
        given:
        def person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .pesel("50122026321")
                .build()
        and:
        violation.grantLicense(person, Category.C, Instant.now())

        and:
        violation.revoke(Instant.now())

        when:
        violation.grantLicenseAgain(Instant.now())

        then:
        violation.isGrantedAgain()
    }

    def 'can not revoke when driver has no license'() {
        when:
        violation.revoke(Instant.now())

        then:
        thrown(IllegalStateException)
    }

    def 'can not revoke when already revoked'() {
        given:
        def person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .pesel("50122026321")
                .build()
        and:
        violation.grantLicense(person, Category.C, Instant.now())

        and:
        violation.revoke(Instant.now())

        when:
        violation.revoke(Instant.now())

        then:
        thrown(IllegalStateException)
    }

    def 'can not grant again when is not revoked'() {
        given:
        def person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .pesel("50122026321")
                .build()
        and:
        violation.grantLicense(person, Category.C, Instant.now())

        when:
        violation.grantLicenseAgain(Instant.now())

        then:
        thrown(IllegalStateException)
    }

}
