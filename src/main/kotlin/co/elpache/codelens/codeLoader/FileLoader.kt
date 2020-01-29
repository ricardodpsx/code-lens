package co.elpache.codelens.codeLoader

import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vertice
import co.elpache.codelens.tree.vDataOf
import java.io.File
import java.util.LinkedList


class DefaultFileLoader(file: File, basePath: File) : FileLoader<String>(file, "raw", basePath) {
  override fun parseFile(): String = ""

  override val contents: String get() = "<Excluded>"

  override fun doLoad(): CodeTree {
    val codeTree = CodeTree()
    codeTree.addIfAbsent(fileData().addAll("vid" to file.path))
    codeTree.rootVid = file.path
    return codeTree;
  }
}

abstract class FileLoader<T>(val file: File, val lang: String, val basePath: File) : NodeLoader() {
  val type = "file"
  val fileName = file.name
  val name: String = file.nameWithoutExtension
  val startOffset: Int = 0
  val endOffset: Int = file.length().toInt()
  open val contents by lazy { file.readText(Charsets.UTF_8) }

  fun fileData(): Vertice {
    return vDataOf(
      "fileName" to file.name,
      "name" to file.nameWithoutExtension,
      "extension" to file.extension,
      "type" to "file",
      "lang" to lang,
      "path" to file.relativeTo(basePath).toString(),
      "start" to 0,
      "end" to contents.length,
      "code" to contents
    )
  }

  open fun getValues(node: T): Map<String, Any> = mapOf()

  open fun getChildren(node: T): Map<String, T> = mapOf()

  abstract fun parseFile(): T

  private fun isInt(key: String) = key.matches("[0-9]+".toRegex())

  override fun doLoad(): CodeTree {
    data class Item(val node: T, val parent: Vertice, val key: String)

    val codeTree = CodeTree()
    val prefix = file.path.replace("-", "_").replace("/", "-")
    val fileNode = codeTree.addRoot(fileData().addAll("vid" to prefix))

    val list = LinkedList<Item>()
    list.addLast(Item(node = parseFile(), parent = fileNode, key = "body"))

    var i = 0
    while (list.isNotEmpty()) {
      val (unprocessedNode, parent, key) = list.removeFirst()

      val node = codeTree.addIfAbsent(vDataOf("vid" to "${prefix}-$i"))
      node.putAll(getValues(unprocessedNode))

      codeTree.addChild(parent.vid, node)

      getChildren(unprocessedNode).forEach {
        list.add(Item(it.value, node, it.key))
      }

      if (isInt(key)) node["index"] = key.toInt()
      else node["type"] = key

      i++
    }
    return codeTree
  }

}