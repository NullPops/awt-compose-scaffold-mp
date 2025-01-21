package meteor.common.plugin.meteor

import io.github.nullpops.properties.Configuration
import io.github.nullpops.properties.ConfigurationItem
import meteor.plugin.meteor.FilterQuality

class MeteorConfig(plugin: MeteorPlugin) : Configuration(plugin.name) {
    val uiColor = ConfigurationItem(this, "UI color", "uicolor".key(), UIColor.GREEN)
    val filterQuality = ConfigurationItem(this, "Filter quality", "filterQuality".key(),
        FilterQuality.None
    )
    val test2 = ConfigurationItem(this, "1", "1".key(), false)
    val test1 = ConfigurationItem(this, "2", "2".key(), "")
    val testStr = ConfigurationItem(this, "3", "3".key(), 123321)
}