package io.kotati.pickup_automator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import org.w3c.dom.Text
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ridePrice = findViewById<EditText>(R.id.ridePrice)
        val startButton = findViewById<Button>(R.id.startButton)

        startButton.visibility = View.INVISIBLE

        ridePrice.addTextChangedListener(object : TextWatcher {
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
        })

        //start or terminate automation service
        startButton.setOnClickListener {
            if (startButton.text == "Start") {
                startButton.text = "Stop"
                Toast.makeText(this, "Automation Service Started", Toast.LENGTH_SHORT).show();

                // Start Service
                //val serviceIntent = Intent(this, AutomatorService::class.java)
                //this?.startService(serviceIntent)
            } else {
                startButton.text = "Start"
                Toast.makeText(this, "Automation Service Terminated", Toast.LENGTH_SHORT).show();
            }
        }

        var serviceIntent = Intent(this, AutomatorService::class.java)

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

        //this?.startService(serviceIntent)
    }
}
