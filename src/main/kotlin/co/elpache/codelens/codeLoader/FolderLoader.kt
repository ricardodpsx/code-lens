package co.elpache.codelens.codeLoader

import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.vDataOf
import mu.KotlinLogging
import java.io.File
import java.util.LinkedList

val ignorePatterns = listOf("(.*node_modules.*|\\.git)")


open class FolderLoader(val dir: File, val basePath: File = dir) : NodeLoader() {
  val file = dir

  private val logger = KotlinLogging.logger {}

  companion object {
    fun loadDir(src: String): FolderLoader {
      val dir = File(src)
      if (dir.isAbsolute)
        return FolderLoader(dir, File(src))

      val c = FolderLoader(File(src), File(src))

      languageSupportRegistry.forEach {
        it.onBaseCodeLoad(c.dir)
      }

      return c
    }
  }

  //Todo: long method
  override fun doLoad(): CodeTree {
    languageSupportRegistry.forEach {
      it.onBaseCodeLoad(dir)
    }

    val codeTree = CodeTree()

    data class Child(val node: File, val parent: VData?)

    val queue = LinkedList<Child>()
    queue.addLast(Child(dir, null))

    while (queue.isNotEmpty()) {
      val (cur, parent) = queue.removeFirst()
      try {
        if (cur.isDirectory) {
          val node = codeTree.addIfAbsent(
            vDataOf(
              "vid" to cur.path,
              "fileName" to cur.name, "type" to "dir", "name" to cur.name
            )
          )

          cur.listFiles()
            .filterNot { f ->
              ignorePatterns.any { f.path.matches(it.toRegex()) }
            }
            .forEach {
              queue.addLast(Child(it, node))
            }

          if (parent != null)
            codeTree.addChild(parent.vid, node.vid)

        } else {
          val loader = languageSupportRegistry.find {
            cur.path.matches(it.filePattern.toRegex())
          }?.fileLoaderBuilder

          if (loader != null)
            codeTree.addSubTree(loader(cur, basePath).doLoad(), parent!!.vid)
        }

      } catch (e: Exception) {
        logger.warn(e) { "problem opening directory ${cur.absolutePath}" }
      }
    }
    codeTree.rootVid = dir.path
    return codeTree
  }
}

