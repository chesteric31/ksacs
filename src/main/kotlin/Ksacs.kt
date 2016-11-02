import java.awt.GridBagLayout
import java.awt.MouseInfo
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.swing.JButton
import javax.swing.JFrame
import kotlin.concurrent.timerTask

fun main(args: Array<String>) {
    println("Hello Kotlin")
    val frame = JFrame("KSACS")
    frame.layout = GridBagLayout()
    var scheduledThreadPool = Executors.newScheduledThreadPool(10)
    val clickButton = JButton("Left Click")
    val clickAdapter: MouseAdapter = object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            super.mouseClicked(e)
            println("Click")
            print("$scheduledThreadPool")
            var oldX = MouseInfo.getPointerInfo().location.x
            var oldY = MouseInfo.getPointerInfo().location.y
            scheduledThreadPool.scheduleAtFixedRate(timerTask {
                val newX = MouseInfo.getPointerInfo().location.x
                val newY = MouseInfo.getPointerInfo().location.y
                println("oldX $oldX - oldY $oldY newX $newX newY $newY")
                if (oldX != newX && oldY != newY) {
                    val robot = Robot()
                    robot.mousePress(InputEvent.BUTTON1_MASK)
                    robot.mouseRelease(InputEvent.BUTTON1_MASK)
                    oldX = newX
                    oldY = oldY
                }
            }, 0, 1000, TimeUnit.MILLISECONDS)
        }
    }
    clickButton.addMouseListener(clickAdapter)
    frame.add(clickButton)
    val stopButton = JButton("Stop")
    val stopAdapter: MouseAdapter = object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            super.mouseClicked(e)
            if (!scheduledThreadPool.isTerminated) {
                scheduledThreadPool.shutdown()
                scheduledThreadPool = Executors.newScheduledThreadPool(10)
            }
        }
    }
    stopButton.addMouseListener(stopAdapter)
    frame.add(stopButton)
    frame.pack()
    frame.isVisible = true
}