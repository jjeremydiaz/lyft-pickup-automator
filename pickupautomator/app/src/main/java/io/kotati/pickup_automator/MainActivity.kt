package io.kotati.pickup_automator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ridePrice = findViewById<EditText>(R.id.ridePrice)
        val startButton = findViewById<Button>(R.id.startButton)

        startButton.visibility = View.INVISIBLE

        ridePrice.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val priceInput = ridePrice.text.toString().trim()

                if(!priceInput.isNotEmpty()) {
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
                } else {
                startButton.text = "Start"
                Toast.makeText(this, "Automation Service Terminated", Toast.LENGTH_SHORT).show();
            }

        // Start Service
        Intent()
        val serviceIntent = Intent(this,  AutomatorService::class.java)
        if (this != null) {
            this.startService(serviceIntent)
        }
    }
}
