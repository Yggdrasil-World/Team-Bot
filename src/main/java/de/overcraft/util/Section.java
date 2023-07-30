package de.overcraft.util;

import de.overcraft.SectionImpl;

import java.util.Arrays;

public interface Section {
    long[] sectionManagers();

    default boolean isManager(long id) {
        return Arrays.stream(sectionManagers()).anyMatch(value -> value == id);
    }

    static Section Of(long[] managers) {
        return new SectionImpl(managers);
    }
}
