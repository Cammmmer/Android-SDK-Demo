
package com.applovin.apps.demoapp.mrecs;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.applovin.adview.AppLovinAdView;
import com.applovin.adview.AppLovinAdViewDisplayErrorCode;
import com.applovin.adview.AppLovinAdViewEventListener;
import com.applovin.apps.demoapp.AdStatusActivity;
import com.applovin.apps.demoapp.R;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdkUtils;

/**
 * Created by monica ong on 7/20/17.
 */

public final class MRecProgrammaticActivity
        extends AdStatusActivity
{
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_mrec_programmatic );

        adStatusTextView = findViewById( R.id.status_label );

        // Create MRec
        final AppLovinAdView adView = new AppLovinAdView( AppLovinAdSize.MREC, this );

        final ViewGroup rootView = findViewById( android.R.id.content );
        rootView.addView( adView );

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams( AppLovinSdkUtils.dpToPx( this, AppLovinAdSize.MREC.getWidth() ),
                                                                              AppLovinSdkUtils.dpToPx( this, AppLovinAdSize.MREC.getHeight() ) );
        layoutParams.topMargin = AppLovinSdkUtils.dpToPx( this, 80 );
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        adView.setLayoutParams( layoutParams );

        // Set up load button
        final Button loadButton = findViewById( R.id.load_button );
        loadButton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                log( "Loading ad..." );
                adView.loadNextAd();
            }
        } );

        //
        // Optional: Set listeners
        //
        adView.setAdLoadListener( new AppLovinAdLoadListener()
        {
            @Override
            public void adReceived(final AppLovinAd ad)
            {
                log( "MRec loaded" );
            }

            @Override
            public void failedToReceiveAd(final int errorCode)
            {
                // Look at AppLovinErrorCodes.java for list of error codes
                log( "MRec failed to load with error code " + errorCode );
            }
        } );

        adView.setAdDisplayListener( new AppLovinAdDisplayListener()
        {
            @Override
            public void adDisplayed(final AppLovinAd ad)
            {
                log( "MRec Displayed" );
            }

            @Override
            public void adHidden(final AppLovinAd ad)
            {
                log( "MRec Hidden" );
            }
        } );

        adView.setAdClickListener( new AppLovinAdClickListener()
        {
            @Override
            public void adClicked(final AppLovinAd ad)
            {
                log( "MRec Clicked" );
            }
        } );

        adView.setAdViewEventListener( new AppLovinAdViewEventListener()
        {
            @Override
            public void adOpenedFullscreen(final AppLovinAd ad, final AppLovinAdView adView)
            {
                log( "MRec opened fullscreen" );
            }

            @Override
            public void adClosedFullscreen(final AppLovinAd ad, final AppLovinAdView adView)
            {
                log( "MRec closed fullscreen" );
            }

            @Override
            public void adLeftApplication(final AppLovinAd ad, final AppLovinAdView adView)
            {
                log( "MRec left application" );
            }

            @Override
            public void adFailedToDisplay(final AppLovinAd ad, final AppLovinAdView adView, final AppLovinAdViewDisplayErrorCode code)
            {
                log( "MRec failed to display with error code " + code );
            }
        } );
    }
}
