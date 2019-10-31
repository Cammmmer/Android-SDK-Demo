package com.applovin.apps.demoapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.applovin.apps.demoapp.banners.BannerDemoMenuActivity;
import com.applovin.apps.demoapp.eventtracking.EventTrackingActivity;
import com.applovin.apps.demoapp.interstitials.InterstitialDemoMenuActivity;
import com.applovin.apps.demoapp.leaders.LeaderDemoMenuActivity;
import com.applovin.apps.demoapp.mrecs.MRecDemoMenuActivity;
import com.applovin.apps.demoapp.nativeads.NativeAdDemoMenuActivity;
import com.applovin.apps.demoapp.rewarded.RewardedVideosDemoMenuActivity;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by thomasso on 10/5/15.
 */
public class MainActivity
        extends DemoMenuActivity
{
    private MenuItem muteToggleMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );

        AppLovinSdk.getInstance( this ).initializeSdk( new AppLovinSdk.SdkInitializationListener()
        {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration config)
            {
                // SDK finished initialization
            }
        } );

        // Set an identifier for the current user. This identifier will be tied to various analytics events and rewarded video validation
        AppLovinSdk.getInstance( this ).setUserIdentifier( "support@applovin.com" );

        // Check that SDK key is present in Android Manifest
        checkSdkKey();
    }

    @Override
    protected DemoMenuItem[] getListViewContents()
    {
        DemoMenuItem[] result = {
                new DemoMenuItem( "Interstitials", "Full screen ads. Graphic or video", new Intent( this, InterstitialDemoMenuActivity.class ) ),
                new DemoMenuItem( "Rewarded Videos (Incentivized Ads)", "Reward your users for watching these on-demand videos", new Intent( this, RewardedVideosDemoMenuActivity.class ) ),
                new DemoMenuItem( "Native Ads", "In-content ads that blend in seamlessly", new Intent( this, NativeAdDemoMenuActivity.class ) ),
                new DemoMenuItem( "Banners", "320x50 Classic banner ads", new Intent( this, BannerDemoMenuActivity.class ) ),
                new DemoMenuItem( "MRecs", "300x250 Rectangular ads typically used in-content", new Intent( this, MRecDemoMenuActivity.class ) ),
                new DemoMenuItem( "Event Tracking", "Track in-app events for your users", new Intent( this, EventTrackingActivity.class ) ),
                new DemoMenuItem( "Resources", "https://support.applovin.com/support/home", new Intent( Intent.ACTION_VIEW, Uri.parse( "https://support.applovin.com/support/home" ) ) ),
                new DemoMenuItem( "Contact", "support@applovin.com", getContactIntent() )
        };
        boolean isTablet = getResources().getBoolean( R.bool.is_tablet );
        if ( isTablet )
        {
            ArrayList<DemoMenuItem> menuItems = new ArrayList<>( result.length + 1 );
            menuItems.addAll( Arrays.asList( result ) );
            // Add Leaders menu item below MRecs.
            menuItems.add( 5, new DemoMenuItem( "Leaders", "728x90 leaderboard advertisement indented for tablets", new Intent( this, LeaderDemoMenuActivity.class ) ) );
            result = menuItems.toArray( result );
        }
        return result;
    }

    private void checkSdkKey()
    {
        final String sdkKey = AppLovinSdk.getInstance( getApplicationContext() ).getSdkKey();
        if ( "YOUR_SDK_KEY".equalsIgnoreCase( sdkKey ) )
        {
            new AlertDialog.Builder( this )
                    .setTitle( "ERROR" )
                    .setMessage( "Please update your sdk key in the manifest file." )
                    .setCancelable( false )
                    .setNeutralButton( "OK", null )
                    .show();
        }
    }

    private Intent getContactIntent()
    {
        Intent intent = new Intent( Intent.ACTION_SENDTO );
        intent.setType( "text/plain" );
        intent.setData( Uri.parse( "mailto:" + "support@applovin.com" ) );
        intent.putExtra( Intent.EXTRA_SUBJECT, "Android SDK support" );
        intent.putExtra( Intent.EXTRA_TEXT, "\n\n\n---\nSDK Version: " + AppLovinSdk.VERSION );
        return intent;
    }

    // Mute Toggling

    /**
     * Toggling the sdk mute setting will affect whether your video ads begin in a muted state or not.
     */
    private void toggleMute()
    {
        AppLovinSdk sdk = AppLovinSdk.getInstance( this );
        sdk.getSettings().setMuted( !sdk.getSettings().isMuted() );
        muteToggleMenuItem.setIcon( getMuteIconForCurrentSdkMuteSetting() );
    }

    private Drawable getMuteIconForCurrentSdkMuteSetting()
    {
        AppLovinSdk sdk = AppLovinSdk.getInstance( this );
        int drawableId = sdk.getSettings().isMuted() ? R.drawable.mute : R.drawable.unmute;

        if ( Build.VERSION.SDK_INT >= 22 )
        {
            return getResources().getDrawable( drawableId, getTheme() );
        }
        else
        {
            return getResources().getDrawable( drawableId );
        }
    }

    @Override
    protected void setupListViewFooter()
    {
        String appVersion = "";
        try
        {
            PackageInfo pInfo = getPackageManager().getPackageInfo( getPackageName(), 0 );
            appVersion = pInfo.versionName;
        }
        catch ( PackageManager.NameNotFoundException e )
        {
            e.printStackTrace();
        }

        final String versionName = Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
        final int apiLevel = Build.VERSION.SDK_INT;

        TextView footer = new TextView( getApplicationContext() );
        footer.setTextColor( Color.GRAY );
        footer.setPadding( 0, 20, 0, 0 );
        footer.setGravity( Gravity.CENTER );
        footer.setTextSize( 18 );
        footer.setText( "\nApp Version: " + appVersion +
                                "\nSDK Version: " + AppLovinSdk.VERSION +
                                "\nOS Version: " + versionName + "(API " + apiLevel + ")" );

        listView.addFooterView( footer );
        listView.setFooterDividersEnabled( false );
    }

    // Options Menu Functions

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        muteToggleMenuItem = menu.findItem( R.id.action_toggle_mute );
        muteToggleMenuItem.setIcon( getMuteIconForCurrentSdkMuteSetting() );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if ( item.getItemId() == R.id.action_toggle_mute )
        {
            toggleMute();
        }

        return true;
    }
}
