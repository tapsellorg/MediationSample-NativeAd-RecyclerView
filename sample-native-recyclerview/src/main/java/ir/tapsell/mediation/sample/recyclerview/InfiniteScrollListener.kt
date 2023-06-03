package ir.tapsell.mediation.sample.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * InfiniteScrollListener, which can be added to RecyclerView with addOnScrollListener
 * to detect the moment when RecyclerView was scrolled to the end.
 *
 * @param layoutManager LinearLayoutManager created in the Activity.
 */
abstract class InfiniteScrollListener(
    private val layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {
    /**
     * Callback method to be invoked when the RecyclerView has been scrolled
     *
     * @param recyclerView The RecyclerView which scrolled.
     * @param dx           The amount of horizontal scroll.
     * @param dy           The amount of vertical scroll.
     */
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (canLoadMoreItems()) {
            onScrolledToEnd()
        }
    }

    /**
     * Checks if more items can be loaded to the RecyclerView
     *
     * @return boolean Returns true if can load more items or false if not.
     */
    private fun canLoadMoreItems(): Boolean {
        val visibleItemsCount = layoutManager.childCount
        val totalItemsCount = layoutManager.itemCount
        val pastVisibleItemsCount = layoutManager.findFirstVisibleItemPosition()
        return visibleItemsCount + pastVisibleItemsCount >= totalItemsCount
    }

    /**
     * Callback method to be invoked when the RecyclerView has been scrolled to the end
     */
    abstract fun onScrolledToEnd()
}