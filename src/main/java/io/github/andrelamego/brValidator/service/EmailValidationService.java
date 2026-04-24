package io.github.andrelamego.brValidator.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class EmailValidationService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Set<String> DISPOSABLE_DOMAINS = Set.of(
            "tempmail.com",
            "10minutemail.com",
            "guerrillamail.com",
            "mailinator.com",
            "trashmail.com",
            "yopmail.com"
    );

    public boolean isValid(String email,
                           boolean allowPlusAlias,
                           boolean disposableAllowed,
                           String[] allowedDomains,
                           String[] blockedDomains
    ) {
        if(email == null || email.isBlank()){
            return false;
        }

        email = email.trim().toLowerCase();

        if(!EMAIL_PATTERN.matcher(email).matches()) {
            return false;
        }

        if(!allowPlusAlias && email.contains("+")){
            return false;
        }

        String domain = email.substring(email.indexOf("@") + 1);

        if(!disposableAllowed && DISPOSABLE_DOMAINS.contains(domain)){
            return false;
        }

        Set<String> allowed = new HashSet<>(Arrays.asList(allowedDomains));
        Set<String> blocked = new HashSet<>(Arrays.asList(blockedDomains));

        if (!allowed.isEmpty() && !allowed.contains(domain)) {
            return false;
        }

        if (!blocked.isEmpty() && blocked.contains(domain)) {
            return false;
        }

        return true;
    }

    public boolean isValid(String email) {
        return isValid(
                email,
                true,
                true,
                new String[]{},
                new String[]{}
        );
    }
}
