package net.sghill.ci.sentry.validation;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class EnumerationValidator implements ConstraintValidator<Enumeration, String> {
    private final Set<String> validNames = Sets.newHashSet();

    @Override
    public void initialize(Enumeration at) {
        Class<? extends Enum<?>> givenEnum = at.of();
        Set<Enum<?>> enums = Sets.newHashSet(givenEnum.getEnumConstants());
        for (Enum<?> anEnum : enums) {
            validNames.add(anEnum.name());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = validNames.contains(Strings.nullToEmpty(value));
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(String.format(
                            "%s is not valid. Valid options are %s", value, validNames
                    ))
                    .addConstraintViolation();
        }
        return isValid;
    }
}
