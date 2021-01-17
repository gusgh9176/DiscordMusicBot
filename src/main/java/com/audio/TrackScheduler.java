package com.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.javacord.api.audio.AudioConnection;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final AudioConnection audioConnection;
    private final BlockingQueue<AudioTrack> queue;

    /**
     * @param player The audio player this scheduler uses
     * @param audioConnection The audioConnection this scheduler uses connection channel
     */
    public TrackScheduler(AudioPlayer player, AudioConnection audioConnection) {
        this.player = player;
        this.audioConnection = audioConnection;
        this.queue = new LinkedBlockingQueue<>();
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        if(queue.peek() == null){
            exitVoiceChannel();
        }
        else{
            player.startTrack(queue.poll(), false);
        }
    }

    /**
     * clear player, queue
     * exit audio channel
     */
    public void exitVoiceChannel(){
        player.destroy();
        queue.clear();
        audioConnection.close();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        // LOAD_FAILED -> exit Voice Channel
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }

    /**
     * return next playing Track Title
     * @return nextTitle or ""
     */
    public String getNextTrackTitle(){
        AudioTrack audioTrack = queue.peek();
        String nextTitle = "";
        if(audioTrack != null){
            nextTitle = audioTrack.getInfo().title;
        }
        return nextTitle;
    }


}