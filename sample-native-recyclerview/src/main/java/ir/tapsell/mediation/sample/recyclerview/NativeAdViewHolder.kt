package ir.tapsell.mediation.sample.recyclerview

import android.app.Activity
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class NativeAdViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bindAd(activity: Activity) {
        NativeAdProvider.bindAdView(
            activity = activity,
            parent = view.findViewById<CardView>(R.id.ad_container)
        )
    }
}