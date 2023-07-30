package de.overcraft;

import org.javacord.api.entity.permission.Role;

import java.util.Arrays;

public interface Section {
    long[] sectionManagers();
    Role role();

    default boolean isManager(long id) {
        return Arrays.stream(sectionManagers()).anyMatch(value -> value == id);
    }

    static Section Of(long[] managers, Role role) {
        return new SectionImpl(managers, role);
    }
}
