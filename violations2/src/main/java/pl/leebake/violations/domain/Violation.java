package pl.leebake.violations.domain;

import io.vavr.API;
import io.vavr.Predicates;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.leebake.violations.domain.events.DomainEvent;
import pl.leebake.violations.domain.events.LicenseGrantedAgainEvent;
import pl.leebake.violations.domain.events.LicenseGrantedEvent;
import pl.leebake.violations.domain.events.LicenseRevokedEvent;

import java.time.Instant;
import java.util.*;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.collection.List.ofAll;

@RequiredArgsConstructor
public class Violation {

    @Getter
    private final UUID uuid;
    @Getter
    private final List<DomainEvent> pendingEvents;

    private Person person;
    private Category category;
    private Instant licenseGrantedAgainDate;
    private Instant licenseRevokedDate;

    public Violation revoke(Instant when) {
        if (hasNoLicense()) {
            throw new IllegalStateException();
        }
        if (isAlreadyRevoked()) {
            throw new IllegalStateException();
        }
        LicenseRevokedEvent event = LicenseRevokedEvent.builder()
                .when(when)
                .uuid(uuid)
                .build();
        return handleWithAppend(event);
    }

    public boolean isAlreadyRevoked() {
        return Optional.ofNullable(licenseRevokedDate).isPresent();
    }

    public boolean hasNoLicense() {
        return !Optional.ofNullable(person).isPresent() && !Optional.ofNullable(category).isPresent();
    }

    private Violation licenseRevoked(LicenseRevokedEvent event) {
        this.licenseRevokedDate = event.when();
        this.pendingEvents.add(event);
        return this;
    }

    public Violation grantLicenseAgain(Instant when) {
        if (!isAlreadyRevoked()) {
            throw new IllegalStateException();
        }
        LicenseGrantedAgainEvent event = LicenseGrantedAgainEvent.builder()
                .when(when)
                .uuid(uuid)
                .build();
        return handleWithAppend(event);
    }

    private Violation licenseGrantedAgain(LicenseGrantedAgainEvent event) {
        this.licenseGrantedAgainDate = event.when();
        return this;
    }

    public boolean isGrantedAgain() {
        return Optional.ofNullable(licenseGrantedAgainDate).isPresent();
    }

    public Violation grantLicense(Person person, Category category, Instant when) {
        LicenseGrantedEvent event = LicenseGrantedEvent.builder()
                .when(when)
                .person(person)
                .category(category)
                .uuid(uuid)
                .build();
        return handleWithAppend(event);
    }

    private Violation licenseGranted(LicenseGrantedEvent event) {
        this.person = event.getPerson();
        this.category = event.getCategory();
        return this;
    }

    public Violation markChangesAsCommited() {
        return new Violation(uuid, Collections.emptyList());
    }

    private Violation handleWithAppend(DomainEvent domainEvent) {
        this.pendingEvents.add(domainEvent);
        return this.handle(domainEvent);
    }

    private Violation handle(DomainEvent event) {
        return API.Match(event).of(
                Case($(Predicates.instanceOf(LicenseGrantedEvent.class)), this::licenseGranted),
                Case($(Predicates.instanceOf(LicenseGrantedAgainEvent.class)), this::licenseGrantedAgain),
                Case($(Predicates.instanceOf(LicenseRevokedEvent.class)), this::licenseRevoked)
        );
    }

    public static Violation from(UUID uuid, List<DomainEvent> domainEvents) {
        return ofAll(domainEvents).foldLeft(new Violation(uuid, new ArrayList<>()), Violation::handle);
    }
}
