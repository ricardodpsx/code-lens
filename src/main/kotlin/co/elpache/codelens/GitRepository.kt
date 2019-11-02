package co.elpache.codelens

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.errors.RepositoryNotFoundException
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

    repo!!.pull()

    return this
  }

  fun goTo(commit: String) {
    repo!!
      .checkout()
      .setName(commit)
      .call()
  }

  fun logs() = repo!!.log().call().map {
    Commit(it.id.name, it.shortMessage, it.commitTime.toLong())
  }


  fun commitsBetween(from: Date, to: Date): List<String> {
    val masterId = repo!!.repository.exactRef(branch).getObjectId()
    val since = from
    val until = to
    val between = CommitTimeRevFilter.between(since, until)

    val commits = ArrayList<String>()
    for (commit in repo!!.log().add(masterId).setRevFilter(between).call())
      commits.add(commit.id.toString())
    return commits
  }

  fun lastCommits(numCommits: Int): List<String> {
    val masterId = repo!!.repository.exactRef(branch).getObjectId()
    val commits = ArrayList<String>()

    for (commit in repo!!.log().add(masterId).setMaxCount(numCommits).call())
      commits.add(commit.name)
    return commits
  }

}