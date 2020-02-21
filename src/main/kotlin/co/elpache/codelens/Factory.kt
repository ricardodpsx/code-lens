package co.elpache.codelens

import co.elpache.codelens.app.database.AstStore
import co.elpache.codelens.codeLoader.FolderLoader
import co.elpache.codelens.extensions.gitIntegrations
import co.elpache.codelens.extensions.js.jsLanguageIntegration
import co.elpache.codelens.extensions.kotlin.kotlinLanguageIntegration
import co.elpache.codelens.tree.CodeTree
import mu.KotlinLogging
import org.springframework.context.ApplicationContext
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.PreDestroy


//Todo: Factory is becomming big
class Factory(
  val path: String = "./tmp", //Path to put the repository and navigate back and forth
  val currentCodePath: String = "../code-examples",
  repoUrl: String = "https://github.com/ricardodpsx/test-repo.git",
  val context: ApplicationContext? = null
) {

  val astCache = ConcurrentHashMap<String, CodeTree>()

  private val logger = KotlinLogging.logger {}

  //Todo: Move to property files or command line args
  val repo = GitRepository(path, repoUrl)

  fun createBaseCode(): CodeTree {

    logger.info { "loading current base code" }

    val codeTree = FolderLoader
      .loadDir(currentCodePath)
      .extensions(jsLanguageIntegration, kotlinLanguageIntegration, gitIntegrations)
      .load()

    logger.info { "Done loading current base code" }

    return codeTree
  }

  fun getAstStore() = context!!.getBean(AstStore::class.java)

  fun preloadCommits(commits: List<Commit>) {

    logger.info { "Preloading ${commits}" }
    commits
      .reversed()
      .forEach {
        fromCache(it.id) {
          logger.info { "Preloading ${it.id}" }
          repo.goTo(it.id)
          FolderLoader.loadDir(path).load()
        }
      }
  }

  fun loadCodeFromCommits(commits: List<Commit>): Map<Commit, CodeTree> {
    return commits.filter {
      astCache[it.id] != null
    }.map {
      it to astCache[it.id]!!
    }.toMap()
  }

  fun createBaseCode(commit: String): CodeTree? {
    return astCache[commit]
  }

  private fun fromCache(commit: String, loadCallback: () -> CodeTree): CodeTree {

    val fromMemory = {
      if (astCache.contains(commit)) logger.info { "in Memory hit" }
      astCache[commit]
    }

    val fromDb = { getAstStore().load(commit) }

    var first = fromMemory()

    if (first == null) first = fromDb()

    if (first == null) first = loadCallback()

    astCache[commit] = first
    getAstStore().save(commit, first)

    return first
  }


  @PreDestroy
  @Throws(Exception::class)
  fun onDestroy() {
    repo.close()
  }

}