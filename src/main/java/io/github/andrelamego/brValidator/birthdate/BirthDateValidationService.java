package io.github.andrelamego.brValidator.birthdate;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class BirthDateValidationService {

    public boolean isValid(
            LocalDate birthDate,
            int minAge,
            int maxAge,
            boolean allowFutureDate,
            boolean allowToday
    ) {
        if (birthDate == null) {
            return false;
        }

        if (minAge > maxAge) {
            return false;
        }

        LocalDate today = LocalDate.now();

        if (birthDate.isAfter(today)) {
            return allowFutureDate;
        }

        if (birthDate.isEqual(today) && !allowToday) {
            return false;
        }

        int age = Period.between(birthDate, today).getYears();
        return age >= minAge && age <= maxAge;
    }

    public boolean isValid(LocalDate birthDate) {
        return isValid(
                birthDate,
                0,
                120,
                false,
                true
        );
    }
}
