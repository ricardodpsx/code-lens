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

class Factory(
  val path: String = "./tmp", //Path to put the repository and navigate back and forth
  val currentCodePath: String = "../code-examples",
  repoUrl: String = "https://github.com/ricardodpsx/test-repo.git",
  val context: ApplicationContext? = null
) {
  private val logger = KotlinLogging.logger {}

  companion object {
    fun initializeLanguageRegistry() {
      jsInit()
      kotlinInit()
    }
  }

  //Todo: Move to property files or command line args
  val repo = GitRepository(path, repoUrl)

  fun createBaseCode(): CodeTree {

    val codeTree = CodeLoader().expandFullCodeTree(FolderLoader.load(currentCodePath))

    logger.info { "Done loading code" }

    return codeTree
  }

  fun getAstDatabase() = context!!.getBean(AstRepository::class.java)

  fun createBaseCode(tree: CodeTree): CodeTree {
    return tree
  }

  val mapper = ObjectMapper().registerModule(KotlinModule())

  val astCache = ConcurrentHashMap<String, CodeTree>()


  fun preloadCommits(commits: List<Commit>) {
    repo
    logger.info { "Preloading ${commits}" }
    commits
      .filter { getAstDatabase().findByCommit(it.id) == null }
      .forEach {
        logger.info { "Preloading commit # ${it.id}" }
        val code = createBaseCode(it.id)
        getAstDatabase().save(AstRecord(it.id, mapper.writeValueAsString(code)))
      }
  }

  fun createBaseCode(commit: String): CodeTree {
    //In memory cache
    if (astCache[commit] != null) {
      logger.info { "in Memory hit" }
      return astCache[commit]!!
    }

    //Database cache
    val record = getAstDatabase().findByCommit(commit)

    val res = if (record != null)
      createBaseCode(mapper.readValue(record.ast, CodeTree::class.java))
    else {
      repo.goTo(commit)
      CodeLoader()
        .expandFullCodeTree(FolderLoader.load(path))
    }
    astCache[commit] = res
    return res
  }


  @PreDestroy
  @Throws(Exception::class)
  fun onDestroy() {
    repo.close()
  }

}