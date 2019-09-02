package io.kotati.lyft_mockup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentAdapter = TabAdapter(supportFragmentManager)
        pager.adapter = fragmentAdapter

        tab_layout.setupWithViewPager(pager)
    }
}
