package de.overcraft;


import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import java.io.*;

public class EntryPoint {
    public static void main(String[] args) {
        File file = new File("../config/token.txt");
        String token;
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            token = fileReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(token.isBlank() || token.isEmpty())
            throw new RuntimeException("no token found");
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        new Bot(api);
    }

}
