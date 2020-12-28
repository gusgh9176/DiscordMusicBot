package com.example;

import com.listner.MyListener;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;


public class ExampleTest {
    public static void main(String[] args) {
        // Log the bot
        DiscordApi api = new DiscordApiBuilder()
                .addServerBecomesAvailableListener(event ->{
                    System.out.println("Loaded " + event.getServer().getName());
                })
                .addListener(new MyListener())
                .setToken("Token")
                .login()
                .join();

        // Cache a maximum of 10 messages per channel for and remove messages older than 1 hour
        api.setMessageCacheSize(10, 60*60);

        // Invite bot url
        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
//        System.out.println("메인 API " + api);
    }
}
