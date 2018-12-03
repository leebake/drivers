package pl.leebake.courses.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.leebake.courses.domain.Category
import pl.leebake.courses.domain.CourseService
import pl.leebake.courses.domain.DrivingLicenseCandidateRepository
import pl.leebake.courses.domain.Person
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseServiceTest extends Specification {

    @Autowired
    DrivingLicenseCandidateRepository repository

    @Subject
    @Autowired
    CourseService courseService

    def "can register for course"() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(20))
                .build()

        when:
        def uuid = courseService.registerForCourse(person, Category.B, Instant.now())

        then:
        repository.getByUUID(uuid).isRegisteredForCourse()
    }

    def 'can complete course'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(20))
                .build()

        and:
        def uuid = courseService.registerForCourse(person, Category.B, Instant.now())

        when:
        courseService.completeCourse(uuid, Instant.now())

        then:
        repository.getByUUID(uuid).isCourseCompleted()
    }

    def 'can register for exam'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(20))
                .build()

        and:
        def uuid = courseService.registerForCourse(person, Category.B, Instant.now())

        and:
        courseService.completeCourse(uuid, Instant.now())

        when:
        courseService.registerForExam(uuid, Instant.now())

        then:
        repository.getByUUID(uuid).isRegisteredForExam()
    }

    def 'can pass the exam'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(20))
                .build()

        and:
        def uuid = courseService.registerForCourse(person, Category.B, Instant.now())

        and:
        courseService.completeCourse(uuid, Instant.now())

        and:
        courseService.registerForExam(uuid, Instant.now())

        when:
        courseService.grantLicense(uuid)

        then:
        repository.getByUUID(uuid).isLicenseGranted()
    }

    def 'can forbid license'() {
        given:
        Person person = Person.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .birthDate(LocalDate.now().minusYears(20))
                .build()


        and:
        def uuid = courseService.registerForCourse(person, Category.B, Instant.now())

        when:
        courseService.forbidLicense(uuid)

        then:
        repository.getByUUID(uuid).isLicenseForbidden()
    }

}
