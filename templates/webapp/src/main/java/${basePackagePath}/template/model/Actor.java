package ${basePackage}.template.model;

import java.time.LocalDate;

/**
 * Actor data object
 *
 * @author pwalser
 * @since 25.01.2018.
 */
public class Actor {

    private final String firstName;

    private final String lastName;

    private final LocalDate birthDate;

    public Actor(String firstName, String lastName, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}