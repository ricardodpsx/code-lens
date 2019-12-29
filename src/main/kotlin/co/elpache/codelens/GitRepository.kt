package co.elpache.codelens

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.errors.RepositoryNotFoundException
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter
import java.io.File
import java.time.LocalDate
import java.util.Date


data class Commit(val id: String, val message: String, val commitTime: Long, val author: String = "") {
  fun date() = LocalDate.ofEpochDay(commitTime)
}


/**
 * Todo: Git probably requires two abstractions? one to deal with git related commands on the current code and one
 * to deal with the evolutionary metrics stuff
 * The init method right now is making a lot of assumptions
 */
class GitRepository(path: String, val remoteUrl: String, val branch: String = "refs/heads/master") {
  val dir = File(path)
  var repo: Git? = null

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
      Commit(it.id.name, it.shortMessage, it.commitTime.toLong())
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
    logs(numCommits).map {
      Commit(
        id = it.name,
        message = it.shortMessage,
        commitTime = it.commitTime.toLong(),
        author = it.authorIdent.name
      )
    }.reversed()


  fun logOf(filePath: String): List<Commit> {
    init()
    return repo!!.log().add(masterId).addPath(filePath).call().map {
      Commit(
        id = it.name,
        message = it.shortMessage,
        commitTime = it.commitTime.toLong(),
        author = it.authorIdent.name
      )
    }
  }

  private fun logs(numCommits: Int): List<RevCommit> {
    init()
    return repo!!.log().add(masterId).setMaxCount(numCommits).call().toList()
  }

  fun close() {
    repo!!.close()
  }

}