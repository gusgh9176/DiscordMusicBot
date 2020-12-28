package com.example;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.util.CalcuTime;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;

public class AudioResultHandler implements AudioLoadResultHandler {

    MessageAuthor messageAuthor;
    TextChannel textChannel;
    TrackScheduler trackScheduler;
    EmbedBuilder embed;

    public AudioResultHandler(MessageAuthor messageAuthor, TextChannel textChannel, TrackScheduler trackScheduler){
        this.messageAuthor = messageAuthor;
        this.textChannel = textChannel;
        this.trackScheduler = trackScheduler;
        // Set embed 1
        embed = new EmbedBuilder()
                .setTitle("Play Music")
                .setDescription("Music List")
                .setAuthor(messageAuthor);
    }

    // Search Result = 1
    @Override
    public void trackLoaded(AudioTrack track) {
        trackScheduler.queue(track);
    }

    // Search Result > 1, max = 19
    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        // Set embed 2
        embed.setColor(Color.RED);

        for (AudioTrack track : playlist.getTracks()) {
            // Get track information
            AudioTrackInfo audioTrackInfo = track.getInfo();
            // title
            // [hh:mm:ss] + https ~
            embed.addField(audioTrackInfo.title, CalcuTime.se2Time(audioTrackInfo.length) + audioTrackInfo.uri, false);
        }
        // add queue if queue == 0, play music if queue > 0
        trackScheduler.queue(playlist.getTracks().get(0));

        textChannel.sendMessage(embed);
    }

    @Override
    public void noMatches() {
        // Notify the user that we've got nothing
        textChannel.sendMessage("해당 노래를 찾을 수 없습니다.");
    }

    @Override
    public void loadFailed(FriendlyException throwable) {
        // Notify the user that everything exploded
        textChannel.sendMessage("해당 작업을 실패하였습니다.");
    }
}
