package cmd

import api.ClickService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class CmdClickService : ClickService {

    override fun getScheduledThreadPool(): ScheduledExecutorService = threadPool!!

    var threadPool: ScheduledExecutorService? = null

    override fun buildScheduledThreadPool(): ScheduledExecutorService {
        return Executors.newScheduledThreadPool(10)
    }

    override fun click(periodInMilliseconds: Long) {
        threadPool = buildScheduledThreadPool()
        startClick(periodInMilliseconds)
    }

}