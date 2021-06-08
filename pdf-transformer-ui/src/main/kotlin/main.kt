import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.unit.IntSize

fun main() = Window(
    size = IntSize(480, 320)
) {
    MaterialTheme {
        TransformerMainPage()
    }
}