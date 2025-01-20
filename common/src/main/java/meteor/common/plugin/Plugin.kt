package meteor.common.plugin

import meteor.common.config.Config
import meteor.common.config.ConfigManager
import meteor.common.plugin.PluginManager.runningMap

open class Plugin(val name: String, var enabledByDefault: Boolean = false, var hidden: Boolean = false, var cantDisable: Boolean = false) : EventSubscriber() {
    var configuration: Config? = null
    var running = false

    init {
        if (hidden) {
            enabledByDefault = true
        }
    }

    open fun onStart() {

    }

    open fun onStop() {

    }

    fun start() {
        val enable = ConfigManager.get<Boolean>("plugin.$name.enabled", enabledByDefault) || cantDisable
        if (!enable && !hidden)
            return
        onStart()
        subscribeEvents(true)
        running = true
        runningMap[this] = true
    }

    fun stop() {
        unsubscribe()
        onStop()
        running = false
        runningMap[this] = false
    }

    inline fun <reified T> configuration(): T {
        configuration = T::class.constructors.first().call(this) as Config
        return configuration as T
    }
}