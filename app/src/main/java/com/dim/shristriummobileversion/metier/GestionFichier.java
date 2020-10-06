package com.dim.shristriummobileversion.metier;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class GestionFichier extends Activity
{
    private SharedPreferences prefs;

    public GestionFichier( SharedPreferences prefs )
    {
        this.prefs = prefs;
    }

    public void enregistrerScore(int level )
    {
        if( this.lireScore() > level ) return;

        SharedPreferences.Editor editor     = this.prefs.edit();
        editor.putInt("highScore", level);

        editor.commit();
    }

    public int lireScore()
    {
        return this.prefs.getInt("highScore", 0);
    }

    public int lireMode()
    {
        return this.prefs.getInt("Mode", 0); // 0 = TouchMode
    }

    public void enregistrerMode( int mode )
    {
        SharedPreferences.Editor editor     = this.prefs.edit();
        editor.putInt("Mode", mode);

        editor.commit();
    }

    public int lireTheme()
    {
        return this.prefs.getInt("Theme", 0);
    }

    public void enregistrerTheme( int theme )
    {
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putInt("Theme", theme);

        editor.commit();
    }
}
