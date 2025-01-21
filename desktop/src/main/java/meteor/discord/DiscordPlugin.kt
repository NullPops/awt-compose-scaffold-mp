package meteor.discord

import meteor.common.plugin.Plugin


class DiscordPlugin : Plugin("Discord", true, cantDisable = true) {
    val config = configuration<DiscordConfig>()

    fun enabled() : Boolean {
        return config.enabled.get<Boolean>()
    }

    fun sendStatusLoggedOut() : Boolean {
        return config.sendStatusLoggedOut.get<Boolean>()
    }
}