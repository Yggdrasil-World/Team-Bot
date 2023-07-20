package de.overcraft.command;

import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandFinder {

    public static Set<SlashCommandRegister> FindCommands() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath())
                .setScanners(Scanners.TypesAnnotated));
        Set<SlashCommandRegister> collect = reflections.getTypesAnnotatedWith(RegisterCommand.class).stream()
                .filter(SlashCommandRegister.class::isAssignableFrom)
                .map(aClass -> {
                    try {
                        return ((SlashCommandRegister) aClass.getConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(slashCommandRegister -> slashCommandRegister.get() != null)
                .collect(Collectors.toUnmodifiableSet());
        System.out.println("Commands found: " + collect.size());
        return collect;
    }

}
