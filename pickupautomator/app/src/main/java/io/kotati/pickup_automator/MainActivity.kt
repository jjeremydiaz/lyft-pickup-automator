package io.kotati.pickup_automator

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.startButton)
        val toggleOn = findViewById<TextView>(R.id.toggleOn)
        val toggleOff = findViewById<TextView>(R.id.toggleOff)

        toggleOff.visibility = View.INVISIBLE

        /*fun isAccessServiceEnabled(context: Context): Boolean {
            val prefString =
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            return prefString.contains("${context.packageName}/${context.packageName}.${context.getString(R.string.app_name)}")
        }

        if(isAccessServiceEnabled(applicationContext)) {
            toggleOn.visibility = View.INVISIBLE
            //toggleOff.visibility = View.INVISIBLE
        }*/


        /*ridePrice.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val priceInput = ridePrice.text.toString().trim()

                if (!priceInput.isNotEmpty()) {
                    startButton.visibility = View.INVISIBLE
                } else {
                    startButton.visibility = View.VISIBLE
                }
            }
        })*/

        startButton.setOnClickListener {
            intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }

        // Start Service
        Intent()
        val serviceIntent = Intent(this,  AutomatorService::class.java)
        if (this != null) {
            this.startService(serviceIntent)
        }

        //var serviceIntent = Intent(this, AutomatorService::class.java)

        ridePrice.addTextChangedListener(object: TextWatcher {
            // Implement delay on text change
            var timer = Timer()
            val DELAY: Long = 2000 // ms

            override fun afterTextChanged(s: Editable) {
                timer.cancel()
                timer = Timer()
                timer.schedule(object: TimerTask() {
                    override fun run() {
                        Log.d("Edit", s.toString())

                        serviceIntent.putExtra("data", s.toString())
                        startService(serviceIntent)
                    }
                }, DELAY)

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
    }
}
