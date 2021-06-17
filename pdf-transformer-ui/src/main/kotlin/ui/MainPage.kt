import androidx.compose.desktop.AppManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import util.OutputOption
import util.toPageNumber
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDropEvent
import java.io.File

@Composable
fun TransformerMainPage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var path by remember { mutableStateOf("") }
        var option by remember { mutableStateOf(OutputOption.OnePicPerPage) }
        var startPageNumber by remember { mutableStateOf("1") }
        var endPageNumber by remember { mutableStateOf("1") }
        var isHandling by remember { mutableStateOf(false) }
        var resultMsg by remember { mutableStateOf("") }
        var isSuccess by remember { mutableStateOf(true) }

        PathInputTextField(path) { path = it }
        OutputMethodSelect { option = it }
        StartEndSelect(
            startPageNumber = startPageNumber,
            endPageNumber = endPageNumber,
            onStartNumberChanged = { startPageNumber = it },
            onEndNumberChanged = { endPageNumber = it }
        )
        Button(
            modifier = Modifier.padding(8.dp),
            onClick = {
                isHandling = true
                kotlin.runCatching {
                    require(path.trim().isNotEmpty()) { "文件路径不能为空!" }
                    if (option == OutputOption.OnePicPerPage) {
                        PDFTransformer.pdfToPng(
                            pdfFile = File(path),
                            startPageNumber = startPageNumber.toPageNumber() ?: 1,
                            endPageNumber = endPageNumber.toPageNumber() ?: Int.MAX_VALUE
                        )
                    } else if (option == OutputOption.SinglePic) {
                        PDFTransformer.pdfToSinglePng(
                            pdfFile = File(path),
                            startPageNumber = startPageNumber.toPageNumber() ?: 1,
                            endPageNumber = endPageNumber.toPageNumber() ?: Int.MAX_VALUE
                        )
                    }
                    isSuccess = true
                    resultMsg = "Success!"
                }.getOrElse {
                    isSuccess = false
                    resultMsg = it.message!!
                }
                isHandling = false
            },
            enabled = !isHandling
        ) {
            Text("确定")
        }
        if (resultMsg.isNotEmpty()) {
            Text(
                text = resultMsg,
                color = if (isSuccess) {
                    Color.Green
                } else {
                    Color.Red
                }
            )
        }
        val target = object : DropTarget() {
            override fun drop(evt: DropTargetDropEvent) {
                evt.acceptDrop(DnDConstants.ACTION_REFERENCE)
                val dropFiles = evt.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<*>
                dropFiles.first()?.let { path = (it as File).absolutePath }
            }
        }
        AppManager.windows.first().window.contentPane.dropTarget = target
    }
}

@Composable
fun PathInputTextField(path: String, onPathChanged: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(0.8f),
        value = path,
        onValueChange = { onPathChanged(it) },
        label = { Text("文件路径") },
        singleLine = true
    )
}

@Composable
fun OutputMethodSelect(onOptionChanged: (OutputOption) -> Unit) {
    val radioOptions = listOf("每页一图", "单图")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    Row(modifier = Modifier.padding(0.dp, 8.dp)) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            if (text == radioOptions[0]) {
                                onOptionChanged(OutputOption.OnePicPerPage)
                            } else {
                                onOptionChanged(OutputOption.SinglePic)
                            }
                            onOptionSelected(text)
                        }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = {
                        if (text == radioOptions[0]) {
                            onOptionChanged(OutputOption.OnePicPerPage)
                        } else {
                            onOptionChanged(OutputOption.SinglePic)
                        }
                        onOptionSelected(text)
                    }
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun StartEndSelect(
    startPageNumber: String,
    endPageNumber: String,
    onStartNumberChanged: (String) -> Unit,
    onEndNumberChanged: (String) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(0.8f)) {
        OutlinedTextField(
            modifier = Modifier.weight(1f).padding(8.dp, 0.dp),
            value = startPageNumber,
            onValueChange = { onStartNumberChanged(it) },
            label = { Text("起始页号") }
        )
        OutlinedTextField(
            modifier = Modifier.weight(1f).padding(8.dp, 0.dp),
            value = endPageNumber,
            onValueChange = { onEndNumberChanged(it) },
            label = { Text("结束页号") }
        )
    }
}