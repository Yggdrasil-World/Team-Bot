package de.overcraft.command;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;

import java.util.Set;
import java.util.stream.Collectors;


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
