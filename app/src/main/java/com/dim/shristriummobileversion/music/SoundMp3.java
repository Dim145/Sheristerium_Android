package com.dim.shristriummobileversion.music;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import com.dim.shristriummobileversion.MainActivity;

import java.io.*;

// MP3, WMA, MPEG, WAV compatible

public class SoundMp3 implements Runnable
{
    private String path;

    private boolean        isPlaying = false;
    private MediaPlayer    player;
    private MainActivity ctrl;

    public SoundMp3(String path, boolean reLaunch, MainActivity ctrl)
    {
        this.path = path;
        this.ctrl = ctrl;

        this.player = MediaPlayer.create( ctrl, this.getRawResIdByName(path));

        try
        {
            this.player.setLooping(reLaunch);

            //this.player.prepare();
        }
        catch (Exception e )
        {
            e.printStackTrace();
        }
    }

    public void play()
    {
        if (player != null)
        {
            isPlaying = true;
            player.start();
        }
    }

    public void stop()
    {
        if (player != null)
        {
            isPlaying = false;
            player.stop();
        }
    }

    public void pause()
    {
        if (player != null)
        {
            isPlaying = false;
            player.pause();
        }
    }

    public boolean isPlaying()
    {
        return isPlaying;
    }

    @Override
    public void run()
    {
        try
        {
            this.play();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private int getRawResIdByName(String resName)
    {
        String pkgName = this.ctrl.getPackageName();

        // Return 0 if not found.
        int resID = this.ctrl.getResources().getIdentifier(resName, "raw", pkgName);

        return resID;
    }
}
