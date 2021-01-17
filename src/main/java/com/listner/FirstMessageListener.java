package com.listner;

import com.search.AudioResultHandler;
import com.audio.LavaplayerAudioSource;
import com.audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.util.ParseTime;
import org.apache.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.awt.*;
import java.util.NoSuchElementException;

public class FirstMessageListener implements MessageCreateListener {

    private static final Logger logger = Logger.getLogger(FirstMessageListener.class);

    final private AudioPlayerManager playerManager;
    final private AudioPlayer player;
    private TrackScheduler trackScheduler;
    private AudioResultHandler audioResultHandler;

    public FirstMessageListener() {
        // Create a player manager
        playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        player = playerManager.createPlayer();

        player.setVolume(15); // Set Default Volume
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        // Split by space
        String[] content = event.getMessageContent().split(" "); // 0 = cmd, 1~ = option

        // MessageAuthor, TextChannel
        MessageAuthor messageAuthor = event.getMessageAuthor();
        TextChannel textChannel = event.getChannel();

        if (content[0].equalsIgnoreCase("!music")) {
            try {
                logger.info("User enter the !music");
                Server server = event.getServer().get();
                DiscordApi api = server.getApi();
                Long userId = event.getMessageAuthor().getId();
                ServerVoiceChannel channel = server.getConnectedVoiceChannel(userId).get(); // Get sender voice channel

                StringBuilder tempMusicName = new StringBuilder();

                // Create music Name
                for (int i = 1; i < content.length; i++) {
                    tempMusicName.append(content[i]).append(" ");
                }
                final String musicName = tempMusicName.toString();

                /**
                 * Don't join channel Bot
                 * Join channel Bot
                 */
                if (channel.isConnected(api.getClientId())) {
                    logger.info("Already connected channel bot");
                    // Already connected channel bot
                    playerManager.loadItem("ytsearch: " + musicName, audioResultHandler);

                    // add music choice listener
                    api.addListener(new ChoiceMusicListener(userId, audioResultHandler));
                    logger.info("Music List show Successful");
                } else {
                    channel.connect()
                            .thenAccept(audioConnection -> {
                                // Do stuff
                                logger.info("Connect channel bot");
                                // Add listener trackScheduler in player
                                trackScheduler = new TrackScheduler(player, audioConnection);
                                player.addListener(trackScheduler);

                                // Create an audio source and add it to the audio connection's queue
                                AudioSource source = new LavaplayerAudioSource(api, player);
                                audioConnection.setAudioSource(source);

                                // You can now use the AudioPlayer like you would normally do with Lavaplayer, e.g.,
                                audioResultHandler = new AudioResultHandler(messageAuthor, textChannel, trackScheduler);
                                playerManager.loadItem("ytsearch: " + musicName, audioResultHandler);

                                // add music choice listener
                                api.addListener(new ChoiceMusicListener(userId, audioResultHandler));

                                logger.info("Music List show Successful");
                            })
                            .exceptionally(e -> {
                                // Failed to connect to voice channel (no permission?)
                                logger.error(e);
                                return null;
                            });

                }
            }catch (NoSuchElementException nsee){
                logger.error(nsee);
                logger.error("User is not in VoiceChannel");
            }
        }

        // Skip now playing music and play next music
        else if (content[0].equalsIgnoreCase("!skip")) {
            try {
                logger.info("User enter the !skip");
                String nextTrackTitle = trackScheduler.getNextTrackTitle();
                // Set embed
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Skip Music")
                        .setAuthor(messageAuthor)
                        .setColor(Color.BLUE);
                if (nextTrackTitle.isEmpty()) {
                    embed.setDescription("다음 곡이 없습니다. \n 플레이어가 종료됩니다.");
                    trackScheduler.exitVoiceChannel();
                } else {
                    embed.setDescription("Next Music: " + trackScheduler.getNextTrackTitle());
                    trackScheduler.nextTrack();
                }
                textChannel.sendMessage(embed);
            } catch (NullPointerException npe) {
                logger.error(npe);
                logger.error("music skip error");
            }
        }

        // print now play music
        else if (content[0].equalsIgnoreCase("!now")) {
            logger.info("User enter the !now");
            AudioTrackInfo audioTrackInfo = player.getPlayingTrack().getInfo();
            // Set embed
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Now Playing...")
                    .setDescription(audioTrackInfo.title + " " + ParseTime.se2Time(audioTrackInfo.length));
            textChannel.sendMessage(embed);
        }

        else if (content[0].equalsIgnoreCase("!volume")) {
            logger.info("User enter the !volume");
            // Set embed
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Volume");
            // content.length > 1 -> set volume num
            // max volume = 50
            try {
                if (content.length > 1) {
                    int volume = Integer.parseInt(content[1]);
                    if (volume > 50) {
                        volume = 50;
                    }
                    player.setVolume(volume);
                    embed.setDescription("Volume이 " + player.getVolume() + "으로 변경되었습니다. (기본 값 15)");
                }
                // content.length == 1 -> get volume
                else {
                    embed.setDescription("현재 Volume: " + player.getVolume() + " (기본 값 15)");
                }
                textChannel.sendMessage(embed);
            }catch (NumberFormatException nfe){
                logger.error(nfe);
                logger.error("set volume error");
            }
        }

        // disconnect audio channel
        else if (content[0].equalsIgnoreCase("!dis")) {
            logger.info("User enter the !dis");
            try {
                trackScheduler.exitVoiceChannel();
            } catch (NullPointerException npe) {
                logger.error(npe);
                logger.error("disconnect error");
            }
        }
    }
}
