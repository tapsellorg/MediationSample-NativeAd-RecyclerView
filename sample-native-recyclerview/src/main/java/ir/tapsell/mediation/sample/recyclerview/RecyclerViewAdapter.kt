package ir.tapsell.mediation.sample.recyclerview

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.tapsell.mediation.sample.recyclerview.RecyclerViewAdapter.MenuItemViewHolder

/**
 * The [RecyclerViewAdapter] class.
 *
 * The adapter provides access to the items in the [MenuItemViewHolder]
 *
 * For this example app, the recyclerViewItems list contains only [MenuItem] and NativeAd types.
 *
 * @param activity An Activity to be used as a Context
 * @param recyclerViewItems The list of Native ads and menu items.
 *
 */
internal class RecyclerViewAdapter(
    private val activity: Activity,
    private val recyclerViewItems: MutableList<MenuItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * The [MenuItemViewHolder] class.
     * Provides a reference to each view in the menu item view.
     */
    inner class MenuItemViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val menuItemName: TextView = view.findViewById<View>(R.id.menu_item_name) as TextView
        val menuItemDescription: TextView = view.findViewById<View>(R.id.menu_item_description) as TextView
        val menuItemPrice: TextView = view.findViewById<View>(R.id.menu_item_price) as TextView
        val menuItemCategory: TextView = view.findViewById<View>(R.id.menu_item_category) as TextView
        val menuItemImage: ImageView = view.findViewById<View>(R.id.menu_item_image) as ImageView
    }

    override fun getItemCount(): Int =
        recyclerViewItems.size + adsCount(recyclerViewItems.size)
//        if (NativeAdProvider.adAvailable()) recyclerViewItems.size + adsCount(recyclerViewItems.size)
//        else recyclerViewItems.size

    /**
     * Determines the view type for the given position.
     */
    override fun getItemViewType(position: Int): Int =
        if (position != 0 && ((position + 1) % ADS_FREQUENCY) == 0 && NativeAdProvider.adAvailable()) NATIVE_AD_VIEW_TYPE
        else MENU_ITEM_VIEW_TYPE

    /**
     * Creates a new view for a menu item view or a Native ad view
     * based on the viewType. This method is invoked by the layout manager.
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == NATIVE_AD_VIEW_TYPE && NativeAdProvider.adAvailable()) {
            val holder = NativeAdViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(
                    R.layout.ad_item_container, viewGroup, false
                )
            )
            holder
        }
        else MenuItemViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.menu_item_container, viewGroup, false
            )
        )

    /**
     * Replaces the content in the views that make up the menu item view and the
     * Native ad view. This method is invoked by the layout manager.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == NATIVE_AD_VIEW_TYPE && NativeAdProvider.adAvailable()) {
            (holder as NativeAdViewHolder).bindAd(activity)
        }
        else {
            val menuItemHolder = holder as MenuItemViewHolder
            val menuItem = getItemForPosition(position)

            // Get the menu item image resource ID.
            val imageName = menuItem.imageName
            val imageResID = activity.resources.getIdentifier(
                imageName, "drawable",
                activity.packageName
            )

            // Add the menu item details to the menu item view.
            menuItemHolder.menuItemImage.setImageResource(imageResID)
            menuItemHolder.menuItemName.text = menuItem.name
            menuItemHolder.menuItemPrice.text = menuItem.price
            menuItemHolder.menuItemCategory.text = menuItem.category
            menuItemHolder.menuItemDescription.text = menuItem.description
        }
    }

    fun addNewItems(items: List<MenuItem>) {
        val currentCount = itemCount
        recyclerViewItems.addAll(items)
        notifyItemRangeInserted(currentCount - 1, items.size)
    }

    private fun adsCount(size: Int): Int = size / ADS_FREQUENCY

    private fun getItemForPosition(position: Int): MenuItem =
        recyclerViewItems[position - adsCount(position - 1)]

    companion object {
        // A menu item view type.
        private const val MENU_ITEM_VIEW_TYPE = 0
        private const val NATIVE_AD_VIEW_TYPE = 1

        private const val ADS_FREQUENCY: Int = 10 // frequency of ads in list
    }
}