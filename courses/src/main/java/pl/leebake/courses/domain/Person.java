package pl.leebake.courses.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@Builder
@Getter
public class Person {

    @NonNull
    private final String firstname;
    @NonNull
    private final String lastname;
    @NonNull
    private final LocalDate birthDate;
}
