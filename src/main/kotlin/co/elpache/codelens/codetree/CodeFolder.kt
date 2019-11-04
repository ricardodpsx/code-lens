package co.elpache.codelens.codetree

import co.elpache.codelens.languages.js.buildParseCache
import java.io.File

open class CodeFolder(val dir: File, val basePath: File = dir) : CodeEntity(dir.name, "dir") {
  val file = dir

  companion object {
    fun load(src: String): CodeFolder {
      val dir = File(src)
      if (dir.isAbsolute)
        return CodeFolder(dir, File(src))

      val path = System.getProperty("user.dir")
      val c = CodeFolder(File("$path/$src"), File(path))


      //Todo: This doesn't belong here
      buildParseCache(c.dir)


      return c
    }
  }

  init {
    data.addAll(
      "fileName" to dir.name
    )
  }

  override fun expand() =
    try {
      dir.listFiles()
        .map {
          if (it.isDirectory) CodeFolder(
            it,
            basePath
          ) else CodeFile.loadFile(it.toString())
        }.filterNotNull()
    } catch (e: Exception) {
      throw RuntimeException("problem opening directory ${dir.absolutePath}", e)
    }

}