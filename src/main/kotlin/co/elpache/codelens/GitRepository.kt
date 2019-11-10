package co.elpache.codelens

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.errors.RepositoryNotFoundException
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter
import java.io.File
import java.time.LocalDate
import java.util.Date


data class Commit(val id: String, val message: String, val commitTime: Long) {
  fun date() = LocalDate.ofEpochDay(commitTime)
}

class GitRepository(path: String, val remoteUrl: String, val branch: String = "refs/heads/master") {
  val dir = File(path)
  var repo: Git? = null

  fun init(): GitRepository {
    if (repo != null) return this

    dir.mkdir()

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


  fun commitsBetween(from: Date, to: Date): List<String> {
    init()
    val masterId = repo!!.repository.exactRef(branch).getObjectId()
    val since = from
    val until = to
    val between = CommitTimeRevFilter.between(since, until)

    val commits = ArrayList<String>()
    for (commit in repo!!.log().add(masterId).setRevFilter(between).call())
      commits.add(commit.id.toString())
    return commits
  }

  fun log(path: String): List<String> {
    init()
    return repo!!.log().add(masterId).addPath(path).call().map { it.id.name }
  }

  private val masterId: ObjectId? get() = repo!!.repository.exactRef(branch).getObjectId()

  fun lastCommits(numCommits: Int): List<String> {
    init()
    return repo!!.log().add(masterId).setMaxCount(numCommits).call().map { it.name }
  }

}