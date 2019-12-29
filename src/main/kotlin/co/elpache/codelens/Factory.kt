package co.elpache.codelens

import co.elpache.codelens.app.database.AstRecord
import co.elpache.codelens.app.database.AstRepository
import co.elpache.codelens.codeLoader.CodeLoader
import co.elpache.codelens.codeLoader.FolderLoader
import co.elpache.codelens.languages.js.jsInit
import co.elpache.codelens.languages.kotlin.kotlinInit
import co.elpache.codelens.tree.CodeTree
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
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

  companion object {
    fun initializeLanguageRegistry() {
      jsInit()
      kotlinInit()
      gitInit()
    }

  }

  //Todo: Move to property files or command line args
  val repo = GitRepository(path, repoUrl)

  fun createBaseCode(): CodeTree {

    logger.info { "loading current base code" }

    val codeTree = CodeLoader()
      .expandFullCodeTree(FolderLoader.load(currentCodePath))

    logger.info { "Done loading current base code" }

    return codeTree
  }

  fun getAstDatabase() = context!!.getBean(AstRepository::class.java)

  fun createBaseCode(tree: CodeTree): CodeTree {
    return tree
  }

  val mapper = ObjectMapper().registerModule(KotlinModule())


  fun preloadCommits(commits: List<Commit>) {

    logger.info { "Preloading ${commits}" }
    commits
      .reversed()
      .forEach {
        fromCache(it.id) {
          logger.info { "Preloading ${it.id}" }
          repo.goTo(it.id)
          CodeLoader().expandFullCodeTree(FolderLoader.load(path))
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

    val fromDb = {

      val record = getAstDatabase().findByCommit(commit)
      if (record != null) {
        logger.info { "DB hit" }
        createBaseCode(mapper.readValue(record.ast, CodeTree::class.java))
      } else null
    }


    var first = fromMemory()

    if (first == null) first = fromDb()

    if (first == null) first = loadCallback()

    astCache[commit] = first
    getAstDatabase().save(AstRecord(commit, mapper.writeValueAsString(first)))

    return first
  }


  @PreDestroy
  @Throws(Exception::class)
  fun onDestroy() {
    repo.close()
  }

}