package com.dim.shristriummobileversion.ihm;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.dim.shristriummobileversion.MainActivity;
import com.dim.shristriummobileversion.metier.balle.Balle;
import com.dim.shristriummobileversion.R;

public class FirstFrameTest extends RelativeLayout implements Runnable
{
    public static final int  TAILLE_BALLE = 10;
    public int  WIDTH ;
    public int  HEIGTH;

    private MainActivity  ctrl;
    private LinearLayout  layout;
    private Paint         paint;
    private ViewAffichage view;

    private boolean canRun;

    private boolean refresh;
    private int deplacement;

    private Button bRetry;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public FirstFrameTest(MainActivity ctrl, int[] tabDimension )
    {
        super( ctrl );

        this.WIDTH       = tabDimension[0];
        this.HEIGTH      = tabDimension[1];
        this.deplacement = 0;

        this.view = new ViewAffichage(ctrl, tabDimension);

        this.addView(this.view);
        this.addView(this.view.bRetry);
        this.addView(this.view.bPause);
        this.addView(this.view.bLaunch);
        this.addView(this.view.bMusic);
        this.addView(this.view.bParam);
        this.addView(this.view.bCloseParam);

        this.addView(this.view.controlGroup);

        this.setOnTouchListener(this.view);

        this.ctrl   = ctrl;
        this.paint  = new Paint();
        this.layout = this.ctrl.findViewById(R.id.layout);

        this.paint.setColor(Color.BLACK);
        this.paint.setTextSize( 20 );

        this.canRun = false;

        this.ctrl.setContentView(this);

        this.bRetry = new Button(this.ctrl);

        this.bRetry.setText("Retry");
        this.bRetry.setVisibility(View.INVISIBLE);
        this.bRetry.setX(this.getWidth()/2-50);
        this.bRetry.setY(this.getHeight()/2+20);
    }

    public void run()
    {
        this.afficher(true);
        this.invalidate();
        this.view.invalidate();
    }

    public void afficher( boolean on )
    {
        this.canRun = on;
    }

    public void maj()
    {
        this.refresh = true;

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.P )
        {
            this.invalidate();
            this.view.invalidate();
        }
        else
        {
            this.ctrl.runOnUiThread(this);
        }

    }

    public void reset()
    {
        this.deplacement = 0;
        this.canRun = true;

        this.view.reset();
    }

    public int getLargueur ()
    {
        return this.view.getWidth();
    }

    public int getHauteur ()
    {
        return this.view.getHeight();
    }
}
