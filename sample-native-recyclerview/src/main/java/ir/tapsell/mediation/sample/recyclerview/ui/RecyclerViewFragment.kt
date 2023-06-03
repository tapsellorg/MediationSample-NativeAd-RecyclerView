package ir.tapsell.mediation.sample.recyclerview.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.tapsell.mediation.sample.recyclerview.*
import ir.tapsell.mediation.sample.recyclerview.MenuItem
import ir.tapsell.mediation.sample.recyclerview.RecyclerViewAdapter

class RecyclerViewFragment internal constructor(private val initialData: List<MenuItem> = listOf()) : Fragment() {
    // Used for an endless scrolling experience
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_recycler_view, container, false)
        val recyclerView = rootView.findViewById<View>(R.id.recycler_view) as RecyclerView

        progressBar = rootView.findViewById(R.id.progress_bar)

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView.
        recyclerView.setHasFixedSize(true)

        // Specify a linear layout manager.
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        savedInstanceState?.getInt(POSITION_BUNDLE_KEY)?.let {
            layoutManager.scrollToPositionWithOffset(it, 2)
        }

        // Specify an adapter.
        adapter = RecyclerViewAdapter(requireActivity(), getCurrentData(savedInstanceState))

        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : InfiniteScrollListener(layoutManager) {
            override fun onScrolledToEnd() {
                progressBar.visibility = View.VISIBLE
                (activity as MainActivity).loadMoreData {
                    recyclerView.post {
                        adapter.addNewItems(it)
                        progressBar.visibility = View.GONE
                    }
                }
            }
        })

        return rootView
    }

    override fun onDestroyView() {
        NativeAdProvider.destroyNativeAds()
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(ITEMS_BUNDLE_KEY, ArrayList(adapter.getCurrentItems()))
        outState.putInt(POSITION_BUNDLE_KEY, layoutManager.findFirstVisibleItemPosition())
    }

    private fun getCurrentData(savedInstanceState: Bundle?) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            savedInstanceState?.getParcelableArrayList(
                ITEMS_BUNDLE_KEY, MenuItem::class.java)?.toMutableList() ?: initialData.toMutableList()
        } else {
            savedInstanceState?.getParcelableArrayList<MenuItem>(
                ITEMS_BUNDLE_KEY)?.toMutableList() ?: initialData.toMutableList()
        }

    companion object {
        private const val ITEMS_BUNDLE_KEY = "items-bundle-key"
        private const val POSITION_BUNDLE_KEY = "position-bundle-key"
    }
}