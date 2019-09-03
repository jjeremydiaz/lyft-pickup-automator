package io.kotati.pickup_automator

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.graphics.Path
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import java.util.*

class AutomatorService  : AccessibilityService(){
    var mLayout: FrameLayout? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

    }

    override fun onInterrupt() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onServiceConnected() {
        // Create an overlay and display the action bar
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLayout = FrameLayout(this)
        val lp = WindowManager.LayoutParams()
        lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        lp.format = PixelFormat.TRANSLUCENT
        lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.TOP
        val inflater = LayoutInflater.from(this)
        inflater.inflate(R.layout.service_overlay, mLayout)
        wm.addView(mLayout, lp)

        //configureScrollButton()
        //configureSwipeButton()
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun configureSwipeButton() {
        val swipeButton = mLayout?.findViewById(R.id.swipe) as Button
        swipeButton.setOnClickListener {
            val swipePath = Path()
            swipePath.moveTo(1000F, 1000F)
            swipePath.lineTo(100F, 1000F)
            val gestureBuilder = GestureDescription.Builder()
            gestureBuilder.addStroke(GestureDescription.StrokeDescription(swipePath, 0, 500))
            dispatchGesture(gestureBuilder.build(), null, null)
        }
    }
     */
}