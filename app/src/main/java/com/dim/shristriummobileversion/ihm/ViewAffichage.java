package com.dim.shristriummobileversion.ihm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.dim.shristriummobileversion.MainActivity;
import com.dim.shristriummobileversion.R;
import com.dim.shristriummobileversion.metier.GestionFichier;
import com.dim.shristriummobileversion.metier.balle.Balle;

import java.io.Console;

public class ViewAffichage extends View implements View.OnClickListener, View.OnTouchListener, SensorEventListener
{
    public static final int  TAILLE_BALLE = 10;
    public int  WIDTH ;
    public int  HEIGTH;

    private MainActivity ctrl;
    private Paint        paint;

    private int timeInSec;

    private int deplacement;

    protected Button bRetry;
    protected Button bPause;
    protected Button bLaunch;
    protected Button bMusic;
    protected Button bParam;
    protected Button bCloseParam;

    private   RadioButton controlTouch;
    private   RadioButton controlFollow;
    private   RadioButton controlMove;
    protected RadioGroup  controlGroup;

    private RadioGroup  groupTheme;
    private RadioButton themeDark;
    private RadioButton themeLight;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public ViewAffichage (Context context, int[] tabDimension)
    {
        super(context);

        this.WIDTH       = tabDimension[0];
        this.HEIGTH      = tabDimension[1];

        this.ctrl = (MainActivity) context;

        this.paint  = new Paint();

        this.paint.setColor(Color.BLACK);
        this.paint.setTextSize( this.convertToPixelFromDp(10) );
        this.paint.setAntiAlias(true);

        this.bRetry      = new Button(this.ctrl);
        this.bPause      = new Button(this.ctrl);
        this.bLaunch     = new Button(this.ctrl);
        this.bMusic      = new Button(this.ctrl);
        this.bParam      = new Button(this.ctrl);
        this.bCloseParam = new Button(this.ctrl);

        this.controlFollow = new RadioButton(this.ctrl);
        this.controlMove   = new RadioButton(this.ctrl);
        this.controlTouch  = new RadioButton(this.ctrl);
        this.themeDark     = new RadioButton(this.ctrl);
        this.themeLight    = new RadioButton(this.ctrl);

        this.groupTheme = new RadioGroup(this.ctrl);
        this.groupTheme.addView(this.themeDark);
        this.groupTheme.addView(this.themeLight);

        TextView textTmpControle = new TextView(this.ctrl);
        textTmpControle.setText("Contrôles: ");

        TextView textTmpTheme = new TextView(this.ctrl);
        textTmpTheme.setText("Thèmes: ");

        this.controlGroup = new RadioGroup(this.ctrl);
        this.controlGroup.addView( textTmpControle );
        //this.controlGroup.addView( this.controlFollow);
        this.controlGroup.addView(this.controlMove);
        this.controlGroup.addView(this.controlTouch);
        this.controlGroup.addView(textTmpTheme);
        this.controlGroup.addView(this.groupTheme);
        this.controlGroup.setY(this.HEIGTH/2-this.convertToPixelFromDp(125));
        this.controlGroup.setX(this.WIDTH /2-this.convertToPixelFromDp(100));
        this.controlGroup.setVisibility(View.INVISIBLE);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = (int) this.convertToPixelFromDp(25);
        params.width  = (int) this.convertToPixelFromDp(25);

        this.controlMove.setText  ("Pencher l'appareil");
        this.controlMove.setOnClickListener(this);
        this.controlFollow.setText("Suivre le doigts");
        this.controlFollow.setOnClickListener(this);
        this.controlTouch.setText ("Toucher les bords de l'écran");
        this.controlTouch.setOnClickListener(this);

        this.themeDark.setText("Thème sombre");
        this.themeDark.setOnClickListener(this);
        this.themeLight.setText("Thème clair");
        this.themeLight.setOnClickListener(this);

        int mode = new GestionFichier(this.ctrl.getPreferences(Context.MODE_PRIVATE) ).lireMode();
        if( mode == 0 )
            this.controlTouch.setChecked(true);
        else
            this.controlMove.setChecked(true);

        if( new GestionFichier(this.ctrl.getPreferences(Context.MODE_PRIVATE)).lireTheme() == 1 )
        {
            this.themeDark.setChecked(true);
            this.ctrl.setBackgroundColor(Color.GRAY);
            this.setBackgroundColor(Color.GRAY);
        }
        else
        {
            this.themeLight.setChecked(true);
        }

        this.bRetry.setText("Retry");
        this.bRetry.setVisibility(View.INVISIBLE);
        this.bRetry.setOnClickListener(this);
        this.bRetry.setX(this.WIDTH/2 -this.convertToPixelFromDp(40));
        this.bRetry.setY(this.HEIGTH/2+this.convertToPixelFromDp(5));

        this.bPause.setOnClickListener(this);
        this.bPause.setBackground(  ContextCompat.getDrawable(this.ctrl, R.drawable.pause) );
        this.bPause.setLayoutParams(params);
        this.bPause.setY(10);
        this.bPause.setX(this.WIDTH/2-this.convertToPixelFromDp(35));
        this.bPause.setVisibility(View.INVISIBLE);

        this.bMusic.setOnClickListener(this);
        this.bMusic.setBackground(ContextCompat.getDrawable(this.ctrl, R.drawable.muteon));
        this.bMusic.setLayoutParams(params);
        this.bMusic.setY( 10 );
        this.bMusic.setX( this.WIDTH/2-this.convertToPixelFromDp(10));

        this.bLaunch.setOnClickListener(this);
        this.bLaunch.setBackground(ContextCompat.getDrawable(this.ctrl, R.drawable.play));
        this.bLaunch.setX(this.WIDTH/2-this.convertToPixelFromDp(15));
        this.bLaunch.setY(this.HEIGTH/2-this.convertToPixelFromDp(25));
        this.bLaunch.setLayoutParams(params);

        this.bParam.setOnClickListener(this);
        this.bParam.setBackground(ContextCompat.getDrawable(this.ctrl, R.drawable.parametres));
        this.bParam.setLayoutParams(params);
        this.bParam.setY(10); //32
        this.bParam.setX(this.WIDTH/2+this.convertToPixelFromDp(15)); //35

        this.bCloseParam.setOnClickListener(this);
        this.bCloseParam.setText("Fermer");
        this.bCloseParam.setX(this.WIDTH/2-this.convertToPixelFromDp(50));
        this.bCloseParam.setY( this.HEIGTH/2+this.convertToPixelFromDp(50) );

        this.bCloseParam.setVisibility(View.INVISIBLE);


        SensorManager sensorManager = (SensorManager) this.ctrl.getSystemService(Context.SENSOR_SERVICE);

        Sensor accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //System.out.println("\nListe capteurs:");
        //for( Sensor tmp : sensorManager.getSensorList(Sensor.TYPE_ALL) )
        //    System.out.println(tmp.getName());
        //System.out.println();

        sensorManager.registerListener( this, accelerometre, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onDraw( Canvas g )
    {
        if( this.deplacement != 0 && !this.ctrl.isbPause() )
            if( this.deplacement < 0 )
                this.ctrl.evoluerBallePerso( (1.3*60)/1000, "q", '1' );
            else if( this.deplacement > 0 )
                this.ctrl.evoluerBallePerso( (1.3*60)/1000, "d", '1' );

        g.drawLine(0, this.getHeight()-(62-TAILLE_BALLE), this.WIDTH+TAILLE_BALLE, this.getHeight()-(62-TAILLE_BALLE), this.paint);

        float tailleBalleJ1 = (float) this.ctrl.getTailleBalle('1');
        float tailleBalleJ2 = (float) this.ctrl.getTailleBalle('2');

        // on inverse axe Y
        float balleX = (float) this.ctrl.getBallePersoCoordonnees('x', '1');
        float balleY = (float) (this.getHeight() - (52+tailleBalleJ1) - this.ctrl.getBallePersoCoordonnees('y', '1'));

        Balle[] tabBalleCoul = this.ctrl.getTabBalle();

        this.paint.setColor( Color.BLACK );

        // on affiche la balle J1
        g.drawCircle( balleX, balleY, tailleBalleJ1, this.paint );
        g.drawText("J1", (float) balleX, (float) (balleY-tailleBalleJ1), this.paint);

        for ( int cpt = 0; cpt < tabBalleCoul.length; cpt++ )
        {
            if ( tabBalleCoul[cpt] == null ) continue;

            float balleCoulx = (float) tabBalleCoul[cpt].x;
            float balleCouly = (float) tabBalleCoul[cpt].y;

            int coulTemp = Color.CYAN;

            switch( tabBalleCoul[cpt].getCoul() )
            {
                case 'b': coulTemp = Color.BLUE      ;break;
                case 'v': coulTemp = Color.GREEN     ;break;
                case 'o': coulTemp = Color.rgb(255,165,0)    ;break;
                case 'r': coulTemp = Color.RED       ;break;
                case 'n': coulTemp = Color.BLACK     ;break;
                case 'w': coulTemp = Color.WHITE     ;break;
                case 'm': coulTemp = Color.MAGENTA   ;break;
                case 'g': coulTemp = Color.rgb(211,211,211) ;break;
            }

            this.paint.setColor( coulTemp );
            g.drawCircle( balleCoulx, balleCouly, TAILLE_BALLE, this.paint );
        }

        if ( this.ctrl.getFinish() )
        {
            String sJTemp = this.ctrl.getJoueurGagne() == 1 ? "joueur 1" : "joueur 2";

            this.paint.setColor(Color.RED);
            this.paint.setTextSize(this.convertToPixelFromDp(25));
            g.drawText("Game Over", this.getWidth()/2-this.convertToPixelFromDp(55), this.getHeight()/2, this.paint );
            this.paint.setTextSize( this.convertToPixelFromDp(10) );

            this.bRetry.setVisibility(View.VISIBLE);
        }

        if( !this.ctrl.getFinish() )
            this.timeInSec = (int) (this.getLunchTime()/1000000000);

        this.paint.setColor(Color.BLACK);

        if( this.timeInSec > 60 )
            g.drawText("temps: " + timeInSec/60 + ":" + (this.timeInSec-( 60 * (timeInSec/60))) + " (s)", 10, this.convertToPixelFromDp(20), this.paint);
        else
            g.drawText("temps: " + timeInSec + " (s)",10,this.convertToPixelFromDp(20), this.paint);

        g.drawText("Niveau: "   + this.ctrl.getNiveaux()  ,       10, this.convertToPixelFromDp(10), this.paint);
        g.drawText("Meilleur score: " + this.ctrl.getHighScore(), this.getWidth()-this.convertToPixelFromDp(85), this.convertToPixelFromDp(10), this.paint);

        if( this.controlMove.isChecked() )
            g.drawText("Inclinez l'appareil", this.convertToPixelFromDp(12), this.getHeight()-15, this.paint);
        else
            g.drawText("touchez les bords de l'écran", this.convertToPixelFromDp(12), this.getHeight()-15, this.paint);

        this.ctrl.majHauteur(this.getHeight());

        //this.invalidate();
    }

    public long getLunchTime()
    {
        return this.ctrl.getLunchTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick (View v)
    {
        if( v == this.bRetry )
            this.ctrl.relancerPartie();

        if( v == this.bLaunch )
        {
            this.bLaunch.setVisibility(View.INVISIBLE);
            this.bPause.setVisibility(View.VISIBLE);
            this.ctrl.runEngine();
        }

        if( v == this.bPause )
        {
            this.ctrl.pausResume();

            if( this.ctrl.isbPause() )
                this.bPause.setBackground(  ContextCompat.getDrawable(this.ctrl, R.drawable.play) );
            else
                this.bPause.setBackground(  ContextCompat.getDrawable(this.ctrl, R.drawable.pause) );
        }

        if( v == this.bMusic )
        {
            this.ctrl.switchMusic();

            if( this.ctrl.getMusicOn() )
                this.bMusic.setBackground(ContextCompat.getDrawable(this.ctrl, R.drawable.muteon));
            else
                this.bMusic.setBackground(ContextCompat.getDrawable(this.ctrl, R.drawable.muteoff));
        }

        if( v == this.controlMove )
            new GestionFichier(this.ctrl.getPreferences(Context.MODE_PRIVATE)).enregistrerMode(1);

        if( v == this.controlTouch )
            new GestionFichier(this.ctrl.getPreferences(Context.MODE_PRIVATE)).enregistrerMode(0);

        if( v == this.bParam )
        {
            if( this.ctrl.isRun() && !this.ctrl.isbPause() )
                this.ctrl.pausResume();

            this.setVisibility(View.INVISIBLE);

            if( this.themeDark.isChecked() )
                this.ctrl.setBackgroundColor(Color.GRAY);

            this.bLaunch.setVisibility(View.INVISIBLE);
            this.bParam.setVisibility(View.INVISIBLE);
            this.controlGroup.setVisibility(View.VISIBLE);
            this.bPause.setVisibility(View.INVISIBLE);
            this.bCloseParam.setVisibility(View.VISIBLE);
            this.bRetry.setVisibility(View.INVISIBLE);
        }

        if( v == this.bCloseParam )
        {
            this.bCloseParam.setVisibility(View.INVISIBLE);
            this.bPause.setVisibility(View.VISIBLE);
            this.controlGroup.setVisibility(View.INVISIBLE);
            this.bParam.setVisibility(View.VISIBLE);

            this.setVisibility(View.VISIBLE);

            if( !this.ctrl.isRun() )
                this.bLaunch.setVisibility(View.VISIBLE);

            if( this.ctrl.isRun() )
                this.ctrl.pausResume();
        }

        if( v == this.themeDark )
        {
            this.ctrl.setBackgroundColor( Color.GRAY );
            this.setBackgroundColor(Color.GRAY);
            new GestionFichier(this.ctrl.getPreferences(Context.MODE_PRIVATE)).enregistrerTheme(1);
        }

        if( v == this.themeLight )
        {
            this.ctrl.setBackgroundColor( Color.WHITE );
            this.setBackgroundColor(Color.WHITE);
            new GestionFichier(this.ctrl.getPreferences(Context.MODE_PRIVATE)).enregistrerTheme(0);
        }
    }

    @Override
    public boolean onTouch (View v, MotionEvent event)
    {
        int tmp = event.getAction();

        if( this.ctrl.isRun() && tmp == MotionEvent.ACTION_DOWN && this.controlTouch.isChecked() )
        {
            if ( event.getX() <= this.WIDTH / 2 )
                this.deplacement = -1;
            else if ( event.getX() > this.WIDTH / 2 )
                this.deplacement = 1;
        }
        else
        {
            this.deplacement = 0;
        }

        return false;
    }

    public void reset()
    {
        this.deplacement = 0;

        this.bRetry.setVisibility( View.INVISIBLE );
    }

    @Override
    public void onSensorChanged (SensorEvent event)
    {
        if( this.ctrl.isRun() && !this.ctrl.isbPause() && this.controlMove.isChecked() )
        {
            if( event.values[0] > 0 )
                this.deplacement = -1;
            else if( event.values[0] < 0)
                this.deplacement = 1;
            else
                this.deplacement = 0;
        }
    }

    @Override
    public void onAccuracyChanged (Sensor sensor, int accuracy)
    {

    }

    private float convertToPixelFromDp(float dpInput)
    {
        // obtenir l'échelle de la densité de l'écran
        final float scale = getResources().getDisplayMetrics().density;

        // convertir les dp en pixels, sur la base de l'échelle de densité
        return (dpInput * scale + 0.5f);
    }
}
