package de.overcraft.command;

import de.overcraft.Sections;

import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.Arrays;
import java.util.List;

public interface SlashCommandTemplates {

    SlashCommandOption COMMAND_OPTION_SECTIONS = SlashCommandOption.createWithChoices(
            SlashCommandOptionType.DECIMAL,
            "section",
            "section to choose from",
            true,
            FromEnum(Sections.SectionEnum.values())
    );

    static SlashCommandOptionChoice FromEnum(Enum<?> en) {
        return SlashCommandOptionChoice.create(en.name(), en.ordinal());
    }

    static List<SlashCommandOptionChoice> FromEnum(Enum<?>[] enums) {
        return Arrays.stream(enums).map(SlashCommandTemplates::FromEnum).toList();
    }
}
