package com.gearit.common.utils;

/**
 * Конвертер преобразует строку в необходимый, переданный Enum.
 */
public class EnumConverter {

    public static <S extends Enum<S>, T extends Enum<T>> T toEnum(Class<T> toEnumClass, S fromEnumClass) {
        if (fromEnumClass == null) {
            throw new IllegalArgumentException("Value cannot be null for enum " + toEnumClass.getName());
        }

        for (T constant : toEnumClass.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(fromEnumClass.name().trim())) {
                return constant;
            }
        }

        throw new IllegalArgumentException("No enum constant " + toEnumClass.getName() + " for value: " + fromEnumClass.name());
    }
}
