package com.listner;

import com.example.AudioResultHandler;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class ChoiceMusicListener implements MessageCreateListener {
    private AudioResultHandler audioResultHandler;
    private Long userId;

    public ChoiceMusicListener(Long userId, AudioResultHandler audioResultHandler){
        this.userId = userId;
        this.audioResultHandler = audioResultHandler;
    }
    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        // !music 호출 유저가 숫자를 말한다면
        if(event.getMessageAuthor().getId() == userId){
            try {
                int num = Integer.parseInt(event.getMessageContent()) - 1;
                audioResultHandler.offerQueue(num);
            }catch (NumberFormatException nfe){
                nfe.printStackTrace();
            }finally {
                // delete listener myself
                event.getApi().removeListener(this);
            }
        }


    }
}
