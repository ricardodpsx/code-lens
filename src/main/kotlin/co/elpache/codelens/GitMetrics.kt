package co.elpache.codelens

import co.elpache.codelens.codeLoader.LanguageIntegration
import co.elpache.codelens.codeLoader.languageSupportRegistry
import co.elpache.codelens.codeSearch.search.ContextNode
import co.elpache.codelens.tree.vDataOf
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.TreeMap

fun gitInit() {
  //Todo: This is wrong
  languageSupportRegistry["no-matter"] = gitIntegrations
}

val gitIntegrations = LanguageIntegration(
  applyMetrics = ::applyGitMetrics
)

fun applyGitMetrics(ctx: ContextNode) {
  //Todo: Need factory configurations
  val repo = GitRepository("../code-examples", "")

  ctx.find("file").forEach { f ->
    val commits = repo.logOf(f.data.getString("path"))
    commits.forEach { c ->
      f.tree.addIfAbsent(
        c.id, vDataOf(
          "type" to "commit",
          "id" to c.id,
          "author" to c.commitTime,
          "time" to c.commitTime
        )
      )
      f.tree.addChild(f.vid, c.id)
    }
  }
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


  return changed.filter { it.value }.size.toDouble() / lastNDays
}

fun today(minus: Long = 0) =
  LocalDate.now().atStartOfDay().minusDays(minus).toEpochSecond()
