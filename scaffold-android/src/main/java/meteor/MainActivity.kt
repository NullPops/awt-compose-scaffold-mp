package meteor

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import ext.android.ComponentActivityExt.setupActivity
import io.github.nullpops.properties.Properties
import meteor.common.Common
import meteor.common.plugin.PluginManager
import meteor.input.KeyboardController.keyboardController
import meteor.ui.Window.MeteorViewBox
import org.apache.harmony.awt.gl.font.FontManager
import java.awt.Font
import java.awt.image.BufferedImage


class MainActivity : ComponentActivity() {

    private val mainHandler = Handler(Looper.getMainLooper())
    var preventReplay = true

    lateinit var image: BufferedImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FontManager.createDefaultFont(applicationContext, "runescape.ttf", "RuneScape", Font.PLAIN, 12)
        image = BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB)
        image.createGraphics().let { graphics ->
            graphics.drawString("Hello World", 0f, 0f)
            println("Hello World")
        }

        Properties.configure(applicationContext.dataDir.toPath())
        loadMeteor()
        setContent {
            keyboardController = LocalSoftwareKeyboardController.current!!
            val focusRequester = remember { FocusRequester() }
            Box(modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)) {
                MeteorViewBox()
            }
        }
    }

    private fun loadMeteor() {
        setupActivity()
        setupMeteor()
        PluginManager.start()
    }

    private fun setupMeteor() {
        Common.isAndroid = true
        meteor.common.Configuration.init(applicationContext)
    }
}

