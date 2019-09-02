package io.kotati.lyft_mockup

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_available_pickups2.*
import kotlinx.android.synthetic.main.template_card_view.*


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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var mContext = this.context

        if(card_layout_1 != null) {
            card_layout_1.removeAllViews()
        }

        // Set refresh listener
        swipeRefresh1.setOnRefreshListener {
            swipeRefresh1.isRefreshing = false
            print("test")

            //val cv = CardView(this.context!!)
            //LayoutInflater.from(mContext).inflate(R.layout.template_card_view, this, false)
            val curScrollView = view.findViewById(R.id.card_layout_1) as LinearLayout
            //var cv = this.layoutInflater.inflate(R.layout.template_card_view, curScrollView, true)
            val layoutInflater : LayoutInflater = LayoutInflater.from(mContext)

            // Need to pass root (parent) view to not mess up layout
            val cv = layoutInflater.inflate(R.layout.template_card_view, curScrollView, false)

            var priceTextView = cv.findViewById(R.id.price_range) as TextView

            var randomInt = (4..60).shuffled().first()
            var randomInt2 = (12..22).shuffled().first()

            var priceRange = "$" + randomInt.toString() + "-" + (randomInt + randomInt2).toString()
            priceTextView.setText(priceRange)

            curScrollView.addView(cv)

            var btn = Button(mContext)
            btn.setText("View details")

            var linLayout = cv.findViewById(R.id.card_lin_layout) as LinearLayout
            linLayout.addView(btn)


            // Set a button listener to start a new activity for that card
            btn.setOnClickListener {
                val intent = Intent(mContext, ViewDetails::class.java)

                intent.putExtra("priceRange", priceRange)

                if(card_layout_1 != null) {
                    card_layout_1.removeAllViews()
                }

                startActivity(intent)
            }
        }
    }
}
