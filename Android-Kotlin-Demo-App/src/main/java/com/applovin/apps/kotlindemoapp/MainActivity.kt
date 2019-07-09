package com.applovin.apps.kotlindemoapp

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.applovin.apps.kotlindemoapp.banners.BannerDemoMenuActivity
import com.applovin.apps.kotlindemoapp.eventtracking.EventTrackingActivity
import com.applovin.apps.kotlindemoapp.interstitials.InterstitialDemoMenuActivity
import com.applovin.apps.kotlindemoapp.leaders.LeaderDemoMenuActivity
import com.applovin.apps.kotlindemoapp.mrecs.MRecDemoMenuActivity
import com.applovin.apps.kotlindemoapp.nativeads.NativeAdDemoMenuActivity
import com.applovin.apps.kotlindemoapp.rewarded.RewardedVideosDemoMenuActivity
import com.applovin.sdk.AppLovinSdk
import kotlinx.android.synthetic.main.activity_list.*
import java.util.*


class MainActivity : DemoMenuActivity()
{
    private lateinit var muteToggleMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        AppLovinSdk.getInstance(this).initializeSdk {
            // SDK finished initialization
        }

        // Mark that we are in a testing environment
        AppLovinSdk.getInstance(this).settings.isTestAdsEnabled = true

        // Set an identifier for the current user. This identifier will be tied to various analytics events and rewarded video validation
        AppLovinSdk.getInstance(this).userIdentifier = "support@applovin.com"

        // Check that SDK key is present in Android Manifest
        checkSdkKey()
    }

    override fun setupListViewFooter()
    {
        var appVersion = ""
        try
        {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            appVersion = pInfo.versionName
        }
        catch (e: PackageManager.NameNotFoundException)
        {
            e.printStackTrace()
        }

        val versionName = Build.VERSION_CODES::class.java.fields[android.os.Build.VERSION.SDK_INT].name
        val apiLevel = Build.VERSION.SDK_INT

        val footer = TextView(applicationContext)
        footer.setTextColor(Color.GRAY)
        footer.setPadding(0, 20, 0, 0)
        footer.gravity = Gravity.CENTER
        footer.textSize = 18f
        footer.text = "\nApp Version: $appVersion\nSDK Version: ${AppLovinSdk.VERSION}\nOS Version: $versionName (API Level $apiLevel)\n"

        list_view.addFooterView(footer)
        list_view.setFooterDividersEnabled(false)
    }

    private fun makeContactIntent(): Intent
    {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.type = "text/plain"
        intent.data = Uri.parse("mailto:" + "support@applovin.com")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Android SDK support")
        intent.putExtra(Intent.EXTRA_TEXT, "\n\n\n---\nSDK Version: ${AppLovinSdk.VERSION}")
        return Intent.createChooser(intent, "Send Email")
    }

    override fun getListViewContents(): Array<DemoMenuItem>
    {
        var items = arrayOf(
                DemoMenuItem("Interstitials", "Full screen ads. Graphic or video", Intent(this, InterstitialDemoMenuActivity::class.java)),
                DemoMenuItem("Rewarded Videos (Incentivized Ads)", "Reward your users for watching these on-demand videos", Intent(this, RewardedVideosDemoMenuActivity::class.java)),
                DemoMenuItem("Native Ads", "In-content ads that blend in seamlessly", Intent(this, NativeAdDemoMenuActivity::class.java)),
                DemoMenuItem("Banners", "320x50 Classic banner ads", Intent(this, BannerDemoMenuActivity::class.java)),
                DemoMenuItem("MRecs", "300x250 Rectangular ads typically used in-content", Intent(this, MRecDemoMenuActivity::class.java)),
                DemoMenuItem("Event Tracking", "Track in-app events for your users", Intent(this, EventTrackingActivity::class.java)),
                DemoMenuItem("Resources", "https://support.applovin.com/support/home", Intent(Intent.ACTION_VIEW, Uri.parse("https://support.applovin.com/support/home"))),
                DemoMenuItem("Contact", "support@applovin.com", makeContactIntent())
        )
        if (resources.getBoolean(R.bool.is_tablet))
        {
            val menuItems = ArrayList<DemoMenuItem>(items.size + 1)
            menuItems.addAll(Arrays.asList(*items))
            // Add Leaders menu item below MRecs.
            menuItems.add(5, DemoMenuItem("Leaders", "728x90 leaderboard advertisement indented for tablets", Intent(this, LeaderDemoMenuActivity::class.java)))
            items = menuItems.toTypedArray()
        }
        return items;
    }

    private fun checkSdkKey()
    {
        val sdkKey = AppLovinSdk.getInstance(applicationContext).sdkKey
        if ("YOUR_SDK_KEY".equals(sdkKey, ignoreCase = true))
        {
            AlertDialog.Builder(this)
                    .setTitle("ERROR")
                    .setMessage("Please update your sdk key in the manifest file.")
                    .setCancelable(false)
                    .setNeutralButton("OK", null)
                    .show()
        }
    }

    // Mute Toggling

    /**
     * Toggling the sdk mute setting will affect whether your video ads begin in a muted state or not.
     */
    private fun toggleMute()
    {
        val sdk = AppLovinSdk.getInstance(applicationContext)
        sdk.settings.isMuted = !sdk.settings.isMuted
        muteToggleMenuItem.icon = getMuteIconForCurrentSdkMuteSetting()
    }

    private fun getMuteIconForCurrentSdkMuteSetting(): Drawable
    {
        val sdk = AppLovinSdk.getInstance(applicationContext)
        val drawableId = if (sdk.settings.isMuted) R.drawable.mute else R.drawable.unmute

        if (Build.VERSION.SDK_INT >= 22)
        {
            return resources.getDrawable(drawableId, theme)
        }
        else
        {
            @Suppress("DEPRECATION")
            return resources.getDrawable(drawableId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean
    {
        muteToggleMenuItem = menu.findItem(R.id.action_toggle_mute).apply {
            icon = getMuteIconForCurrentSdkMuteSetting()
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == R.id.action_toggle_mute)
        {
            toggleMute()
        }

        return true
    }
}
