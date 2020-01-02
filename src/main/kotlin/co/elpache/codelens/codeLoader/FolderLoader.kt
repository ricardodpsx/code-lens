package co.elpache.codelens.codeLoader

import co.elpache.codelens.Factory
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.vDataOf
import mu.KotlinLogging
import java.io.File
import java.util.LinkedList

val ignorePatterns = listOf(".*node_modules.*")


open class FolderLoader(val dir: File, val basePath: File = dir) : NodeLoader {
  val file = dir

  private val logger = KotlinLogging.logger {}

  companion object {
    fun load(src: String): FolderLoader {
      Factory.initializeLanguageRegistry()
      val dir = File(src)
      if (dir.isAbsolute)
        return FolderLoader(dir, File(src))

      val c = FolderLoader(File(src), File(src))

      languageSupportRegistry.values.forEach {
        it.onBaseCodeLoad(c.dir)
      }

      return c
    }
  }

  override fun load(): CodeTree {
    languageSupportRegistry.values.forEach {
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
          val node = codeTree.addNode(
            cur.path,
            vDataOf("fileName" to cur.name, "type" to "dir", "name" to cur.name)
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
          val loader = languageSupportRegistry.entries.find {
            cur.path.matches(it.key.toRegex())
          }?.value?.fileLoaderBuilder ?: ::DefaultFileLoader

          codeTree.addSubTree(loader(cur, basePath).load(), parent!!.vid)
        }

      } catch (e: Exception) {
        logger.warn(e) { "problem opening directory ${cur.absolutePath}" }
      }
    }
    codeTree.rootVid = dir.path
    return codeTree
  }
}

