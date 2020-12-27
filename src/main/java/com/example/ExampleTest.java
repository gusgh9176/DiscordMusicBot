package com.example;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.util.HashMap;
import java.util.Map;

public class ExampleTest {
    public static void main(String[] args) {
        // Log the bot
        DiscordApi api = new DiscordApiBuilder()
                .addServerBecomesAvailableListener(event ->{
                    System.out.println("Loaded " + event.getServer().getName());
                })
                .addListener(new MyListener())
                .setToken("NzkyNjI0ODg2ODcwODM1MjMw.X-gbcw.0qQLydTwc6XrNH1Dbfkw3BAmfMA")
                .login()
                .join();

        // Cache a maximum of 10 messages per channel for and remove messages older than 1 hour
        api.setMessageCacheSize(10, 60*60);

        // Invite bot url
        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());

//        System.out.println("메인 API " + api);
    }
}
