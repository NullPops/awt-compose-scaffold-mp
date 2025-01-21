package meteor

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.application
import meteor.audio.MidiPlayer
import meteor.audio.SoundPlayer
import meteor.common.Common
import meteor.common.Common.logger
import meteor.common.plugin.PluginManager
import meteor.ui.MeteorWindow.MeteorWindow

object Main {

    private val startupTime = System.currentTimeMillis()
    private var started = false

    init {
        Common.isAndroid = false
        MidiPlayer.init()
        SoundPlayer.init()
        Game.init()
        PluginManager.start()
    }

    @JvmStatic
    fun main(args: Array<String>) = application {
        MeteorWindow()

        if (!started) {
            LaunchedEffect(Unit) {
                val startupDuration = System.currentTimeMillis() - startupTime
                logger.info("Startup time: ${startupDuration}ms")
            }
            started = true
        }
    }
}