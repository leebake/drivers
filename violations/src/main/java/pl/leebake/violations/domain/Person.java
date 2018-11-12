package pl.leebake.violations.domain;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Person {

    private final String firstname;
    private final String lastname;
    private final String pesel;
}
