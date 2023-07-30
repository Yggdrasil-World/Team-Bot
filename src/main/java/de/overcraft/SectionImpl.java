package de.overcraft;


import org.javacord.api.entity.permission.Role;

public record SectionImpl(long[] sectionManagers, Role role) implements Section {
}
