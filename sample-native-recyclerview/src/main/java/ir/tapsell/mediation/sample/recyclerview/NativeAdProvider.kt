package ir.tapsell.mediation.sample.recyclerview

import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import ir.tapsell.mediation.Tapsell
import ir.tapsell.mediation.ad.request.RequestResultListener
import ir.tapsell.mediation.ad.views.ntv.NativeAdView
import ir.tapsell.mediation.ad.views.ntv.NativeAdViewContainer
import java.util.LinkedList
import java.util.Queue

/**
 * A singleton responsible for requesting for native ads and preparing them
 * for being added as recyclerView list items
 */
object NativeAdProvider {
    private const val TAG = "Tapsell"
    // The number of native ads to load in each request. Maximum possible value is 5
    private const val ADS_PER_REQUEST = 5

    private const val NEW_REQUEST_TRIGGER_COUNT = 3

    private const val ZONE_ID = "d217e3e6-0070-4120-925d-5d39d0298893"

    private val availableNewAds: Queue<String> = LinkedList()
    /** Holds the shown ads ids to be able to destroy them */
    private val shownAds = mutableListOf<String>()

    private val requestListener = object : RequestResultListener {
        override fun onFailure() {
            Log.e(TAG, "Native ad request failed.")
        }

        override fun onSuccess(adId: String) {
            Log.d(TAG, "New ad was successfully loaded")
            availableNewAds.add(adId)
        }
    }

    /**
     * Creates and binds a [NativeAdViewContainer] containing the ad asset views only if a new ad
     * is available
     *
     * Also triggers a new request check
     */
    internal fun bindAdView(activity: Activity, parent: ViewGroup) {
        availableNewAds.poll()?.let { newAdId ->
            val adViewContainer = activity.layoutInflater.inflate(R.layout.ad_item, null) as NativeAdViewContainer

            // Since the parent view is a recycled one, removing previous views is necessary
            // not to over-populate the container with views on top of each-other
            parent.removeAllViews()
            parent.addView(adViewContainer)

            showAdInContainer(newAdId, adViewContainer, activity)
        }

        requestForNewAdsIfNeeded()
    }

    internal fun destroyNativeAds() {
        shownAds.forEach {
            Tapsell.destroyNativeAd(it)
        }
        shownAds.clear()
    }

    internal fun adAvailable() = (availableNewAds.isNotEmpty() || shownAds.isNotEmpty()).also {
        if (!it) requestForNewAdsIfNeeded()
    }

    private fun showAdInContainer(id: String, adViewContainer: NativeAdViewContainer, activity: Activity) {
        Tapsell.showNativeAd(
            id,
            NativeAdView.Builder(adViewContainer)
                // The MediaView will display a video asset if one is present in the ad, and the
                // first image asset otherwise.
                .withMedia(adViewContainer.findViewById(R.id.ad_media))

                // Register the view used for each individual asset.
                .withTitle(adViewContainer.findViewById(R.id.ad_title))
                .withDescription(adViewContainer.findViewById(R.id.ad_description))
                .withCtaButton(adViewContainer.findViewById(R.id.ad_call_to_action))
                .withLogo(adViewContainer.findViewById(R.id.ad_icon))
                .withSponsored(adViewContainer.findViewById(R.id.ad_sponsored))
                .build(),
            activity
        )
    }

    /**
     * If the available ads count is less than [NEW_REQUEST_TRIGGER_COUNT], a new request is made
     */
    private fun requestForNewAdsIfNeeded() {
        if (shouldRequest()) Tapsell.requestMultipleNativeAds(ZONE_ID, ADS_PER_REQUEST, requestListener)
    }

    private fun shouldRequest() = availableNewAds.size < NEW_REQUEST_TRIGGER_COUNT
}