package pl.leebake.courses.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class Person {

    private final String firstname;
    private final String lastname;
    private final LocalDate birthDate;
}
