package co.elpache.codelens

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File


class GitIT {

  @Test
  fun `Should initialize repository`() {

    File("tmp").deleteRecursively()

    val repo = initRepository()

    assertThat(
      repo.logs().any { it.message.contains("Kotlin code") }
    ).isTrue()

    assertThat(
      repo.logs().any { it.message.contains("Unexisting commit") }
    ).isFalse()
  }

  @Test
  fun `should sample commits`() {
    val repo = initRepository()
    assertThat(repo.perDaySampling(3).map {
      "${it.date().dayOfMonth}/${it.date().month}/${it.date().year}"
    }).containsExactly("27/DECEMBER/2019", "30/OCTOBER/2019", "20/OCTOBER/2019")
  }

  @Test
  fun `should list commits of last n days`() {
    val repo = initRepository()

    val commits = repo.commitsBetween(parseDate("2019-10-04"), parseDate("2019-10-05"))

    assertThat(commits.size).isEqualTo(3)
  }

  @Test
  fun `See commit id of file`() {
    val repo = initRepository()
    assertThat(repo.logOf("README.md").first().id).isEqualTo("123aeca976c663f9f5359aad5f17d6eefd20d434")
  }


  @Test
  fun `Should not throw if the repository exists`() {
    initRepository()

    initRepository()
  }

  @Test
  fun `Should list last N commits`() {
    val repo = initRepository()

    val commits = repo.lastCommits(2)
    println(commits)

    assertThat(commits.size).isEqualTo(2)
  }

  private fun initRepository(): GitRepository {
    return GitRepository(
      "tmp",
      "https://github.com/ricardodpsx/test-repo.git"
    )
  }


}