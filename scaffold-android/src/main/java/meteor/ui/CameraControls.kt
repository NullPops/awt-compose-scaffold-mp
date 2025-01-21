package meteor.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.*
import meteor.common.Common.eventbus
import meteor.common.ext.kotlin.MutableStateExt.toggle
import meteor.common.ui.Colors
import meteor.ui.GamePanel.containerSize

object CameraControls {

    val ingame = mutableStateOf(false)

    init {

    }

    val showControls = mutableStateOf(true)
}