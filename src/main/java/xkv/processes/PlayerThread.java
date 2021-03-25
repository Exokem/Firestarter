package xkv.processes;

import javafx.application.Platform;
import xkv.content.Track;
import xkv.visual.panels.audion.AudionPlayer;

import javax.sound.sampled.*;
import java.io.IOException;

public class PlayerThread extends Thread
{
    private final Clip audioClip;

    private Track track;
    private AudioInputStream audioStream;

    private boolean started = false;

    public PlayerThread()
    {
        super(() -> Platform.runLater(AudionPlayer::updateProgress));

        Clip clip;

        try
        {
            clip = AudioSystem.getClip();
        }
        catch (LineUnavailableException e)
        {
            clip = null;

            e.printStackTrace();
        }

        audioClip = clip;
    }

    public void placeTrack(Track track)
    {
        try
        {
            if (audioClip.isOpen()) audioClip.close();

            audioStream = AudioSystem.getAudioInputStream(track.data());
            audioClip.open(audioStream);
        } catch (UnsupportedAudioFileException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (LineUnavailableException e)
        {
            e.printStackTrace();
        }
        this.track = track;
    }

    @Override
    public synchronized void start()
    {
        super.start();
        started = true;
    }

    public boolean started()
    {
        return started;
    }

    private int framePosition = 0;

    public boolean hasTrack()
    {
        return track != null;
    }

    public boolean active()
    {
        return audioClip.isActive();
    }

    public void activate()
    {
        audioClip.setFramePosition(framePosition);
        audioClip.start();
    }

    public void deactivate()
    {
        framePosition = audioClip.getFramePosition();
        audioClip.stop();
    }

    public void reset()
    {
        audioClip.setFramePosition(0);
        deactivate();
        audioClip.stop();
        audioClip.close();
    }

    public double progress()
    {
        return (double) audioClip.getFramePosition() / (double) audioClip.getFrameLength();
    }

    @Override
    public void run()
    {
        super.run();
    }
}
