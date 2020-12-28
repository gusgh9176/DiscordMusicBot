package com.listner;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class TestListener implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        String[] content = event.getMessageContent().split(" "); // 0 = cmd, 1~ = option
        if (content[0].equalsIgnoreCase("!ping")) {
            event.getChannel().sendMessage("Pong!");
//            System.out.println("말한 사람 ID "+event.getMessageAuthor());
//            System.out.println("겟서버"+event.getServer());
//            System.out.println("겟서버ID"+event.getServer().get().getId());
//            System.out.println("겟채널"+event.getChannel());
//            System.out.println("겟텍스트채널"+event.getServerTextChannel());
//            System.out.println("api " + event.getApi());
        }
    }
}
