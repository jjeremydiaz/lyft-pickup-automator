package io.kotati.pickup_automator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Start Service
        Intent()
        val serviceIntent = Intent(this,  AutomatorService::class.java)
        if (this != null) {
            this.startService(serviceIntent)
        }
    }
}
