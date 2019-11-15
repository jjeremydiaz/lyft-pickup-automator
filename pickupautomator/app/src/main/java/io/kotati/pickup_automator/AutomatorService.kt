package io.kotati.pickup_automator

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.google.android.material.button.MaterialButton
import java.util.*
import kotlin.collections.ArrayList
import androidx.activity.OnBackPressedCallback

class AutomatorService  : AccessibilityService(){
    var mLayout: FrameLayout? = null
    val mainHandler = Handler(Looper.getMainLooper())
    var bRunning = false
    var defaultMin = "15"

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

    }

    override fun onInterrupt() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent != null) {
            Log.d("Extra", intent.extras?.getString("data").toString())
            defaultMin = intent.extras?.getString("data").toString()
        } else {
            Log.d("Extra", "None")
        }
        return Service.START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onServiceConnected() {
        // Create an overlay and display the action bar
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLayout = FrameLayout(this)
        val lp = WindowManager.LayoutParams()
        lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        lp.format = PixelFormat.TRANSLUCENT
        //FLAG_NOT_TOUCH_MODAL allows key inputs into the overlay while also allowing focus to be changed to the view behind the overlay
        //lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.BOTTOM
        val inflater = LayoutInflater.from(this)
        inflater.inflate(R.layout.service_overlay, mLayout)
        wm.addView(mLayout, lp)

        //configureScrollButton()
        //configureSwipeButton()
        parseLoop()
    }

    private fun parseLoop() {
        val mButton = mLayout?.findViewById(R.id.material_button) as MaterialButton
        mButton.setOnClickListener {
            // Create a timer interval for swiping and parsing
            if(!bRunning) {
                mainHandler.post(object : Runnable {
                    override fun run() {
                        Log.d("delay", "delay")
                        Log.d("defaultMin", defaultMin)

                        // Grab value from edit text
                        var priceRangeVal = defaultMin.toInt()

                        // Swipe down on lyft-mockup page
                        configureSwipeButton()

                        // Parse card
                        searchCards(rootInActiveWindow, priceRangeVal)

                        // If value within range click on view Details and confirm
                        mainHandler.postDelayed(this, 5000)
                    }
                })

                bRunning = !bRunning
            } else {
                // Stop the parse loop
                mainHandler.removeCallbacksAndMessages(null);

                bRunning = !bRunning
            }
        }
    }

    fun searchCards(root: AccessibilityNodeInfo, targetVal: Int) {
        val deque = ArrayDeque<AccessibilityNodeInfo>()
        deque.add(root)
        var priceRangeRes = listOf<String>()
        var regRange = "\\$([0-9]+) - \\$([0-9]+)".toRegex()
        var recyclerCount = 0

        while (!deque.isEmpty()) {
            // Print out node info
            val node = deque.removeFirst()
            var nodeCurrClass = node.className.toString()

            Log.d("className1", node.isClickable.toString() + " " + node.className.toString())

            if(nodeCurrClass == "androidx.recyclerview.widget.RecyclerView" && recyclerCount == 0) {
                //Log.d("className2", node.isClickable.toString() + " " + node.className.toString() + " " + node.text.toString())
                //node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                recyclerCount++
            } else if(nodeCurrClass == "androidx.recyclerview.widget.RecyclerView" && recyclerCount > 0) {
                recyclerCount = -1
                Log.d("recycler2", "recycler2 skipped")
                continue
            }

            if(nodeCurrClass == "android.widget.TextView" && node.text.toString() == "View details") {
                Log.d("Inside", node.isClickable().toString())
                //node.actionList.forEach(Log.d("action", ))
                Log.d("action1", node.actionList.joinToString(" "))
                // Check to see if target is greater than range minimum, if so simulate a click
                var minRange = priceRangeRes.get(1).toInt()
                if(targetVal <= minRange) {
                    Log.d("RangeVal", "Target val within range, now automating click")
                    //node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
                    Log.d("ParentNode", node.parent.className.toString() + " " + node.parent.isClickable.toString())
                    node.parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)

                    Thread.sleep(500)

                    //Accept the details and return
                    acceptPickup(rootInActiveWindow)
                    return
                } else {
                    Log.d("RangeErr", "Target val out of range")
                }

                //return
            } else if(nodeCurrClass == "android.widget.TextView") {
                Log.d("seeTextView", node.text.toString())

                // Check to see if price is in price range
                if(regRange.matches(node.text.toString())) {
                    Log.d("regex", node.text.toString())

                    // Empty old array list
                    priceRangeRes = listOf()

                    // Add new values
                    var curPriceRange = regRange.find(node.text.toString())
                    if (curPriceRange != null) {
                        priceRangeRes = curPriceRange.groupValues
                        Log.d("priceRangesRes", priceRangeRes.toString())
                    }
                }
                else{
                    Log.d("regex", "Did not match")
                }
            }
            //if (node.getActionList().contains(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD)) {
            //    return node
            //}
            for (i in 0 until node.getChildCount()) {
                deque.addLast(node.getChild(i))
            }
        }
    }

    fun acceptPickup(root: AccessibilityNodeInfo) {
        val deque = ArrayDeque<AccessibilityNodeInfo>()
        deque.add(root)
        while (!deque.isEmpty()) {
            // Print out node info
            val node = deque.removeFirst()
            var nodeCurrClass = node.className.toString()
            Log.d("acceptPickup", nodeCurrClass)

            // If error dialog box (when ride is already taken)
            /*
            if(nodeCurrClass == "android.widget.FrameLayout") {
                performGlobalAction(GLOBAL_ACTION_BACK)
                performGlobalAction(GLOBAL_ACTION_BACK)

                return
            }
            */

            if(nodeCurrClass == "android.widget.TextView" && node.text.toString() == "Confirm pickup") {
                Log.d("click", node.text.toString())

                // Press the confirm button
                //node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                node.parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)

                // Detect if an error dialog box pops up from a ride already being taken
                Thread.sleep(800)
                var rideErrorCheck = checkRideError(rootInActiveWindow)

                if(rideErrorCheck) {
                    // Manually go back (both the back button in app and the OS back button works
                    performGlobalAction(GLOBAL_ACTION_BACK)
                    Thread.sleep(800)
                    performGlobalAction(GLOBAL_ACTION_BACK)
                } else {
                    performGlobalAction(GLOBAL_ACTION_BACK)
                }

                Thread.sleep(800)
                configureSwipeButton()

                return
            }
            if(nodeCurrClass == "android.widget.TextView"){
                Log.d("acceptPickup2", node.text.toString())
            }

            for (i in 0 until node.getChildCount()) {
                deque.addLast(node.getChild(i))
            }
        }
    }
    fun checkRideError(root: AccessibilityNodeInfo): Boolean {
        val deque = ArrayDeque<AccessibilityNodeInfo>()
        deque.add(root)
        while (!deque.isEmpty()) {
            // Print out node info
            val node = deque.removeFirst()
            var nodeCurrClass = node.className.toString()
            Log.d("rideError", nodeCurrClass)

            if(nodeCurrClass == "android.widget.Button" && node.text.toString() == "OK") {
                return true
            }

            for (i in 0 until node.getChildCount()) {
                deque.addLast(node.getChild(i))
            }
        }
        return false
    }
    /*
    private fun findScrollableNode(root: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        val deque = ArrayDeque<AccessibilityNodeInfo>()
        deque.add(root)
        while (!deque.isEmpty()) {
            val node = deque.removeFirst()
            if (node.getActionList().contains(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD)) {
                return node
            }
            for (i in 0 until node.getChildCount()) {
                deque.addLast(node.getChild(i))
            }
        }
        return null
    }

    private fun configureScrollButton() {
        val scrollButton = mLayout?.findViewById(R.id.scroll) as Button
        scrollButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val scrollable = findScrollableNode(rootInActiveWindow)
                scrollable?.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD.id)
            }
        })
    }
    */

    @RequiresApi(Build.VERSION_CODES.N)
    private fun configureSwipeButton() {
        val swipePath = Path()
        swipePath.moveTo(1000F, 1000F)
        swipePath.lineTo(1000F, 1500F)
        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(swipePath, 0, 500))
        dispatchGesture(gestureBuilder.build(), null, null)
    }
}