package com.listner;

import com.search.AudioResultHandler;
import org.apache.log4j.Logger;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class ChoiceMusicListener implements MessageCreateListener {

    private static final Logger logger = Logger.getLogger(FirstMessageListener.class);

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
                String inputNum = event.getMessageContent();
                int num = Integer.parseInt(inputNum) - 1;

                logger.info("User enter Num : " + inputNum);
                logger.info("Parse User enter Num - 1 : " + num);

                audioResultHandler.offerQueue(num);

                logger.info("Add music Successful");

            }catch (NumberFormatException nfe){
                logger.error(nfe);
                logger.error("User enter number error");
            }finally {
                // delete listener myself
                event.getApi().removeListener(this);
            }
        }


    }
}
