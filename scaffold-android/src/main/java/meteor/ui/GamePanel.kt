package meteor.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntSize
import meteor.Game.fps
import meteor.Game.image
import meteor.common.Common.eventbus
import meteor.common.ext.kotlin.MutableStateExt.toggle
import meteor.common.ui.UI.filterQuality
import meteor.input.KeyboardController.handleKeyEvent
import meteor.input.KeyboardController.keyboardController

/**
 * This panel will contain the game view & compose overlays eventually
 */
object GamePanel {
    var pendingMove : android.graphics.Point? = null
    var pendingPress : android.graphics.Point? = null
    var pendingTap : android.graphics.Point? = null
    var pendingHold : android.graphics.Point? = null

    var sX = 0f
    var sY = 0f
    var stretchedWidth = mutableFloatStateOf(0f)
    var stretchedHeight = mutableFloatStateOf(0f)
    var xPadding = mutableFloatStateOf(0f)
    var yPadding = mutableFloatStateOf(0f)
    var halfXPadding = mutableFloatStateOf(0f)
    var halfYPadding = mutableFloatStateOf(0f)
    var fitScaleFactor = mutableFloatStateOf(0f)
    var containerSize = mutableStateOf(IntSize(789, 532))
    val dragging = mutableStateOf(false)
    val filter = mutableStateOf(FilterQuality.Medium)
    var density = 1f
    var touchScaleX = mutableFloatStateOf(0f)
    var touchScaleY = mutableFloatStateOf(0f)
    var mouseDown = false
    var waitFrame = 0
    var waitTapFrame = 0

    init {
    }
    var viewportImage = mutableStateOf<ImageBitmap?>(null)

    private fun android.graphics.Point.scaled(): android.graphics.Point {
        val scaledX = (x / touchScaleX.floatValue).toInt()
        val scaledY = (y / touchScaleY.floatValue).toInt()

        return android.graphics.Point(scaledX, scaledY);
    }

    val volumeKeys = listOf(Key.VolumeUp,  Key.VolumeDown, Key.VolumeMute)

    fun KeyEvent.isVolumeKey(): Boolean {
        return volumeKeys.contains(this.key)
    }

    fun drawViewportOntoImage(sourceBitmap: Bitmap, targetBitmap: Bitmap): Bitmap {
        val resultBitmap = targetBitmap.copy(Bitmap.Config.RGB_565, true)
        val canvas = Canvas(resultBitmap)
        val paint = Paint()
        canvas.drawBitmap(sourceBitmap, 8f, 11f, paint)
        return resultBitmap
    }

    lateinit var gamePanelFocusRequester: FocusRequester
    val gameInputText = mutableStateOf("")
    val hideInputBox = mutableStateOf(false)

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Game() {
    }
}