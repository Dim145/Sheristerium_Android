package com.dim.shristriummobileversion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.dim.shristriummobileversion.ihm.FirstFrameTest;
import com.dim.shristriummobileversion.metier.Moteur;
import com.dim.shristriummobileversion.metier.balle.Balle;
import com.dim.shristriummobileversion.metier.jeu.Jeu;
import com.dim.shristriummobileversion.metier.jeu.JeuBalle;
import com.dim.shristriummobileversion.music.SoundMp3;

public class MainActivity extends AppCompatActivity implements Runnable
{
    private Jeu j;

    private Thread         tIhm;
    private FirstFrameTest ihm;
    private SoundMp3       tSon;

    private Thread tMoteur;

    private boolean bLoseSoundLaunch;
    private boolean bMusicOn;
    private boolean bPause;

    private boolean isRun;

    private DisplayMetrics metrics;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);     // empeche mise en veille
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);                        // son = music (media)

        this.tSon = new SoundMp3("transforyou_couper", true, this);

        this.metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics); // taille de l'ecran

        int[] tabTmp = { metrics.widthPixels, metrics.heightPixels};

        this.ihm  = new FirstFrameTest(this, tabTmp);
        this.j = new JeuBalle( tabTmp, this.getPreferences(Context.MODE_PRIVATE) );

        this.tIhm = new Thread( this.ihm );

        this.tMoteur = new Thread( new Moteur(this.j, this) );

        this.bLoseSoundLaunch = false;
        this.bMusicOn         = true;
        this.bPause           = false;
        this.isRun            = false;

        this.tIhm.start();
    }

    public void pausResume()
    {
        if(!this.bPause)
        {
            Moteur.pauseResume();

            if( this.bMusicOn )
                this.tSon.pause();
        }
        else
        {
            Moteur.pauseResume();

            if( this.bMusicOn && !this.getFinish() )
                this.tSon.play();
        }

        this.bPause = !this.bPause;
    }

    public boolean isbPause()
    {
        return this.bPause;
    }

    public synchronized void runEngine()
    {
        this.tMoteur.start();

        if( this.bMusicOn )
            this.tSon.play();

        this.isRun = true;
    }

    public boolean isRun()
    {
        return this.isRun;
    }

    public synchronized void relancerPartie()
    {
        int[] tabTmp = { metrics.widthPixels, metrics.heightPixels};

        this.j = new JeuBalle( tabTmp, this.getPreferences(Context.MODE_PRIVATE) ); //temp

        if( ((JeuBalle) this.j).getBallePersoCoordonnees('x', '2') != -1 )
            this.initJ2();

        this.tMoteur = new Thread( new Moteur( this.j, this) );
        this.bLoseSoundLaunch = false;
        this.bPause           = false;

        this.ihm.reset();
        this.runEngine();
    }

    public void evoluerBallePerso( double d, String string, char joueur )
    {
        ( (JeuBalle) j ).evoluerBallePerso( d, string, joueur );
    }

    public long getLunchTime()
    {
        return ((JeuBalle) this.j).getLunchTime();
    }

    public int getJoueurGagne()
    {
        return ((JeuBalle) this.j).getJoueurGagne();
    }

    public double getBallePersoCoordonnees( char c, char joueur )
    {
        return ( (JeuBalle) j ).getBallePersoCoordonnees( c, joueur );
    }

    public Balle[] getTabBalle()
    {
        return ( (JeuBalle) j ).getTabBalle();
    }

    public int getNiveaux()
    {
        return this.j.getNiveaux();
    }

    public int getHighScore()
    {
        return ((JeuBalle) j).getHighScore();
    }

    public void maj()
    {
        //this.ihm.run();
        this.ihm.maj();
    }

    public boolean getFinish()
    {
        if( this.j.getFinish() && !this.bLoseSoundLaunch && this.bMusicOn )
        {
            this.tSon.pause();

            System.out.println("game over");

            MediaPlayer temp = MediaPlayer.create(this, this.getRawResIdByName("j_perdu2"));

            temp.setLooping(false);

            temp.start();

            Vibrator vibreur = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE) ;

            if( vibreur != null && vibreur.hasVibrator() )
                vibreur.vibrate(500);

            this.bLoseSoundLaunch = true;
        }

        return this.j.getFinish();
    }

    private int getRawResIdByName(String resName)
    {
        String pkgName = this.getPackageName();

        // Return 0 if not found.
        int resID = this.getResources().getIdentifier(resName, "raw", pkgName);

        return resID;
    }

    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        this.tIhm.start();
    }

    public void initJ2()
    {
        ((JeuBalle) j).initJ2();
    }

    public void switchMusic()
    {
        if( this.bMusicOn )
        {
            this.tSon.pause();
        }
        else if( !this.bPause )
        {
            if( !this.getFinish())
                this.tSon.play();
        }

        this.bMusicOn = !this.bMusicOn;

    }

    public boolean getMusicOn()
    {
        return this.bMusicOn;
    }

    public double getTailleBalle( char c )
    {
        return ((JeuBalle) this.j).getTailleBalle(c);
    }

    @Override
    public void onPause()
    {
        super.onPause();

        try
        {
            if( !this.isbPause() )
                this.pausResume();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        this.bPause = true;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if( this.tSon != null && this.tMoteur != null && this.isbPause() && !this.j.getFinish() )
        {
            if( this.isbPause() )   this.pausResume();
        }
    }

    public void setBackgroundColor( int color )
    {
        if( this.ihm != null )
            this.ihm.setBackgroundColor(color);
    }

    public void majHauteur( int hauteur )
    {
        ((JeuBalle) j).majHauteur(hauteur);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
