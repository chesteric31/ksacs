package gui

import api.ClickService
import java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.concurrent.ScheduledExecutorService
import javax.swing.JButton
import javax.swing.JFrame

class GuiClickService : ClickService {

    override fun getScheduledThreadPool(): ScheduledExecutorService = threadPool as ScheduledExecutorService

    var threadPool: ScheduledExecutorService? = null

    override fun click(periodInMilliseconds: Long) {
        threadPool = buildScheduledThreadPool()
        val frame = JFrame("KSACS")
        frame.layout = GridBagLayout()
        val clickButton = JButton("Left Click")
        clickButton.addActionListener {  startClick(periodInMilliseconds) }
        frame.add(clickButton)
        val stopButton = JButton("Stop")
        stopButton.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                stopClick()
                threadPool = buildScheduledThreadPool()
            }
        })
        frame.add(stopButton)
        frame.pack()
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        locate(frame)
        frame.isVisible = true
    }

    private fun locate(frame: JFrame) {
        val graphicsEnvironment = getLocalGraphicsEnvironment()
        val defaultScreen = graphicsEnvironment.defaultScreenDevice
        val rectangle = defaultScreen.defaultConfiguration.bounds
        val x = rectangle.maxX - frame.width
        val y = rectangle.maxY - frame.height
        frame.setLocation(x.toInt(), y.toInt())
    }

}