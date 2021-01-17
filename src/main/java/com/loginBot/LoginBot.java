package com.loginBot;

import com.listner.FirstMessageListener;

import org.apache.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.util.concurrent.CompletionException;


public class LoginBot {
    private static final Logger logger = Logger.getLogger(LoginBot.class);


    public static void main(String[] args) {

        logger.info("Application Start");
        // Log the bot
        DiscordApi api;
        try {
            api = new DiscordApiBuilder()
                    .addServerBecomesAvailableListener(event -> {
                        System.out.println("Loaded " + event.getServer().getName());
                    })
                    .addListener(new FirstMessageListener())
                    .setToken("token")
                    .login()
                    .join();
            logger.info("Bot Login Successful");
            // Cache a maximum of 10 messages per channel for and remove messages older than 1 hour
            api.setMessageCacheSize(10, 60 * 60);
            // Invite bot url
            System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());

        } catch (CompletionException ce) {
            logger.error(ce);
            logger.error("Bot Login Failed");
            logger.error("Token or api Connect error");
        }


    }
}
