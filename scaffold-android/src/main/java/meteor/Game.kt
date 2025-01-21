package meteor

import android.graphics.Bitmap
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

import ext.awt.BufferedImageExt.getPixels
import meteor.common.Common.eventbus
import java.lang.Thread.sleep
import java.net.InetAddress

object Game {
    var receivedDraw = false
    var fps = mutableIntStateOf(0)
    var recentDraws = ArrayList<Long>()
    var image = mutableStateOf<ImageBitmap?>(null)
}

