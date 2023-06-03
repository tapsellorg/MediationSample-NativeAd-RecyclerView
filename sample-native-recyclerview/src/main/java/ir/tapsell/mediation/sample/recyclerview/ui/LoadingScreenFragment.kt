package ir.tapsell.mediation.sample.recyclerview.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ir.tapsell.mediation.sample.recyclerview.R

/**
 * A simple [Fragment] subclass.
 */
class LoadingScreenFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading_screen, container, false)
    }
}