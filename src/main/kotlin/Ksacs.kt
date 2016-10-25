import java.awt.GridBagLayout
import java.awt.MouseInfo
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JButton
import javax.swing.JFrame
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    println("Hello Kotlin")
    var myThread = Thread()
    var frame = JFrame("KSACS")
    frame.layout = GridBagLayout()
    var clickButton = JButton("Left Click")
    val clickAdapter : MouseAdapter = object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            super.mouseClicked(e)
            println("Click")
            if (!myThread.isAlive) {
                var oldX = MouseInfo.getPointerInfo().location.x
                var oldY = MouseInfo.getPointerInfo().location.y
                myThread = thread(true, true, null, "myThread", -1,
                        {
                            while (true) {
                                Thread.sleep(1000)
                                val newX = MouseInfo.getPointerInfo().location.x
                                val newY = MouseInfo.getPointerInfo().location.y
                                println("oldX $oldX - oldY $oldY newX $newX newY $newY")
                                if (oldX != newX && oldY != newY) {
                                    var robot = Robot()
                                    robot.mousePress(InputEvent.BUTTON1_MASK)
                                    robot.mouseRelease(InputEvent.BUTTON1_MASK)
                                    oldX = newX
                                    oldY = oldY
                                }
                            }
                        }
                )
            }
        }
    }
    clickButton.addMouseListener(clickAdapter)
    frame.add(clickButton)
    var stopButton = JButton("Stop")
    val stopAdapter : MouseAdapter = object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            super.mouseClicked(e)
            println(myThread.state)
            if (myThread.isAlive) {
                println("stop ${myThread.state}")
                myThread.interrupt()
            }
        }
    }
    stopButton.addMouseListener(stopAdapter)
    frame.add(stopButton)
    frame.pack()
    frame.isVisible = true
}