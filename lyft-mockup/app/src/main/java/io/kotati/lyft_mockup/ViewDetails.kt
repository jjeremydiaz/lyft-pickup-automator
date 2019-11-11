package io.kotati.lyft_mockup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_view_details.*
import kotlinx.android.synthetic.main.fragment_available_pickups2.*

class ViewDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_details)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Pickup Date here"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        var priceRange = intent.getStringExtra("priceRange")
        price_range_details.setText(priceRange)
        confirm_pickup_button.setOnClickListener {
            Log.d("Pickup confirmed: ", priceRange)

            //this.finish()
            // Simulate error dialog box
            val alert = AlertDialog.Builder(this)
            alert.setTitle("")
            alert.setMessage("This pickup is no longer available")
            alert.setPositiveButton("OK"){
                dialog, which ->

                Log.d("Errortest", "Errortest")
            }

            val dialog: AlertDialog = alert.create()

            dialog.show()
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d("Back", "back")

        onBackPressed()
        return true
    }
}
