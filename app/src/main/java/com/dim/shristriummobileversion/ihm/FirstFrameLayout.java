package com.dim.shristriummobileversion.ihm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class FirstFrameLayout extends LinearLayout implements Runnable, View.OnClickListener
{
    private AppCompatActivity ctrl;
    private ViewAffichage     viewAffichage;

    private Button btn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FirstFrameLayout (Context context)
    {
        super(context);

        this.ctrl          = (AppCompatActivity) context;
        this.viewAffichage = new ViewAffichage(this.ctrl, new int[]{} );

        this.ctrl.setContentView(this);

        this.btn = new Button(this.ctrl);

        this.btn.setText("Test");
        this.btn.setOnClickListener(this);

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Ajout du composant au layout.
        this.addView(this.btn,	layoutParam);
        this.addView( this.viewAffichage );

        //this.draw(this.canvas);
    }

    @Override
    public void run ()
    {
        while(true)
        {
            if( System.currentTimeMillis() % 1000 == 0 )
                this.viewAffichage.invalidate();
        }
    }

    public void maj()
    {
        this.invalidate();
        this.viewAffichage.invalidate();
    }

    @Override
    public void onClick (View v)
    {
        this.removeView(this.btn);
    }
}
