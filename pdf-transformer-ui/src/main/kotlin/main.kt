import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.unit.IntSize
import ui.ResourceImage

fun main() = Window(
    title = "PDF",
    size = IntSize(480, 320),
    icon = ResourceImage.icon
) {
    MaterialTheme {
        TransformerMainPage()
    }
}