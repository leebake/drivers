package pl.leebake.courses.domain;

import io.vavr.API;
import io.vavr.Predicates;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.collection.List.ofAll;

@RequiredArgsConstructor
public class DrivingLicenseCandidate {

    private final UUID uuid;
    private Person person;
    private Category category;

    private Instant courseRegistrationDate;
    private Instant courseCompletedDate;
    private Instant examRegistrationDate;
    private Instant licenseGrantedDate;
    private Instant licenseForbiddenDate;

    private List<DomainEvent> pendingEvents = new ArrayList<>();


    public void registerForCourse(final Person person, final Category category) {
        if (isUnderSeventeen(person)) {
            throw new IllegalStateException();
        }
        if (isLicenseGranted()) {
            throw new IllegalStateException();
        }
        if (isRegisteredForCourse()) {
            throw new IllegalStateException();
        }
        if (isLicenseForbidden()) {
            throw new IllegalStateException();
        }
        RegisteredForCourseEvent event = RegisteredForCourseEvent.builder()
                .person(person)
                .category(category)
                .when(Instant.now())
                .uuid(uuid)
                .build();
        handleWithAppend(event);
    }

    private boolean isLicenseForbidden() {
        return Optional.ofNullable(licenseForbiddenDate).isPresent();
    }

    boolean isLicenseGranted() {
        return Optional.ofNullable(licenseGrantedDate).isPresent();
    }

    private boolean isUnderSeventeen(Person person) {
        return person.getBirthDate().isAfter(LocalDate.now().minusYears(17));
    }

    private DrivingLicenseCandidate registeredForCourse(final RegisteredForCourseEvent event) {
        this.person = event.getPerson();
        this.category = event.getCategory();
        this.courseRegistrationDate = event.when();
        return this;
    }

    public void completeCourse(final Instant when) {
        if (!isRegisteredForCourse()) {
            throw new IllegalStateException();
        }
        CourseCompletedEvent event = CourseCompletedEvent.builder().when(when).uuid(uuid).build();
        handleWithAppend(event);
    }

    private DrivingLicenseCandidate courseCompleted(CourseCompletedEvent event) {
        this.courseCompletedDate = event.when();
        return this;
    }

    public void registerForExam(final Instant when) {
        if (!isCourseCompleted()) {
            throw new IllegalStateException();
        }
        if (isUnderEighteen()) {
            throw new IllegalStateException();
        }
        if (isLicenseForbidden()) {
            throw new IllegalStateException();
        }
        RegisteredForExamEvent event = RegisteredForExamEvent.builder().uuid(uuid).when(when).build();
        handleWithAppend(event);
    }

    private boolean isUnderEighteen() {
        return person.getBirthDate().isAfter(LocalDate.now().minusYears(18));
    }

    private DrivingLicenseCandidate registeredForExam(RegisteredForExamEvent event) {
        this.examRegistrationDate = event.when();
        return this;
    }

    public void grantLicense() {
        if (!isCourseCompleted()) {
            throw new IllegalStateException();
        }
        if (!isRegisteredForExam()) {
            throw new IllegalStateException();
        }
        final LicenseGrantedEvent event = LicenseGrantedEvent.builder().uuid(this.uuid).when(Instant.now()).build();
        handleWithAppend(event);
    }

    private DrivingLicenseCandidate licenseGranted(LicenseGrantedEvent event) {
        this.licenseGrantedDate = event.when();
        return this;
    }

    public void forbidLicense() {
        final LicenseForbiddenEvent event = LicenseForbiddenEvent.builder().uuid(uuid).when(Instant.now()).build();
        handleWithAppend(event);
    }

    private DrivingLicenseCandidate licenseForbidden(LicenseForbiddenEvent event) {
        this.licenseForbiddenDate = event.when();
        return this;
    }

    public boolean isRegisteredForCourse() {
        return Optional.ofNullable(courseRegistrationDate).isPresent();
    }

    public boolean isCourseCompleted() {
        return Optional.ofNullable(courseCompletedDate).isPresent();
    }

    public boolean isRegisteredForExam() {
        return Optional.ofNullable(examRegistrationDate).isPresent();
    }

    private void flushEvents() {
        this.pendingEvents.clear();
    }

    private DrivingLicenseCandidate handleWithAppend(DomainEvent domainEvent) {
        this.pendingEvents.add(domainEvent);
        return this.handle(domainEvent);
    }

    private DrivingLicenseCandidate handle(DomainEvent event) {
        return API.Match(event).of(
                Case($(Predicates.instanceOf(CourseCompletedEvent.class)), this::courseCompleted),
                Case($(Predicates.instanceOf(LicenseForbiddenEvent.class)), this::licenseForbidden),
                Case($(Predicates.instanceOf(LicenseGrantedEvent.class)), this::licenseGranted),
                Case($(Predicates.instanceOf(RegisteredForCourseEvent.class)), this::registeredForCourse),
                Case($(Predicates.instanceOf(RegisteredForExamEvent.class)), this::registeredForExam)
        );
    }

    public static DrivingLicenseCandidate from(UUID uuid, List<DomainEvent> domainEvents) {
        return ofAll(domainEvents).foldLeft(new DrivingLicenseCandidate(uuid), DrivingLicenseCandidate::handle);
    }
}
