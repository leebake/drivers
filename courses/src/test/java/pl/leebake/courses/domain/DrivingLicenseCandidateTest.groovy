package pl.leebake.courses.domain

import spock.lang.Specification

import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class DrivingLicenseCandidateTest extends Specification {

    DrivingLicenseCandidate candidate = new DrivingLicenseCandidate(UUID.randomUUID())

    def 'can register for course'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(20))
                .build()

        when:
        candidate.registerForCourse(person, Category.B)

        then:
        candidate.isRegisteredForCourse()
    }

    def 'can complete course'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(20))
                .build()

        and:
        candidate.registerForCourse(person, Category.B)

        when:
        candidate.completeCourse(Instant.now())

        then:
        candidate.isCourseCompleted()
    }

    def 'can register for exam'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(20))
                .build()

        and:
        candidate.registerForCourse(person, Category.B)

        and:
        candidate.completeCourse(Instant.now().minus(1, ChronoUnit.DAYS))

        when:
        candidate.registerForExam(Instant.now())

        then:
        candidate.isRegisteredForExam()
    }

    def 'can pass the exam'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(20))
                .build()

        and:
        candidate.registerForCourse(person, Category.B)

        and:
        candidate.completeCourse(Instant.now().minus(1, ChronoUnit.DAYS))

        and:
        candidate.registerForExam(Instant.now())

        when:
        candidate.grantLicense()

        then:
        candidate.isLicenseGranted()
    }

    def 'cannot register for course when candidate age is under 17'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(15))
                .build()

        when:
        candidate.registerForCourse(person, Category.B)

        then:
        thrown(IllegalStateException)
    }

    def 'can not register for course when already has a license for given category'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(20))
                .build()
        and:
        candidate.registerForCourse(person, Category.B)

        and:
        candidate.completeCourse(Instant.now())

        and:
        candidate.registerForExam(Instant.now())

        and:
        candidate.grantLicense()

        when:
        candidate.registerForCourse(person, Category.B)

        then:
        thrown(IllegalStateException)
    }

    def 'can not register for course when already registered'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(20))
                .build()

        and:
        candidate.registerForCourse(person, Category.B)

        when:
        candidate.registerForCourse(person, Category.B)

        then:
        thrown(IllegalStateException)
    }

    def 'can not register for course when candidate has problems with law'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(20))
                .build()

        and:
        candidate.forbidLicense()

        when:
        candidate.registerForCourse(person, Category.B)

        then:
        thrown(IllegalStateException)
    }

    def 'can not complete course when not registered'() {
        when:
        candidate.completeCourse(Instant.now())

        then:
        thrown(IllegalStateException)
    }

    def 'can not register for exam when student does not have finished course'() {
        when:
        candidate.registerForExam(Instant.now())

        then:
        thrown(IllegalStateException)
    }

    def 'can not register for exam when student is under 18'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(17).minusMonths(10))
                .build()

        and:
        candidate.registerForCourse(person, Category.B)

        and:
        candidate.completeCourse(Instant.now().minus(1, ChronoUnit.DAYS))

        when:
        candidate.registerForExam(Instant.now())

        then:
        thrown(IllegalStateException)
    }

    def 'can not register for exam when student has problems with law'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(20))
                .build()

        and:
        candidate.registerForCourse(person, Category.B)

        and:
        candidate.completeCourse(Instant.now().minus(1, ChronoUnit.DAYS))

        and:
        candidate.forbidLicense()

        when:
        candidate.registerForExam(Instant.now())

        then:
        thrown(IllegalStateException)
    }

    def 'can not license grant when no course completed'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(20))
                .build()

        and:
        candidate.registerForCourse(person, Category.B)

        when:
        candidate.grantLicense()

        then:
        thrown(IllegalStateException)
    }

    def 'can not license grant when no registered for exam'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(20))
                .build()

        and:
        candidate.registerForCourse(person, Category.B)

        and:
        candidate.completeCourse(Instant.now())

        when:
        candidate.grantLicense()

        then:
        thrown(IllegalStateException)
    }

}
