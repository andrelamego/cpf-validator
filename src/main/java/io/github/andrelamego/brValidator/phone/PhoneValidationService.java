package io.github.andrelamego.brValidator.phone;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class PhoneValidationService {

    public boolean isValid(
            String phone,
            boolean formatted,
            boolean allowLandline,
            boolean allowCountryCode,
            boolean rejectRepeatedDigits,
            String[] allowedAreaCodes,
            String[] blockedAreaCodes
    ) {
        if (phone == null || phone.isBlank()) {
            return false;
        }

        String rawPhone = phone.trim();
        String digits = formatted ? rawPhone.replaceAll("[^0-9]", "") : rawPhone;

        if (!digits.matches("\\d+")) {
            return false;
        }

        boolean hasCountryCode = false;
        if (digits.startsWith("55") && (digits.length() == 12 || digits.length() == 13)) {
            hasCountryCode = true;
            digits = digits.substring(2);
        }

        if (hasCountryCode && !allowCountryCode) {
            return false;
        }

        if (!hasCountryCode && digits.length() > 11) {
            return false;
        }

        if (digits.length() != 11 && !(allowLandline && digits.length() == 10)) {
            return false;
        }

        String areaCode = digits.substring(0, 2);
        if (!isValidAreaCode(areaCode)) {
            return false;
        }

        Set<String> allowed = normalizeAreaCodes(allowedAreaCodes);
        Set<String> blocked = normalizeAreaCodes(blockedAreaCodes);

        if (!allowed.isEmpty() && !allowed.contains(areaCode)) {
            return false;
        }

        if (!blocked.isEmpty() && blocked.contains(areaCode)) {
            return false;
        }

        if (rejectRepeatedDigits && (
                digits.matches("(\\d)\\1+") ||
                digits.substring(2).matches("(\\d)\\1+")
        )) {
            return false;
        }

        String subscriberNumber = digits.substring(2);
        if (digits.length() == 11) {
            // celular BR: deve iniciar com 9 e ter 9 dígitos
            return subscriberNumber.matches("9\\d{8}");
        }

        // telefone fixo BR: 8 dígitos iniciando de 2 a 8
        return subscriberNumber.matches("[2-8]\\d{7}");
    }

    public boolean isValid(String phone) {
        return isValid(
                phone,
                true,
                false,
                true,
                true,
                new String[]{},
                new String[]{}
        );
    }

    private Set<String> normalizeAreaCodes(String[] areaCodes) {
        Set<String> normalized = new HashSet<>();

        if (areaCodes == null) {
            return normalized;
        }

        for (String areaCode : Arrays.asList(areaCodes)) {
            if (areaCode == null) {
                continue;
            }

            String digits = areaCode.trim().replaceAll("[^0-9]", "");
            if (digits.length() == 2) {
                normalized.add(digits);
            }
        }

        return normalized;
    }

    private boolean isValidAreaCode(String areaCode) {
        if (!areaCode.matches("\\d{2}")) {
            return false;
        }

        return !areaCode.equals("00");
    }
}
