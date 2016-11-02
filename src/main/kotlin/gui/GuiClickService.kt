package gui

import api.ClickService
import java.awt.GridBagLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import javax.swing.JButton
import javax.swing.JFrame

class GuiClickService : ClickService {

    override fun getScheduledThreadPool(): ScheduledExecutorService = threadPool as ScheduledExecutorService

    var threadPool: ScheduledExecutorService? = null

    override fun buildScheduledThreadPool(): ScheduledExecutorService {
        return Executors.newScheduledThreadPool(10)
    }

    override fun click(periodInMilliseconds: Long) {
        threadPool = buildScheduledThreadPool()
        val frame = JFrame("KSACS")
        frame.layout = GridBagLayout()
        val clickButton = JButton("Left Click")
        val clickAdapter: MouseAdapter = object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                super.mouseClicked(e)
                startClick(periodInMilliseconds)
            }
        }
        clickButton.addMouseListener(clickAdapter)
        frame.add(clickButton)
        val stopButton = JButton("Stop")
        val stopAdapter: MouseAdapter = object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                super.mouseClicked(e)
                stopClick()
                threadPool = buildScheduledThreadPool()
            }
        }
        stopButton.addMouseListener(stopAdapter)
        frame.add(stopButton)
        frame.pack()
        frame.isVisible = true
    }

}