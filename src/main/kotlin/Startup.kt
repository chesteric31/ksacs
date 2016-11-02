import cmd.CmdClickService
import gui.GuiClickService

private val GUI = "-g"

private val CMD = "-c"

fun main(args: Array<String>) {
    var periodInMilliseconds = 1000L
    printBanner()
    if (args.isEmpty()) {
        printUsages()
        return
    }
    if (args.size > 1) {
        periodInMilliseconds = args[1].toLong()
    }
    when (args[0]) {
        GUI -> GuiClickService().click(periodInMilliseconds)
        CMD -> CmdClickService().click(periodInMilliseconds)
        else -> {
            printUsages()
            return
        }
    }
}

private fun printUsages() {
    val cmd = "java -jar ksacs.jar"
    println("Usage for graphical interface is: $cmd -g")
    println("Usage for command line is: $cmd -c")
}

private fun printBanner() {
    val banner = StringBuilder()
    banner.append(" _  __ _____         _____  _____ \n")
    banner.append("| |/ // ____|  /\\   / ____|/ ____|\n")
    banner.append("| ' /| (___   /  \\ | |    | (___  \n")
    banner.append("|  <  \\___ \\ / /\\ \\| |     \\___ \\\n")
    banner.append("| . \\ ____) / ____ \\ |____ ____) |\n")
    banner.append("|_|\\_\\_____/_/    \\_\\_____|_____/\n")
    println(banner.toString())
}