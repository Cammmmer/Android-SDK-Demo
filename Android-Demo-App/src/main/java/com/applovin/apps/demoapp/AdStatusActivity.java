package com.applovin.apps.demoapp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public abstract class AdStatusActivity
        extends AppCompatActivity
{
    protected TextView adStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );

        getSupportActionBar().setBackgroundDrawable( new ColorDrawable( 0xDD0A83AA ) );
        getSupportActionBar().show();
    }

    protected void log(final String message)
    {
        if ( adStatusTextView != null )
        {
            runOnUiThread( new Runnable()
            {
                @Override
                public void run()
                {
                    adStatusTextView.setText( message );
                }
            } );
        }
        System.out.println( message );
    }
}
