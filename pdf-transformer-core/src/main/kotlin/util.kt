import java.io.File

fun checkParamsValid(file: File, startPageNumber: Int, endPageNumber: Int) {
    require(file.isFile) {
        "${file.absolutePath} is not an valid file!"
    }
    require(startPageNumber >= 1 && endPageNumber >= startPageNumber - 1) {
        "starPageNumber must >=1 and endPageNumber must >= startPageNumber!"
    }
}