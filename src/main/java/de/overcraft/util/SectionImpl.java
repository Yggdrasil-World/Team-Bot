package de.overcraft.util;

import de.overcraft.util.Section;
import org.javacord.api.entity.permission.Role;

public record SectionImpl(long[] sectionManagers, Role role) implements Section {
}
