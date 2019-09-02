package io.kotati.lyft_mockup

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_available_pickups2.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AvailablePickupsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AvailablePickupsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AvailablePickupsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_available_pickups2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Set refresh listener
        swipeRefresh1.setOnRefreshListener {
            swipeRefresh1.isRefreshing = false
            print("test")
        }
    }
}
