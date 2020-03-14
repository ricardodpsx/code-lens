package co.elpache.codelens.extensions

import co.elpache.codelens.Commit
import co.elpache.codelens.GitRepository
import co.elpache.codelens.codeLoader.LanguageIntegration
import co.elpache.codelens.codeSearch.parser.SelectorFunction
import co.elpache.codelens.codeSearch.search.find
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.verticeOf
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.TreeMap

val repo = GitRepository("../code-examples", "")

val gitIntegrations = LanguageIntegration(
  applyMetrics = ::applyGitMetrics,
  onBaseCodeLoad = {
    SelectorFunction.addFunction("percentOfChanges", "file") { params, node ->
      percentOfChanges(
        repo.logOf(
          node.vertice.getString("path")
        ), (params.firstOrNull() ?: "7").toLong()
      )
    }
  },
  filePattern = ".*"
)

fun applyGitMetrics(ctx: CodeTree) {
  //Todo: Need factory configurations
  ctx.find("file").forEach { f ->
    val commits = repo.logOf(f.getString("path"))
    f["commits"] = commits.size
    commits.forEach { c ->
      ctx.addVertice(
        verticeOf(
          c.id,
          "type" to "commit",
          "id" to c.id,
          "author" to c.commitTime,
          "time" to c.commitTime,
          "message" to c.message
        )

      )
      ctx.addChild(ctx.rootDirVid(), c.id)
      ctx.addRelation("files", c.id, f.vid)
      ctx.addRelation("commits", f.vid, c.id)
    }
  }

  ctx.find("commit[{$>file|count()} as filesAffected]")
}

fun LocalDateTime.toEpochSecond() =
  toEpochSecond(OffsetDateTime.now().offset)

fun percentOfChanges(commits: List<Commit>, lastNDays: Long): Double {

  val changed = TreeMap<Long, Boolean>()

  var from = LocalDate.now().atStartOfDay().minusDays(lastNDays - 1)
  val to = LocalDate.now().atStartOfDay()

  while (from <= to) {
    changed[from.toEpochSecond()] = false
    from = from.plusDays(1)
  }

  commits.map {
    changed.floorKey(it.commitTime)?.let {
      changed[it] = true
    }
  }
  return String.format("%.3f", changed.filter { it.value }.size.toDouble() / lastNDays).toDouble()
}

fun today(minus: Long = 0) =
  LocalDate.now().atStartOfDay().minusDays(minus).toEpochSecond()
