package meteor.common.events

import io.github.nullpops.properties.Configuration
import io.github.nullpops.properties.ConfigurationItem


class ConfigChanged(val item: ConfigurationItem<*>?) {
    fun affects(config: Configuration): Boolean {
        return config.items.contains(item)
    }
}