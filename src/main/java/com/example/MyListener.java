package com.example;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class MyListener implements MessageCreateListener {

    @Override
    public void onMessageCreate(MessageCreateEvent event){
        String[] content = event.getMessageContent().split(" "); // 0 = cmd, 1~ = option
        if (content[0].equalsIgnoreCase("!ping")){
            event.getChannel().sendMessage("Pong!");
//            System.out.println("말한 사람 ID "+event.getMessageAuthor());
//            System.out.println("겟서버"+event.getServer());
//            System.out.println("겟서버ID"+event.getServer().get().getId());
//            System.out.println("겟채널"+event.getChannel());
//            System.out.println("겟텍스트채널"+event.getServerTextChannel());
//            System.out.println("api " + event.getApi());
        }
        else if (content[0].equalsIgnoreCase("!music")){
            Server server = event.getServer().get();
            Long id = event.getMessageAuthor().getId();
            ServerVoiceChannel channel = server.getConnectedVoiceChannel(id).get(); // Get sender voice channel
            String tempMusicName = "";
            // Create music Name
            for(int i = 1; i < content.length; i++){
                tempMusicName += content[i] + " ";
            }
            final String musicName = tempMusicName;

            // Connect with voice channel(Bot)
            channel.connect().thenAccept(audioConnection -> {
                // Do stuff
                // Create a player manager
                AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
                playerManager.registerSourceManager(new YoutubeAudioSourceManager());
                AudioPlayer player = playerManager.createPlayer();

                // Create an audio source and add it to the audio connection's queue
                AudioSource source = new LavaplayerAudioSource(server.getApi(), player);
                audioConnection.setAudioSource(source);

                // You can now use the AudioPlayer like you would normally do with Lavaplayer, e.g.,
                playerManager.loadItem("ytsearch: " + musicName, new AudioLoadResultHandler() {
                    @Override
                    public void trackLoaded(AudioTrack track) {
                        System.out.println(1);
                        player.playTrack(track);
                    }

                    @Override
                    public void playlistLoaded(AudioPlaylist playlist) {
                        System.out.println(2);
                        for (AudioTrack track : playlist.getTracks()) {
                            player.playTrack(track);
                        }
                    }

                    @Override
                    public void noMatches() {
                        // Notify the user that we've got nothing
                        System.out.println(3);
                        event.getChannel().sendMessage("해당 노래를 찾을 수 없습니다.");
                    }

                    @Override
                    public void loadFailed(FriendlyException throwable) {
                        // Notify the user that everything exploded
                        System.out.println(4);
                        event.getChannel().sendMessage("해당 작업을 실패하였습니다.");
                    }
                });

                // Search by keword
//                playerManager.loadItem("ytsearch: epic soundtracks", new FunctionalResultHandler(null, audioPlaylist -> {
//                    player.playTrack(audioPlaylist.getTracks().get(0));
//                }, null, null));

            }).exceptionally(e ->{
                // Failed to connect to voice channel (no permission?)
                e.printStackTrace();
                return null;
            });

            System.out.println("말한 사람이 들어가 있는 음성 채널" + channel);
            System.out.println("노래 제목 " + musicName);

        }

    }
}
