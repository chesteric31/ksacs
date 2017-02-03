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
        var oldDevice = getPointerInfo().device
        var oldLocation = Location(oldX, oldY, oldDevice)
        scheduledThreadPool.scheduleAtFixedRate(timerTask {
            val newX = getPointerInfo().location.x
            val newY = getPointerInfo().location.y
            val newDevice = getPointerInfo().device
            val newLocation = Location(newX, newY, newDevice)
            if (isMoving(newLocation, oldLocation)) {
                val robot = Robot(newDevice)
                robot.mousePress(InputEvent.BUTTON1_MASK)
                robot.mouseRelease(InputEvent.BUTTON1_MASK)
                oldX = newX
                oldY = newY
                oldDevice = newDevice
                oldLocation = Location(oldX, oldY, oldDevice)
            }
        }, 0, periodInMilliseconds, TimeUnit.MILLISECONDS)
    }

    fun stopClick() {
        if (!getScheduledThreadPool().isTerminated) {
            println("Stopping click")
            getScheduledThreadPool().shutdown()
        }
    }

    fun isMoving(newLocation: Location, oldLocation: Location) : Boolean {
        return oldLocation.x != newLocation.x
                || oldLocation.y != newLocation.y
                || !oldLocation.device.equals(newLocation.device)
    }
}