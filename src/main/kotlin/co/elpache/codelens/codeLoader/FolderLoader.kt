package co.elpache.codelens.codeLoader

import co.elpache.codelens.Factory
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.vDataOf
import java.io.File

open class FolderLoader(val dir: File, val basePath: File = dir) : NodeLoader {
  val file = dir

  companion object {
    fun load(src: String): FolderLoader {
      Factory.initializeLanguageRegistry()
      val dir = File(src)
      if (dir.isAbsolute)
        return FolderLoader(dir, File(src))

      val path = System.getProperty("user.dir")
      val c = FolderLoader(File("$path/$src"), File(path))

      languageSupportRegistry.values.forEach {
        it.onBaseCodeLoad(c.dir)
      }

      return c
    }
  }

  override fun traverse(visitor: (node: VData, parent: VData?) -> Unit, parent: VData?) {
    traverse(dir, visitor, parent)
  }
}

fun traverse(cur: File, visitor: (node: VData, parent: VData?) -> Unit, parent: VData?) {
  val data = vDataOf("fileName" to cur.name, "type" to "dir", "name" to cur.name)

  visitor(data, parent)

  try {
    cur.listFiles()
      .forEach {
        if (it.isDirectory) traverse(it, visitor, data)
        else FileLoader.loadFile(it.toString(), data, visitor)
      }
  } catch (e: Exception) {
    throw RuntimeException("problem opening directory ${cur.absolutePath}", e)
  }
}