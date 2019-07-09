package com.applovin.apps.kotlindemoapp.nativeads

import android.content.Intent
import com.applovin.apps.kotlindemoapp.DemoMenuActivity
import com.applovin.apps.kotlindemoapp.DemoMenuItem

class NativeAdDemoMenuActivity : DemoMenuActivity()
{
    override fun getListViewContents(): Array<DemoMenuItem> = arrayOf(
            DemoMenuItem("Single ad", "Programmatically loading an ad using our open-source carousel view", Intent(this, NativeAdCarouselUIActivity::class.java)),
            DemoMenuItem("Multiple ads", "Simple native ads in a RecyclerView", Intent(this, NativeAdRecyclerViewActivity::class.java))
    )
}
