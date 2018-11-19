package pl.leebake.courses.domain.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Instant;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = RegisteredForCourseEvent.TYPE, value = RegisteredForCourseEvent.class),
        @JsonSubTypes.Type(name = LicenseGrantedEvent.TYPE, value = LicenseGrantedEvent.class),
        @JsonSubTypes.Type(name = RegisteredForExamEvent.TYPE, value = RegisteredForExamEvent.class),
        @JsonSubTypes.Type(name = CourseCompletedEvent.TYPE, value = CourseCompletedEvent.class),
        @JsonSubTypes.Type(name = LicenseForbiddenEvent.TYPE, value = LicenseForbiddenEvent.class)
})
public interface DomainEvent {

    String type();
    Instant when();
    UUID uuid();
}

