package co.elpache.codelens

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File
import java.time.Instant
import java.time.LocalDate

class GitIntegrationTest {

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
  fun `should list commits of last n days`() {
    val repo = initRepository()

    val commits = repo.commitsBetween(parseDate("2019-10-04"), parseDate("2019-10-05"))

    assertThat(commits.size).isEqualTo(3)
  }


  @Test
  fun `Should not throw if the repository exists`() {
    initRepository()

    initRepository()
  }

  private fun initRepository(): GitRepository {
    return GitRepository(
      "tmp",
      "https://github.com/ricardodpsx/test-repo.git"
    ).init()
  }


}