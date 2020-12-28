package com.listner;

import com.example.AudioResultHandler;
import com.example.LavaplayerAudioSource;
import com.example.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.util.CalcuTime;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class MyListener implements MessageCreateListener {

    final AudioPlayerManager playerManager;
    final AudioPlayer player;
    final TrackScheduler trackScheduler;

    public MyListener() {
        // Create a player manager
        playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        player = playerManager.createPlayer();

        // Add listener trackScheduler in player
        trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);

        player.setVolume(15); // Set Volume
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        // Split by space
        String[] content = event.getMessageContent().split(" "); // 0 = cmd, 1~ = option

        // MessageAuthor, TextChannel
        MessageAuthor messageAuthor = event.getMessageAuthor();
        TextChannel textChannel = event.getChannel();

        if (content[0].equalsIgnoreCase("!music")) {
            Server server = event.getServer().get();
            Long id = event.getMessageAuthor().getId();
            ServerVoiceChannel channel = server.getConnectedVoiceChannel(id).get(); // Get sender voice channel

            String tempMusicName = "";

            // Create music Name
            for (int i = 1; i < content.length; i++) {
                tempMusicName += content[i] + " ";
            }
            final String musicName = tempMusicName;

            /**
             * Don't join channel Bot
             * Join channel Bot
             */
            if (channel.isConnected(server.getApi().getClientId())) {
                // Already connected channel bot
                playerManager.loadItem("ytsearch: " + musicName, new AudioResultHandler(messageAuthor, textChannel, trackScheduler));
            }
            else {
                channel.connect().thenAccept(audioConnection -> {

                    // Do stuff
                    // Create an audio source and add it to the audio connection's queue
                    AudioSource source = new LavaplayerAudioSource(server.getApi(), player);
                    audioConnection.setAudioSource(source);

                    // You can now use the AudioPlayer like you would normally do with Lavaplayer, e.g.,
                    playerManager.loadItem("ytsearch: " + musicName, new AudioResultHandler(messageAuthor, textChannel, trackScheduler));
                }).exceptionally(e -> {
                    // Failed to connect to voice channel (no permission?)
                    e.printStackTrace();
                    return null;
                });

//                System.out.println("말한 사람이 들어가 있는 음성 채널" + channel);
            }
        }
        // Skip now playing music and play next music
        else if (content[0].equalsIgnoreCase("!skip")) {
            String nextTrackTitle = trackScheduler.getNextTrackTitle();
            // Set embed
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Skip Music")
                    .setAuthor(messageAuthor);
            if (nextTrackTitle.isEmpty()) {
                embed.setDescription("다음 곡이 없습니다. \n 플레이어가 종료됩니다.");
            } else {
                embed.setDescription("Next Music: " + trackScheduler.getNextTrackTitle());
            }
            textChannel.sendMessage(embed);
            trackScheduler.nextTrack();
        }

        // print now play music
        else if (content[0].equalsIgnoreCase("!now")) {
            AudioTrackInfo audioTrackInfo = player.getPlayingTrack().getInfo();
            // Set embed
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Now Playing...")
                    .setDescription(audioTrackInfo.title + " " +CalcuTime.se2Time(audioTrackInfo.length));
            textChannel.sendMessage(embed);
        }

    }
}
