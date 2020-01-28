package co.elpache.codelens

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.errors.RepositoryNotFoundException
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date


data class Commit(val id: String, val message: String, val commitTime: Long, val author: String = "") {
  fun date() = LocalDateTime.ofInstant(Instant.ofEpochMilli(commitTime), ZoneId.systemDefault())
}

/**
 * Todo: Git probably requires two abstractions? one to deal with git related commands on the current code and one
 * to deal with the evolutionary metrics stuff
 * The init method right now is making a lot of assumptions
 */
class GitRepository(path: String, val remoteUrl: String, val branch: String = "refs/heads/master") {
  val dir = File(path)
  var repo: Git? = null

  fun perDaySampling(days: Int): List<Commit> {
    return logs().groupBy {
      "${it.date().dayOfMonth}/${it.date().monthValue}/${it.date().year}"
    }.map { it.value.first() }.take(days)
  }

  fun init(): GitRepository {
    if (repo != null) return this

    dir.mkdir()

    dir.resolve(".git/index.lock").delete()

    repo = try {
      Git.open(dir)
    } catch (e: RepositoryNotFoundException) {
      Git.cloneRepository()
        .setURI(remoteUrl)
        .setDirectory(dir)
        .call()
    }


    repo!!
      .checkout()
      .setName("refs/heads/master")
      .call()

    repo!!.pull()

    return this
  }

  fun goToCurrent() {
    goTo(branch)
  }

  fun goTo(commit: String) {
    init()
    repo!!
      .checkout()
      .setName(commit)
      .call()
  }

  fun logs(): List<Commit> {
    init()
    return repo!!.log().call().map {
      Commit(it.id.name, it.shortMessage, it.authorIdent.`when`.time)
    }
  }

  private val masterId: ObjectId? get() = repo!!.repository.exactRef(branch).objectId

  fun commitsBetween(since: Date, until: Date): List<String> {
    init()
    return logsBetween(since, until).map { it.name }.reversed()
  }

  private fun logsBetween(since: Date, until: Date): List<RevCommit> {
    val between = CommitTimeRevFilter.between(since, until)
    return repo!!.log().add(masterId).setRevFilter(between).call().toList()
  }

  fun lastCommits(numCommits: Int) =
    logs(numCommits).reversed()


  fun logOf(filePath: String): List<Commit> {
    init()
    return repo!!.log().add(masterId).addPath(filePath).call().map {

      Commit(
        id = it.name,
        message = it.shortMessage,
        commitTime = it.authorIdent.`when`.time,
        author = it.authorIdent.name
      )
    }
  }

  private fun logs(numCommits: Int): List<Commit> {
    init()
    return repo!!.log().add(masterId).setMaxCount(numCommits).call().map {
      Commit(
        id = it.name,
        message = it.shortMessage,
        commitTime = it.authorIdent.`when`.time,
        author = it.authorIdent.name
      )
    }
  }

  fun close() {
    repo!!.close()
  }

}