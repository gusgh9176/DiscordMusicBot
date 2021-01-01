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

    private MessageAuthor messageAuthor;
    private TextChannel textChannel;
    private TrackScheduler trackScheduler;
    private AudioPlaylist audioPlaylist;

    public AudioResultHandler(MessageAuthor messageAuthor, TextChannel textChannel, TrackScheduler trackScheduler){
        this.messageAuthor = messageAuthor;
        this.textChannel = textChannel;
        this.trackScheduler = trackScheduler;
    }

    public void offerQueue(int num){
        AudioTrack audioTrack = audioPlaylist.getTracks().get(num);
        trackScheduler.queue(audioTrack);
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Play Music")
                .setDescription("노래 정보")
                .setAuthor(this.messageAuthor)
                .addField(audioTrack.getInfo().title, CalcuTime.se2Time(audioTrack.getInfo().length) + audioTrack.getInfo().uri, false);
        textChannel.sendMessage(embed);
    }

    // Search Result = 1
    @Override
    public void trackLoaded(AudioTrack track) {
        trackScheduler.queue(track);
    }

    // Search Result > 1, max = 19
    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        audioPlaylist = playlist;
        // Set embed 2
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Play Music")
                .setDescription("Music List")
                .setAuthor(this.messageAuthor)
                .setColor(Color.RED);

        int i = 1;
        for (AudioTrack track : audioPlaylist.getTracks()) {
            // Get track information
            AudioTrackInfo audioTrackInfo = track.getInfo();
            // title
            // [hh:mm:ss] + https ~
            String num = "[" + i +"번]";
            embed.addField(num + " " + audioTrackInfo.title, CalcuTime.se2Time(audioTrackInfo.length) + audioTrackInfo.uri, false);
            i = i + 1;
        }

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
