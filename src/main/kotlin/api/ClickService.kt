package api

import java.awt.MouseInfo.getPointerInfo
import java.awt.Robot
import java.awt.event.InputEvent
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask

interface ClickService {

    fun buildScheduledThreadPool(): ScheduledExecutorService {
        return Executors.newScheduledThreadPool(10)
    }

    fun getScheduledThreadPool(): ScheduledExecutorService

    fun click(periodInMilliseconds: Long)

    fun startClick(periodInMilliseconds: Long) {
        println("Starting clicking every $periodInMilliseconds milliseconds")
        val scheduledThreadPool = getScheduledThreadPool()
        var oldX = getPointerInfo().location.x
        var oldY = getPointerInfo().location.y
        scheduledThreadPool.scheduleAtFixedRate(timerTask {
            val newX = getPointerInfo().location.x
            val newY = getPointerInfo().location.y
            if (isMoving(newX, newY, oldX, oldY)) {
                val robot = Robot()
                robot.mousePress(InputEvent.BUTTON1_MASK)
                robot.mouseRelease(InputEvent.BUTTON1_MASK)
                oldX = newX
                oldY = oldY
            }
        }, 0, periodInMilliseconds, TimeUnit.MILLISECONDS)
    }

    fun stopClick() {
        if (!getScheduledThreadPool().isTerminated) {
            println("Stopping click")
            getScheduledThreadPool().shutdown()
        }
    }

    fun isMoving(newX: Int, newY: Int, oldX: Int, oldY: Int) = oldX != newX && oldY != newY
}