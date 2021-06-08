fun String.toPageNumber(): Int? {
    if (isEmpty()) return null
    return kotlin.runCatching { toInt() }.getOrNull()
}