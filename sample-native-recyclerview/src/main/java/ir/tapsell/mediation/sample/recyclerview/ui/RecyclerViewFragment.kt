package ir.tapsell.mediation.sample.recyclerview.ui

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // retain this fragment
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_recycler_view, container, false)
        val recyclerView = rootView.findViewById<View>(R.id.recycler_view) as RecyclerView

        progressBar = rootView.findViewById(R.id.progress_bar)

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView.
        recyclerView.setHasFixedSize(true)

        // Specify a linear layout manager.
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        // Specify an adapter.
        val adapter = RecyclerViewAdapter(requireActivity(), initialData.toMutableList())
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : InfiniteScrollListener(layoutManager) {
            override fun onScrolledToEnd() {
                progressBar.visibility = View.VISIBLE
                (activity as MainActivity).loadMoreData {
                    adapter.addNewItems(it)
                    progressBar.visibility = View.GONE
                }
            }
        })

        return rootView
    }

    override fun onDestroy() {
        NativeAdProvider.destroyNativeAds()
        super.onDestroy()
    }
}