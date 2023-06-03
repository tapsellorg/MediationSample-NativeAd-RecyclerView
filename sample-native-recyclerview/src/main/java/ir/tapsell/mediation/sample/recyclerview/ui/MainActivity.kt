package ir.tapsell.mediation.sample.recyclerview.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import ir.tapsell.mediation.sample.recyclerview.*
import ir.tapsell.mediation.sample.recyclerview.MenuItem

/**
 * A simple activity showing the use of Tapsell Mediation native ads in a [RecyclerView] widget.
 */
class MainActivity : FragmentActivity() {
    // List of MenuItems that populate the RecyclerView.
    private val mRecyclerViewItems: MutableList<MenuItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            // Create new fragment to display a progress spinner while the data set for the
            // RecyclerView is populated.
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, LoadingScreenFragment())
                .commit()

            loadMoreData {
                loadMenu()
            }
        }
    }

    internal fun loadMoreData(callback: (List<MenuItem>) -> Unit) {
        // Update the RecyclerView item's list with menu items.
        mRecyclerViewItems.addAll(MenuItemProvider.fetchMenuItems(this))
        callback(mRecyclerViewItems)
    }

    private fun loadMenu() {
        // Create new fragment and transaction
        // Replace whatever is in the fragment_container view with this fragment,
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, RecyclerViewFragment(mRecyclerViewItems))
            .commit()
    }
}