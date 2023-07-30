package de.overcraft;

import de.overcraft.command.CommandFinder;
import de.overcraft.command.SlashCommandHandler;
import de.overcraft.message.MessageHandler;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ServerChannel;


public class Bot {

    private static Bot bot;
    private static final long DefaultServer = 1021108286302453821L;
    private final DiscordApi api;
    private final SlashCommandHandler slashCommandHandler;
    private final MessageHandler messageHandler;
    private final Sections sections;
    private final ServerChannel welcomeChannel;

    public Bot(DiscordApi api) {
        bot = this;
        this.api = api;
        this.sections = new Sections(
                Section.Of(new long[]{503603263862734858L, 415544998558433280L}, api.getRoleById(1021111128308318248L).get()), // Builder
                Section.Of(new long[]{620685606447611914L}, api.getRoleById(1026589866328346705L).get()), // Developer
                Section.Of(new long[]{1082920396724121600L}, api.getRoleById(1021111184667189288L).get())); // Storywriter

        this.messageHandler = new MessageHandler(api);
        this.slashCommandHandler = new SlashCommandHandler(api);
        this.welcomeChannel = api.getServerChannelById(1021482228708020275L).get();
        registerCommands();
    }

    private void registerCommands() {
        slashCommandHandler.registerSlashCommands(CommandFinder.FindCommands(), DefaultServer);
    }

    public DiscordApi getApi() {
        return api;
    }

    public Sections getSections() {
        return sections;
    }

    public static Bot get() {
        return bot;
    }

    public SlashCommandHandler getSlashCommandHandler() {
        return slashCommandHandler;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public ServerChannel getWelcomeChannel() {
        return welcomeChannel;
    }
}
